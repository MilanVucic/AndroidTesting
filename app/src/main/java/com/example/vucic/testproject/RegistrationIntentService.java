/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.vucic.testproject;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class RegistrationIntentService extends IntentService {

    private static final String AUTHORIZED_ENTITY = "461171941868";

    public RegistrationIntentService() {
        super("RegIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            SharedPreferences preferences = this.getSharedPreferences("stuff", Context.MODE_PRIVATE);
            int userId = preferences.getInt("userId", 0);
            if (userId != 0) {
                InstanceID instanceID = InstanceID.getInstance(this);
                String deviceToken = instanceID.getToken(AUTHORIZED_ENTITY,
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("deviceToken", deviceToken);
                editor.apply();

                ApiRequests apiRequests = new ApiRequests();
                apiRequests.postDeviceToken(deviceToken, preferences);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
