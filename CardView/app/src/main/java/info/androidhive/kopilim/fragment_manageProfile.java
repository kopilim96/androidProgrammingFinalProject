package info.androidhive.kopilim;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class fragment_manageProfile extends Fragment {
    private EditText hp, address, id;
    private TextInputLayout tAddress, tPhone;
    private TextView email, fullName, Ic, Sex, emailVerify;
    private Button update, getData;

    private FirebaseAuth fire;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseReference;


    public fragment_manageProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_manage_profile, container, false);

        //RegisterProfileActivity activity = (RegisterProfileActivity)getActivity();
        //final String emoplyeeId = activity.getEmployeeID();
        fire = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Employee");

        id = (EditText)v.findViewById(R.id.empID);
        hp = (EditText)v.findViewById(R.id.empPh);
        address = (EditText)v.findViewById(R.id.empAddress);

        Ic = (TextView)v.findViewById(R.id.empIC);
        fullName = (TextView)v.findViewById(R.id.empName);
        email = (TextView)v.findViewById(R.id.empEmail);
        emailVerify = (TextView)v.findViewById(R.id.empVerifyEmail);
        Sex = (TextView)v.findViewById(R.id.empSex);
        //Email, name and IC will be generated.

        update = (Button)v.findViewById(R.id.profileBtn);
        getData = (Button)v.findViewById(R.id.getdata);

        tAddress = (TextInputLayout)v.findViewById(R.id.tAddress);
        tPhone = (TextInputLayout)v.findViewById(R.id.tPhone);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    String emailText = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    email.setText(emailText);
                }
            }
        };

        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String empId = id.getText().toString();
                if(TextUtils.isEmpty(empId)){
                    id.setError("Please Input Employee ID");
                }else {
                    searching(id.getText().toString());
                }

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = new ProgressDialog(getContext());
                dialog.setMessage("Processing");

                final String empId = id.getText().toString();
                final String empHp = hp.getText().toString();
                final String empAddress = address.getText().toString().trim();


                if (TextUtils.isEmpty(empHp)) {
                    hp.setError("Please Enter Phone Number");
                } else if (TextUtils.isEmpty(empAddress)) {
                    address.setError("Please Enter Address");
                } else {

                    dialog.show();

                    databaseReference.orderByChild("Employee ID").equalTo(empId)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists()) {
                                        databaseReference = FirebaseDatabase.getInstance()
                                                .getReference("Employee").child(empId);
                                        Employee employee =
                                                new Employee(empId, fullName.getText().toString(), Ic.getText().toString(),
                                                        Sex.getText().toString(), empHp, empAddress, email.getText().toString());

                                        databaseReference.setValue(employee);
                                        Toast.makeText(getContext(), "Profile Managed Successful", Toast.LENGTH_LONG).show();

                                        //Clear Text
                                        clearText();
                                        Intent intent = new Intent(getContext(), MainActivity.class);
                                        startActivity(intent);

                                        //End of Managing Profile
                                        dialog.dismiss();

                                    } else {
                                        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                        alertDialog.setTitle("Manage Profile");
                                        alertDialog.setMessage("Manage Profile Fail. Please check your Employee ID or " +
                                                "contact with HR Department");
                                        alertDialog.setButton(getActivity().getString(R.string.okText), new DialogInterface.OnClickListener() {
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
            }
        });

        return v;
    }
    public void searching(final String id){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("Employee");
        Query query = databaseReference.orderByChild("id").equalTo(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        if(data.child("id").exists()) {
                            Employee employee = data.getValue(Employee.class);
                            hp.setText(employee.getPhoneNum());
                            address.setText(employee.getAddress());
                            fullName.setText(employee.getFullName());
                            Ic.setText(employee.getIcNum());
                            Sex.setText(employee.getSex());
                            emailVerify.setText(employee.getEmail());

                            getData.setVisibility(View.GONE);
                            update.setVisibility(View.VISIBLE);
                            fullName.setVisibility(View.VISIBLE);
                            Ic.setVisibility(View.VISIBLE);
                            tAddress.setVisibility(View.VISIBLE);
                            tPhone.setVisibility(View.VISIBLE);
                            hp.setVisibility(View.VISIBLE);
                            address.setVisibility(View.VISIBLE);
                            Sex.setVisibility(View.VISIBLE);

                        }else{

                            Toast.makeText(getContext(),"Please Enter Correct Employee ID",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(getContext(),"No data found.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        fire.addAuthStateListener(mAuthListener);
    }

    public void clearText(){
        id.setText("");
        hp.setText("");
        address.setText("");

        getData.setVisibility(View.VISIBLE);

        update.setVisibility(View.INVISIBLE);
        fullName.setVisibility(View.INVISIBLE);
        Ic.setVisibility(View.INVISIBLE);
        tAddress.setVisibility(View.INVISIBLE);
        tPhone.setVisibility(View.INVISIBLE);
        hp.setVisibility(View.INVISIBLE);
        address.setVisibility(View.INVISIBLE);
        Sex.setVisibility(View.INVISIBLE);
    }

}
