package kr.devy.cest.client;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import kr.devy.cest.R;
import kr.devy.cest.db.ClassInfo;
import kr.devy.cest.http.APIManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AttendanceListActivity extends Activity {

    RecyclerView rv;
    TextView tv;
    AttendanceListAdapter ala;
    JsonArray array;
    private LinearLayoutManager mLayoutManager;
    ClassInfo info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);


        info = getIntent().getExtras().getParcelable("classinfo");

        tv= (TextView)findViewById(R.id.attendance_tv_name);
        tv.setText(info.getName());
        rv = (RecyclerView) findViewById(R.id.attendance_rv);
        array  = new JsonArray();
        ala = new AttendanceListAdapter(getApplicationContext(), array);
        ala.setOnItemClickListener(new AttendanceListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(C.TAG, ala.getItem(position).toString());
                Intent i = new Intent(AttendanceListActivity.this,AttendanceResultActivity.class);
                i.putExtra("classinfo",info);
                i.putExtra("uuid",ala.getItem(position).get("uuid").getAsString());
                i.putExtra("attendance_time",ala.getItem(position).get("attendance_time").getAsString().replace("T"," ").replace(".000Z", ""));
                startActivity(i);


            }
        });
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rv.setAdapter(ala);
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        getResults();
    }

    private void getResults() {
        APIManager.getInstance().getAPI().getAttendance(info.getC_class_id(), new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                JsonObject json = jsonElement.getAsJsonObject();
                if (json.get("status").getAsInt() == 200) {
                    array.addAll(json.get("results").getAsJsonArray());
                    ala.notifyDataSetChanged();
                } else if (json.get("status").getAsInt() == 500) {
                    Log.d("cest", "AttendanceResultActivity - " + json.get("error").getAsJsonObject().toString());
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

}
