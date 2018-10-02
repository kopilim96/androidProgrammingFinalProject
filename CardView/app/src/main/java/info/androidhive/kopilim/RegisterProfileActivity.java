package info.androidhive.kopilim;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by SCS on 6/2/2018.
 */

public class RegisterProfileActivity extends AppCompatActivity {
    private EditText name, ic, hp, address, id;
    private TextView email;
    private RadioGroup sex;
    private RadioButton male, female, radioButton;
    private Button update;

    private FirebaseAuth fire;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile);

        fire = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Employee");

        name = (EditText) findViewById(R.id.empName);
        ic = (EditText) findViewById(R.id.empIC);
        id = (EditText) findViewById(R.id.empID);
        hp = (EditText) findViewById(R.id.empPh);
        address = (EditText) findViewById(R.id.empAddress);
        email = (TextView) findViewById(R.id.empEmail);
        //Email will be generated.

        male = (RadioButton) findViewById(R.id.maleRadio);
        female = (RadioButton) findViewById(R.id.femaleRadio);

        update = (Button) findViewById(R.id.profileBtn);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    String emailText = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    email.setText(emailText);
                }
            }
        };

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = new ProgressDialog(RegisterProfileActivity.this);
                dialog.setMessage("Processing");

                final String empName = name.getText().toString().trim();
                final String empIc = ic.getText().toString();
                final String empId = id.getText().toString();
                final String empHp = hp.getText().toString();
                final String empAddress = address.getText().toString().trim();
                final String empEmail = email.getText().toString();


                if (TextUtils.isEmpty(empName)) {
                    name.setError("Please Enter Name");
                } else if (TextUtils.isEmpty(empIc)) {
                    ic.setError("Please Enter IC Number");
                } else if (TextUtils.isEmpty(empHp)) {
                    hp.setError("Please Enter Phone Number");
                } else if (TextUtils.isEmpty(empAddress)) {
                    address.setError("Please Enter Address");
                } else if (TextUtils.isEmpty(empId) || !(empId.toString().length() == 6)) {
                    id.setError("Enter Correct Employee ID");
                } else {

                    if (male.isChecked()) {
                        final String empSex = "male";

                        dialog.show();

                        databaseReference.orderByChild("Employee ID").equalTo(empId)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.exists()) {
                                            databaseReference = FirebaseDatabase.getInstance()
                                                    .getReference("Employee").child(empId);
                                            Employee employee = new Employee(empId, empName, empIc, empSex, empHp, empAddress, empEmail);
                                            databaseReference.setValue(employee);
                                            Intent passData = new Intent(getApplicationContext(), fragment_manageProfile.class);
                                            passData.putExtra("empID", empId);
                                            Toast.makeText(RegisterProfileActivity.this, "Profile Register Successful", Toast.LENGTH_LONG).show();

                                            //Clear Text
                                            clearText();

                                            //End of Managing Profile
                                            dialog.dismiss();
                                            Intent goToLogin = new Intent(RegisterProfileActivity.this, LoginActivity.class);
                                            startActivity(goToLogin);

                                        } else {
                                            final AlertDialog alertDialog = new AlertDialog.Builder(RegisterProfileActivity.this).create();
                                            alertDialog.setTitle("Register Profile Fail");
                                            alertDialog.setMessage("Register Profile Fail. Please check your Employee ID or " +
                                                    "contact with HR Department");
                                            alertDialog.setButton(getApplicationContext().getString(R.string.okText), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    alertDialog.dismiss();
                                                    id.setError("Please Check Your Employee ID");
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                    }//End of if else male.isCheck
                    else if (female.isChecked()) {
                        final String empSex = "female";
                        dialog.show();

                        databaseReference.orderByChild("Employee ID").equalTo(empId)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.exists()) {
                                            databaseReference = FirebaseDatabase.getInstance()
                                                    .getReference("Employee").child(empId);
                                            Employee employee = new Employee(empId, empName, empIc, empSex, empHp, empAddress,empEmail);
                                            databaseReference.setValue(employee);
                                            Toast.makeText(RegisterProfileActivity.this, "Profile Register Successful", Toast.LENGTH_LONG).show();

                                            //Clear Text
                                            clearText();

                                            //End of Managing Profile
                                            dialog.dismiss();

                                            //Finish and Back to Login
                                            Intent goToLogin = new Intent(RegisterProfileActivity.this, LoginActivity.class);
                                            startActivity(goToLogin);

                                        } else {
                                            final AlertDialog alertDialog = new AlertDialog.Builder(RegisterProfileActivity.this).create();
                                            alertDialog.setTitle("Register Profile Fail");
                                            alertDialog.setMessage("Register Profile Fail. Please check your Employee ID or " +
                                                    "contact with HR Department");
                                            alertDialog.setButton(getApplicationContext().getString(R.string.okText), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    alertDialog.dismiss();
                                                    id.setError("Please Check Your Employee ID");
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }//End if else statement female.isCheck


                }//End of Else textUtil

            }//End of onCLick
        });

    }//End of onCreate

    public String getEmployeeID(){
        return String.valueOf(id);
    }

    @Override
    public void onStart() {
        super.onStart();
        fire.addAuthStateListener(mAuthListener);
    }

    public void clearText(){
        id.setText("");
        name.setText("");
        ic.setText("");
        hp.setText("");
        address.setText("");
    }
}
