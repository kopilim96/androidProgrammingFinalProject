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
import com.google.firebase.auth.FirebaseAuth;


public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText email;
    private FirebaseAuth fire;
    private Button resetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        email = (EditText)findViewById(R.id.emailForgotPass);
        resetBtn = (Button)findViewById(R.id.forgotPassBtn);
        final ProgressDialog dialog = new ProgressDialog(ForgotPasswordActivity.this);

        fire = FirebaseAuth.getInstance();

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailReset = email.getText().toString();

                if(TextUtils.isEmpty(emailReset)){
                    email.setError("Input Email");
                    return;
                }

                dialog.setMessage("Processing...");
                dialog.show();

                fire.sendPasswordResetEmail(emailReset)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss();
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),
                                            "We have sent you instructions to reset your password!" +
                                                    ". Please Check",
                                            Toast.LENGTH_LONG).show();
                                    //Clear Text
                                    email.setText("");
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),
                                            "Fail To Send Reset Email",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }

    public void click(View view){
        Intent intent = null;
        switch(view.getId()){
            case R.id.goToMainPage:
                intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                break;
        }
        startActivity(intent);
    }
}
