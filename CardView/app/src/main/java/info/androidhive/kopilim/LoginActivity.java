package info.androidhive.kopilim;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button login;
    private FirebaseAuth fire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Hide Status Bar

        fire = FirebaseAuth.getInstance();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        login = (Button)findViewById(R.id.loginBtn);
        username = (EditText)findViewById(R.id.usernameLogin);
        password = (EditText)findViewById(R.id.passwordLogin);
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String usernameForLogin = username.getText().toString();
                final String passwordForLogin = password.getText().toString();

                    if (TextUtils.isEmpty(usernameForLogin)) {
                        username.setError("Input Username");
                        return;
                    }
                    if (TextUtils.isEmpty(passwordForLogin)) {
                        password.setError("Input Password");
                        return;
                    }

                    dialog.setMessage("Checking Email and Password..");
                    dialog.show();

                    fire.signInWithEmailAndPassword(usernameForLogin, passwordForLogin)
                            .addOnCompleteListener(LoginActivity.this,
                                    new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            //Progress Dialog
                                            dialog.dismiss();

                                            if (!task.isSuccessful()) {
                                                //If There is an Error
                                                Toast.makeText(LoginActivity.this,
                                                        "Login Fail. Please Check your Email or Password again",
                                                        Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(LoginActivity.this,
                                                        "Login Successful",
                                                        Toast.LENGTH_SHORT).show();

                                                Intent intent = null;
                                                intent = new Intent(LoginActivity.this, MainActivity.class);

                                                username.setText("");
                                                password.setText("");

                                                startActivity(intent);
                                            }
                                        }
                            });
            }
        });
    }

    public void click(View v){
        Intent intent = null;
        switch(v.getId()){
            case R.id.goToRegister:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                break;
            case R.id.goToGetPass:
                intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /*
    Navigation Bar Left Side
     */
    private void hideNavBar(){
        this.getWindow().getDecorView()
                .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                );
    }//ENd Method


}
