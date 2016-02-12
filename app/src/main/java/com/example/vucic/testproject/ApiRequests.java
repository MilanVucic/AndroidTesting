package com.example.vucic.testproject;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class ApiRequests {

    static  HTTPClient client = new HTTPClient();

    public boolean getAccessToken(String username, String password, SharedPreferences preferences) throws IOException, JSONException {
        Log.i("neeee", "da");
        final String payload = "{\"grant_type\": \"password\",\"client_id\": \"1\", \"client_secret\": \"secret\", \"username\": \""+username+"\", \"password\": \""+password+"\"}";
        String response = client.post("http://api.studentinfo.rs/oauth/access_token", payload);
        Log.i("rispons", response);
        JSONObject json = new JSONObject(response);
        Log.i("accessToken", json.toString());
        if (json.has("success")) {
            JSONObject success = json.getJSONObject("success");
            JSONObject data = success.getJSONObject("data");
            JSONObject oauth = data.getJSONObject("oauth");

            String accessToken = oauth.getString("access_token");

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("accessToken", accessToken);
            editor.apply();

            return true;
        }
        return false;
    }

    public boolean getUser(String email, String password, SharedPreferences preferences) throws IOException, JSONException {
        Log.i("daaaaa", "da");
        final String payload = "{\"email\": \""+email+"\", \"password\": \""+password+"\"}";
        String response = client.post("http://api.studentinfo.rs/auth", payload);
        JSONObject json = new JSONObject(response);
        Log.i("accessToken", json.toString());
        if (json.has("success")) {
            JSONObject success = json.getJSONObject("success");
            JSONObject data = success.getJSONObject("data");
            JSONObject user = data.getJSONObject("user");

            int userId = user.getInt("id");

            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("userId", userId);
            editor.apply();

            return true;
        }
        return false;
    }

    public boolean postDeviceToken(String token, SharedPreferences preferences) throws IOException, JSONException {
        int userId = preferences.getInt("userId", 0);
        if (userId != 0) {
            final String payload = "{\"token\": \"" + token + "\", \"userId\": \"" + userId + "\"}";
            String response = client.post("http://api.studentinfo.rs/deviceToken", payload);
            JSONObject json = new JSONObject(response);
            Log.i("responseJSON", json.toString());
            if (json.has("success")) {

                return true;
            }
        }
        return false;
    }
}
