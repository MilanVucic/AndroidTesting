package com.example.vucic.testproject;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class ApiRequests {

    static  HTTPClient client = new HTTPClient();

    public boolean getAccessToken(String username, String password, SharedPreferences preferences) {
        final String payload = "{\"grant_type\": \"password\",\"client_id\": \"1\", \"client_secret\": \"secret\", \"username\": \""+username+"\", \"password\": \""+password+"\"}";
        String response = null;
        try {
            response = client.post("@string/url"+"/oauth/access_token", payload);
            if (!response.contains("\"success\"")) {
                Log.i("neuspeh", "dayum");
                return false;
            }
            JSONObject json = new JSONObject(response);

            JSONObject success = json.getJSONObject("success");
            JSONObject data = success.getJSONObject("data");
            JSONObject oauth = data.getJSONObject("oauth");

            String accessToken = oauth.getString("access_token");

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("accessToken", accessToken);
            editor.apply();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean getUser(String email, String password, SharedPreferences preferences) {
        final String payload = "{\"email\": \""+email+"\", \"password\": \""+password+"\"}";
        String response = null;
        try {
            response = client.post("@string/url"+"/auth", payload);
            if (!response.contains("success")) {
                return false;
            }
            JSONObject json = new JSONObject(response);
            JSONObject success = json.getJSONObject("success");
            JSONObject data = success.getJSONObject("data");
            JSONObject user = data.getJSONObject("user");

            int userId = user.getInt("id");

            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("userId", userId);
            editor.apply();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean postDeviceToken(String token, SharedPreferences preferences) {
        int userId = preferences.getInt("userId", 0);
        if (userId != 0) {
            final String payload = "{\"token\": \"" + token + "\", \"userId\": \"" + userId + "\"}";
            String response = null;
            try {
                response = client.post("@string/url"+"/deviceToken", payload);
                JSONObject json = new JSONObject(response);
                if (json.has("success")) {

                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public JSONArray getLecturesForGroup(int groupId, SharedPreferences preferences){
        String accessToken = preferences.getString("accessToken", "");
        try {
            String response = client.get("http://api.studentinfo.rs"+"/raf/group/"+groupId+"?access_token="+accessToken);
            JSONObject json = new JSONObject(response);

            JSONObject success = json.getJSONObject("success");
            JSONObject data = success.getJSONObject("data");
            JSONObject group = data.getJSONObject("group");

            return group.getJSONArray("lectures");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }
}
