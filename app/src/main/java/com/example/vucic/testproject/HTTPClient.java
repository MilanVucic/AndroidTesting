package com.example.vucic.testproject;

import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HTTPClient {

    private static final String TAG = "HTTPClient";

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    public String post(String url, String json) throws IOException {
        Log.i(TAG, "Sending a post request with body:\n" + json + "\n to URL: " + url);

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        String responseString = response.body().toString();

        Log.i(TAG, "Received response: " + responseString);

        return responseString;
    }

    public String put(String url, String json) throws IOException {
        Log.i(TAG, "Sending a put request with body:\n" + json + "\n to URL: " + url);

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        Response response = client.newCall(request).execute();
        String responseString = response.body().toString();

        Log.i(TAG, "Received response: " + responseString);

        return responseString;
    }

    public String get(String url) throws IOException {
        Log.i(TAG, "Sending a get request to URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String responseString = response.body().toString();

        Log.i(TAG, "Received response: " + responseString);

        return responseString;
    }

    public String delete(String url) throws IOException {
        Log.i(TAG, "Sending a delete request to URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        Response response = client.newCall(request).execute();
        String responseString = response.body().toString();

        Log.i(TAG, "Received response: " + responseString);

        return responseString;
    }
}
