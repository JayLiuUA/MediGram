package medigram.medigram;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ProblemListActivity extends AppCompatActivity {
    final Context context = this;
    public ProblemList problemList = new ProblemList();
    public List<String> problemString;
    public ListView problemsView;
    public Problem chosenProblem;
    public ArrayAdapter<String> adapter;
    public String bodyLocation;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                // get the new edited problem from child activity
                Bundle bundleObject = data.getExtras();
                if (bundleObject == null){
                    Log.d("ISNULL", "YES, IT IS NULL");
                }

                chosenProblem = (Problem) bundleObject.getSerializable("editedProblem");

                // add the new problem
                problemList.addProblem(chosenProblem);
                problemString.add(chosenProblem.toString());

                adapter.notifyDataSetChanged();

            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                // some stuff that will happen if there's no result
            }
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* test*/
        bodyLocation = "Left Arm";
        Date date = new Date();

        Problem testproblem = new Problem("TestTitle",
                "TestDescription", date, "Test Bodylocation");
        problemList.addProblem(testproblem);
         /*end test*/

        setContentView(R.layout.activity_problem_list);

        problemsView = (ListView) findViewById(R.id.ProblemListView);
        problemString = problemList.getList().stream().map(Problem::toString).collect(Collectors.toList());
        adapter = new ProblemListAdapter(this,
                R.layout.problem_list_item, problemString);
        problemsView.setAdapter(adapter);

        Button addProblemBtn = (Button) findViewById(R.id.addProblemBtn);
        View.OnClickListener addProblemListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openEditor = new Intent(getApplicationContext(), EditProblemActivity.class);
                Bundle problem_bundle = new Bundle();

                Date date = new Date();
                Problem newProblem = new Problem("New Problem", "", date, "left arm");

                problem_bundle.putSerializable("chosenProblem", newProblem);
                openEditor.putExtras(problem_bundle);
                startActivityForResult(openEditor, 1);
            }
        };
        addProblemBtn.setOnClickListener(addProblemListener);
    }

    private class ProblemListAdapter extends ArrayAdapter<String> {
        private int layout;

        private ProblemListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            ViewHolder mainViewholder = null;
            if(convertView==null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.info = (TextView) convertView.findViewById(R.id.list_item_text);
                viewHolder.titleText = (TextView) convertView.findViewById(R.id.titleText);
                viewHolder.deleteBtn = (Button) convertView.findViewById(R.id.deleteBtn);
                viewHolder.editBtn = (Button) convertView.findViewById(R.id.editBtn);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            mainViewholder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                // remove emotion from emotion list and string from string list
                @Override
                public void onClick(View v) {
                    problemList.removeProblem(position);
                    problemString.remove(position);
                    notifyDataSetChanged();
                }
            });
            mainViewholder.editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    problemList.removeProblem(position);
                    problemString.remove(position);

                    Intent openEditor = new Intent(getApplicationContext(), EditProblemActivity.class);

                    // Pass list of emotion objects by using serializable
                    chosenProblem = problemList.getProblem(position);
                    Bundle problem_bundle = new Bundle();
                    problem_bundle.putSerializable("chosenProblem", chosenProblem);
                    openEditor.putExtras(problem_bundle);
                    startActivityForResult(openEditor, 1);
                    notifyDataSetChanged();
                }
            });
            mainViewholder.info.setText(getItem(position));
            mainViewholder.titleText.setText(problemList.getProblem(position).getProblemTitle());

            return convertView;

        }
    }
    public class ViewHolder {
        TextView info;
        Button deleteBtn;
        Button editBtn;
        TextView titleText;
    }

}
