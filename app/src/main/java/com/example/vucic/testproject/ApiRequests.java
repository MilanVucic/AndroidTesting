package com.example.vucic.testproject;


import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;


public class ApiRequests {

    static HTTPClient client = new HTTPClient();
    static String url = "http://api.studentinfo.rs";
    private static final String TAG = "ApiRequest";

    public boolean getAccessToken(String username, String password, SharedPreferences preferences) {
        final String payload = "{\"grant_type\": \"password\",\"client_id\": \"1\", \"client_secret\": \"secret\", \"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
        try {
            BufferedReader reader = client.post(url + "/oauth/access_token", payload);
            String response = readResponse(reader);
            if (!response.contains("\"success\"")) {
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
        final String payload = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";
        try {
            BufferedReader reader = client.post(url + "/auth", payload);
            String response = readResponse(reader);
            if (!response.contains("success")) {
                return false;
            }
            JSONObject json = new JSONObject(response);
            JSONObject success = json.getJSONObject("success");
            JSONObject data = success.getJSONObject("data");
            JSONObject user = data.getJSONObject("user");

            Log.i("USER: ", user.toString());

            int userId = user.getInt("id");
            String slug = user.getJSONObject("faculty").getString("slug");

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("slug", slug);
            editor.putInt("userId", userId);
            editor.apply();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean postDeviceToken(String token, SharedPreferences preferences) {
        String accessToken = preferences.getString("accessToken", "");
        if (!accessToken.equals("")) {
            final String payload = "{\"token\": \"" + token + "\", \"access_token\": \"" + accessToken + "\", \"active\": \"" + 1 + "\"}";
            try {
                BufferedReader reader = client.post(url + "/deviceToken", payload);
                String response = readResponse(reader);

                JSONObject json = new JSONObject(response);
                if (json.has("success")) {
                    return true;
                }
            } catch (JSONException e) {
                Log.i(TAG, "JSONException occurred:" + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.i(TAG, "IOException occurred:" + e.getMessage());
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean verifyAccessToken(String accessToken) {
        try {
            BufferedReader reader = client.get(url + "/verifyAccessToken?access_token=" + accessToken);
            String response = readResponse(reader);
            if (response.contains("success")) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public JSONArray getAllGroups(String accessToken, SharedPreferences preferences) {
        try {
            String slug = preferences.getString("slug", "");

            BufferedReader reader = client.get(url + "/" + slug + "/groups?access_token=" + accessToken);
            String response = readResponse(reader);
            if (response.contains("success")) {
                JSONObject json = new JSONObject(response);
                JSONObject success = json.getJSONObject("success");
                JSONObject data = success.getJSONObject("data");
                JSONArray groups = data.getJSONArray("groups");
                return groups;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean logout(String accessToken) {
        try {
            String payload = "{\"access_token\": \"" + accessToken + "\"}";
            BufferedReader reader = client.delete(url + "/auth", payload);
            String response = readResponse(reader);
            JSONObject json = new JSONObject(response);
            if (json.has("success")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deactivateDeviceToken(String deviceToken, String accessToken) {
        final String payload = "{\"active\": " + 0 + ",\"access_token\": \"" + accessToken + "\"}";
        try {
            BufferedReader reader = client.put(url + "/deviceToken/" + deviceToken, payload);
            String response = readResponse(reader);
            JSONObject json = new JSONObject(response);
            if (json.has("success")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String readResponse(BufferedReader reader) {
        StringBuilder response = new StringBuilder();
        String line;
        try {
            Log.i(TAG, "Reading response.");
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            Log.i(TAG, "Received response:" + response.toString());

        } catch (IOException e) {
            Log.i(TAG, "There was an error reading data. Error message: " + e.getMessage());
            e.printStackTrace();
        }

        return response.toString();
    }

    // samo privremeno za testiranje
    public JSONArray getLecturesForGroup(int groupId, SharedPreferences preferences) {
        String accessToken = preferences.getString("accessToken", "");
        String slug = preferences.getString("slug", "");
        try {
            BufferedReader reader = client.get(url + "/" + slug + "/group/" + groupId + "?access_token=" + accessToken);
            String response = readResponse(reader);
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
