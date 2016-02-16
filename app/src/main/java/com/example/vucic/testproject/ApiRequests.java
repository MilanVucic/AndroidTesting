package com.example.vucic.testproject;


import android.content.SharedPreferences;

import org.json.JSONArray;

import org.json.JSONObject;


public class ApiRequests {

    static HTTPClient client = new HTTPClient();
    static String url = "http://api.studentinfo.rs";

    public boolean getAccessToken(String username, String password, SharedPreferences preferences) {
        final String payload = "{\"grant_type\": \"password\",\"client_id\": \"1\", \"client_secret\": \"secret\", \"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
        try {
            String response = client.post(url + "/oauth/access_token", payload);
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
            String response = client.post(url + "/auth", payload);
            if (!response.contains("success")) {
                return false;
            }
            JSONObject json = new JSONObject(response);
            JSONObject success = json.getJSONObject("success");
            JSONObject data = success.getJSONObject("data");
            JSONObject user = data.getJSONObject("user");

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
                String response = client.post(url + "/deviceToken", payload);
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

    public boolean verifyAccessToken(String accessToken) {
        try {
            String response = client.get(url + "/verifyAccessToken?access_token=" + accessToken);
            JSONObject json = new JSONObject(response);
            if (json.has("success")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean logout(String accessToken) {
        try {
            String response = client.delete(url + "/auth?access_token=" + accessToken);
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
            String response = client.put(url + "/deviceToken/" + deviceToken, payload);
            JSONObject json = new JSONObject(response);
            if (json.has("success")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // samo privremeno za testiranje
    public JSONArray getLecturesForGroup(int groupId, SharedPreferences preferences) {
        String accessToken = preferences.getString("accessToken", "");
        String slug = preferences.getString("slug", "");
        try {
            String response = client.get(url + "/" + slug + "/group/" + groupId + "?access_token=" + accessToken);
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
