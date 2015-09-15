package kr.devy.cest.client;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import kr.devy.cest.R;
import kr.devy.cest.db.ClassInfo;
import kr.devy.cest.http.APIManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MenuActivity extends Activity implements View.OnClickListener{

    TextView tv_name;
    TextView tv_room;
    TextView tv_time;
    CardView cv_start;
    CardView cv_manage;

    ClassInfo info;
    int c_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        SharedPreferences pref = getSharedPreferences(C.PREF_NAME,MODE_PRIVATE);
        c_user_id = pref.getInt(C.PREF_C_USER_ID, -1);
        info = getIntent().getExtras().getParcelable("classinfo");

        tv_name = (TextView) findViewById(R.id.menu_tv_name);
        tv_room = (TextView) findViewById(R.id.menu_tv_room);
        tv_time = (TextView) findViewById(R.id.menu_tv_time);
        tv_name.setText(info.getName());
        tv_room.setText(info.getRoom());
        tv_time.setText(info.getTime());
        cv_start = (CardView) findViewById(R.id.menu_cv_start);
        cv_start.setOnClickListener(this);
        cv_manage = (CardView) findViewById(R.id.menu_cv_manage);
        cv_manage.setOnClickListener(this);
        Log.d("cest", info.toString());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_cv_start:
                startAttendance();
                break;
            case R.id.menu_cv_manage:
                manageAttendance();
                break;
        }
    }
    private void startAttendance(){
        APIManager.getInstance().getAPI().startAttendance(info.getC_class_id(), info.getName(), new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                JsonObject obj = jsonElement.getAsJsonObject();
                if(obj.get("status").getAsInt()==200){
                    if(obj.get("flag").getAsInt()==1){
                        //불렀음
                        Toast.makeText(getApplicationContext(),"출석을 시작하였습니다.",Toast.LENGTH_SHORT).show();
                        return;
                    }else if(obj.get("flag").getAsInt()==3){
                        //이미 부름
                        Toast.makeText(getApplicationContext(),"이미 출석이 진행중입니다.",Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        //error

                    }

                }else if(obj.get("status").getAsInt()==500){
                    //error
                }else{
                    //error
                }

                Toast.makeText(getApplicationContext(),"서버가 정상적으로 동작하지 않습니다\n 잠시 후 다시 이용해주십시오.",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Log.d(C.TAG,"error - " +error.toString());
            }
        });
    }

    private void manageAttendance(){

        Intent i = new Intent(this,AttendanceListActivity.class);
        i.putExtra("classinfo",info);
        startActivity(i);

    }





}
