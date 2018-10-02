package info.androidhive.kopilim;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by SCS on 5/31/2018.
 */

public class welcomePage extends AppCompatActivity {
    private static int TIME_OUT = 2500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(welcomePage.this, LoginActivity.class);
                startActivity(homeIntent);
                finish();
            }
        },TIME_OUT);
    }
}
