package info.androidhive.kopilim;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailED, password1ED, password2ED, tarcCode;
    private ProgressDialog progressDialog;
    private Button reg, goToMain;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        emailED = (EditText)findViewById(R.id.emailREG);
        password1ED = (EditText)findViewById(R.id.password1REG);
        password2ED = (EditText)findViewById(R.id.password2REG);
        tarcCode = (EditText)findViewById(R.id.tarcCode);

        reg = (Button)findViewById(R.id.regBtn);
        goToMain = (Button)findViewById(R.id.backToMainPageBtn);
        final ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);

        firebaseAuth = FirebaseAuth.getInstance();

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailED.getText().toString().trim();
                String password1 = password1ED.getText().toString().trim();
                String password2 = password2ED.getText().toString().trim();
                String varifyCode = tarcCode.getText().toString();


                if (TextUtils.isEmpty(email)) {
                    emailED.setError("Input Email");
                    return;
                }

                if (TextUtils.isEmpty(password1)) {
                    password1ED.setError("Input Password");
                    return;
                }

                if (password1.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password1.equalsIgnoreCase(password2)){
                    password2ED.setError("Password Not Matching");
                    return;
                }
                if(!varifyCode.equalsIgnoreCase("kopi123**") || TextUtils.isEmpty(varifyCode)){
                    tarcCode.setError("Enter Correct Verify Code");
                    return;
                }

                dialog.setMessage("Processing...");
                dialog.show();


                firebaseAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if(task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                            clearTextField();
                            Intent goToProfile = new Intent(RegisterActivity.this, RegisterProfileActivity.class);
                            startActivity(goToProfile);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Something is wrong... Please Check Again",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }//On create

    public void click(View v){
        Intent intent = null;
        switch(v.getId()){
            case R.id.backToMainPageBtn:
                intent = new Intent(RegisterActivity.this, LoginActivity.class);
                break;
        }
        startActivity(intent);
    }

    public void clearTextField(){
        emailED.setText("");
        password1ED.setText("");
        password2ED.setText("");
        //finish();
    }
}