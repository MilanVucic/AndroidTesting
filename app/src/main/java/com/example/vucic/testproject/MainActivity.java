package com.example.vucic.testproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private ApiRequests apiRequests = new ApiRequests();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button logout = (Button) findViewById(R.id.logout);


        preferences = this.getSharedPreferences("stuff", Context.MODE_PRIVATE);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Thread logout = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String accessToken = preferences.getString("accessToken", "");
                        String deviceToken = preferences.getString("deviceToken", "");
                        logout(accessToken, deviceToken);
                        setPreferences();
                        finish();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });

                logout.start();
            }
        });
    }

    private boolean logout(String accessToken, String deviceToken){
        apiRequests.deactivateDeviceToken(deviceToken, accessToken);
        return apiRequests.logout(accessToken);
    }

    private void setPreferences(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("accessToken", "");
        editor.putString("slug", "");
        editor.putInt("userId", 0);
        editor.apply();
    }


}
