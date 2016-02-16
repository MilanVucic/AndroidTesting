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

        //ne bitno samo testiranje
//            GregorianCalendar gc = new GregorianCalendar();
//            day = gc.get(gc.DAY_OF_WEEK);


        preferences = this.getSharedPreferences("stuff", Context.MODE_PRIVATE);

        // nije bitno
//        Thread groupLectures = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    JSONArray lectures = apiRequests.getLecturesForGroup(1, preferences);
//
//                    for(int i = 0; i < lectures.length(); i++){
//                        JSONObject lecture = lectures.getJSONObject(i);
//                        if (lecture.getJSONObject("time").getInt("startsAt")/86400 == day-2){
//                            show(lecture);
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        groupLectures.start();

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

    // ne bitno dalje :D samo za testiranje
//    private void show(JSONObject lecture){
//        ArrayList<Button> buttons = new ArrayList<>();
//        buttons.add((Button) findViewById(R.id.button1));
//        buttons.add((Button) findViewById(R.id.button2));
//        buttons.add((Button) findViewById(R.id.button3));
//        buttons.add((Button) findViewById(R.id.button4));
//        buttons.add((Button) findViewById(R.id.button5));
//        buttons.add((Button) findViewById(R.id.button6));
//        buttons.add((Button) findViewById(R.id.button7));
//        buttons.add((Button) findViewById(R.id.button8));
//        buttons.add((Button) findViewById(R.id.button9));
//        buttons.add((Button) findViewById(R.id.button10));
//        buttons.add((Button) findViewById(R.id.button11));
//        buttons.add((Button) findViewById(R.id.button12));
//        final ArrayList<View> views = new ArrayList<>();
//        views.add((View) findViewById(R.id.view1));
//        views.add((View) findViewById(R.id.view2));
//        views.add((View) findViewById(R.id.view3));
//        views.add((View) findViewById(R.id.view4));
//        views.add((View) findViewById(R.id.view5));
//        views.add((View) findViewById(R.id.view6));
//        views.add((View) findViewById(R.id.view7));
//        views.add((View) findViewById(R.id.view8));
//        views.add((View) findViewById(R.id.view9));
//        views.add((View) findViewById(R.id.view10));
//        views.add((View) findViewById(R.id.view11));
//        views.add((View) findViewById(R.id.view12));
//        try {
//            int startsAt = lecture.getJSONObject("time").getInt("startsAt");
//            int endsAt = lecture.getJSONObject("time").getInt("endsAt");
//            final int startHour = (((startsAt/60)-15)/60)%24;
//            final int lengthHour = (((endsAt - startsAt)/60)+15)/60;
//            //buttons.get(startHour-9).setText(lecture.getJSONObject("course").getString("name") + "    " + lecture.getJSONObject("classroom").getString("name"));
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

}
