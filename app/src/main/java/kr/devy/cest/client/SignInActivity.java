package kr.devy.cest.client;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;

import kr.devy.cest.R;
import kr.devy.cest.http.APIManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SignInActivity extends Activity implements View.OnClickListener {

    public static final int START_INTRO = 1;
    Button bt_register;
    EditText et_id;
    EditText et_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        startActivityForResult(new Intent(this, IntroActivity.class), START_INTRO);
        bt_register = (Button) findViewById(R.id.sign_in_bt_register);
        bt_register.setOnClickListener(this);
        et_id = (EditText) findViewById(R.id.sign_in_et_id);
        et_password = (EditText) findViewById(R.id.sign_in_et_password);

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case START_INTRO:

                break;

        }


    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in_bt_register) {
            String id = et_id.getText().toString();
            String password = et_password.getText().toString();

            if (checkEffectivness(id, password)) {
                background(id, password);
            }
        }
    }

    private boolean checkEffectivness(String id, String password) {
        if (id == null || password == null || id.equals("") || password.equals("")) {
            Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    private void background(String id, String password) {
        new AsyncTask<String, String, String>() {


            @Override
            public String doInBackground(String... params) {
                String result = null;
                String id = params[0];
                String password = params[1];
                String gcmid = null;
                try {

                    gcmid = InstanceID.getInstance(getApplicationContext()).getToken(C.SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (gcmid != null) {
                    result = "available";
                    APIManager.getInstance().getAPI().login(id, password, gcmid, C.type, new Callback<JsonElement>() {
                        @Override
                        public void success(JsonElement jsonElement, Response response) {
                            JsonObject json = jsonElement.getAsJsonObject();
                            if (!json.get("login").getAsBoolean()) {
                                Toast.makeText(getApplicationContext(), "아이디/비밀번호를 다시 한번 확인하세요.", Toast.LENGTH_SHORT).show();
                            } else {
                                //사용가능
                                saveUserId(json.get("c_user_id").getAsInt());
                            }
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            Toast.makeText(getApplicationContext(), "서버가 불안정합니다\n잠시후 다시 시도하세요.", Toast.LENGTH_SHORT).show();
                            Log.d("cest", "error : " + retrofitError.toString());
                        }
                    });
                }

                return result;
            }

            @Override
            public void onPostExecute(String result) {
                if (result == null) {
                    Toast.makeText(getApplicationContext(), "서비스를 이용하실 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

        }.execute(id, password);

    }


    private void saveUserId(int id) {
        Log.d("cest", "saveUserId - " + id);
        SharedPreferences pref = getSharedPreferences(C.PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(C.PREF_C_USER_ID, id);
        editor.commit();

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
