/**
 * Sources:
 * https://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents
 *
 */
package medigram.medigram;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddPatientActivity extends Activity {

    private Button confirmAddingBut;
    private EditText inputUserID;
    private String userID;
    private CareProvider careProvider;
    private PatientList patients;
    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        inputUserID = findViewById(R.id.input_userid);
        confirmAddingBut = findViewById(R.id.confirm_adding);

        // jump back to patient List activity
        confirmAddingBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get user's input
                inputUserID = findViewById(R.id.input_userid);
                userID = inputUserID.getText().toString();
                System.out.print(userID);
                if (userID.length() < 8) {
                    Toast toast = Toast.makeText(AddPatientActivity.this
                            , "Please enter a valid user ID."
                            , Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 320);
                    toast.show();
                }

                // get current logged in care provider
                careProvider = (CareProvider) getIntent().getSerializableExtra("CareProvider");
                patients = careProvider.getAssignedPatients();
                patient = patients.getPatientByID(userID);

                // check if the patient that we want to add exists in patient list
                if (patient != null) {
                    // check if the patient already assigned to current care provider
                    if (careProvider.patientAssigned(userID)) {
                        careProvider.assignPatient(patient); // add the patient
                        Intent intent = new Intent(AddPatientActivity.this, PatientListActivity.class);
                        Toast toast = Toast.makeText(AddPatientActivity.this
                                                    ,"Patient Added Successfully!"
                                                    , Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 320);
                        toast.show();
                        finish();
                    } else {
                        Toast toast = Toast.makeText(AddPatientActivity.this
                                                    , "This patient is already assigned to you."
                                                    , Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 320);
                        toast.show();
                    }
                } else {  // patient not found in patient list
                    Toast toast = Toast.makeText(AddPatientActivity.this
                                                , "No such patient found."
                                                , Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 320);
                    toast.show();
                }

            }
        });
    }



}
