package kr.devy.cest.client;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import kr.devy.cest.R;
import kr.devy.cest.db.AttendanceResult;
import kr.devy.cest.http.APIManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by pc on 2015-08-10.
 */
public class UpdateAttendanceDialog extends Dialog implements View.OnClickListener {

    TextView tv_a;
    TextView tv_l;
    TextView tv_f;
    AttendanceResult ar;
    Handler handler;

    public UpdateAttendanceDialog(Context ctx,AttendanceResult ar,Handler handler) {
        super(ctx);
        this.ar = ar;
        this.handler = handler;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_update_attendance);
        tv_a = (TextView) findViewById(R.id.update_attendance_a);
        tv_l = (TextView) findViewById(R.id.update_attendance_l);
        tv_f = (TextView) findViewById(R.id.update_attendance_f);
        tv_a.setOnClickListener(this);
        tv_l.setOnClickListener(this);
        tv_f.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.update_attendance_a:
                    updateAttendanceResult(1);
                break;
            case R.id.update_attendance_l:

                updateAttendanceResult(2);
                break;
            case R.id.update_attendance_f:

                updateAttendanceResult(3);
                break;

        }
    }


    void updateAttendanceResult(int result){
        APIManager.getInstance().getAPI().updateAttendanceResult(ar.getC_user_id(), ar.getUuid(), result, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                JsonObject obj = jsonElement.getAsJsonObject();
                Log.d(C.TAG,obj.toString());

                if(obj.get("success").getAsInt()==1){
                    //성공
                    handler.sendEmptyMessage(1);

                }else{

                }

                dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });



    }
}
