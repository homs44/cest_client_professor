package kr.devy.cest.client;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

public class GCMListenerService extends GcmListenerService {

    public static final int NOTFICATION_ID = 1000;
    @Override
    public void onMessageReceived(String from, Bundle data){


    }


    private void setNotification(){
       /*
        Log.d("cest", token.toString());

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(this, SignInActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setTicker("Attendance Token" );
        mBuilder.setSmallIcon(R.drawable.circle_green);
        mBuilder.setContentTitle("Token : " + token.getName());
        mBuilder.setContentText(token.getUuid());
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(pi);

        nm.notify(NOTFICATION_ID,mBuilder.build());*/
    }
}
