package kr.devy.cest.client;

import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class InstanceIDListenerService extends com.google.android.gms.iid.InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        try {

            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(C.SENDER_ID,
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
             Log.d(C.TAG, "onTokenRefresh  - token : " + token);


        } catch (IOException e) {

        }
    }
}
