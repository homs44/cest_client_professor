package kr.devy.cest.client;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

import kr.devy.cest.R;
import kr.devy.cest.db.AttendanceResult;
import kr.devy.cest.db.ClassInfo;
import kr.devy.cest.http.APIManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AttendanceResultActivity extends Activity {

    BaseExpandableAdapter bea;
    HashMap<Integer, ArrayList<AttendanceResult>> child;
    ClassInfo info;
    String uuid;
    String attendance_time;

    TextView tv_name;
    TextView tv_day;
    TextView tv_time;
    ExpandableListView elv;

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            getAttendanceResult(info.getC_class_id(), uuid);
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_result);


        info = getIntent().getExtras().getParcelable("classinfo");
        uuid = getIntent().getStringExtra("uuid");
        attendance_time = getIntent().getStringExtra("attendance_time");

        tv_name = (TextView) findViewById(R.id.attendance_result_tv_name);
        tv_day = (TextView) findViewById(R.id.attendance_result_tv_day);
        tv_time = (TextView) findViewById(R.id.attendance_result_tv_time);
        String temp[] = attendance_time.split("\\s");
        tv_day.setText(temp[0]);
        tv_time.setText(temp[1]);
        tv_name.setText(info.getName());

        elv = (ExpandableListView) findViewById(R.id.attendance_result_el);
        child = new HashMap<Integer, ArrayList<AttendanceResult>>();
        bea = new BaseExpandableAdapter(getApplicationContext(), child);
        elv.setAdapter(bea);
        getAttendanceResult(info.getC_class_id(), uuid);

        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                UpdateAttendanceDialog uad = new UpdateAttendanceDialog(AttendanceResultActivity.this, bea.getChild(groupPosition, childPosition),handler);
                uad.show();
                return true;
            }
        });

    }


    public void getAttendanceResult(int c_class_id, String uuid) {
        APIManager.getInstance().getAPI().getAttendanceResult(c_class_id, uuid, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                JsonObject json = jsonElement.getAsJsonObject();
                if (json.get("status").getAsInt() == 200) {
                    updateChild(json.get("results").getAsJsonArray(), child, bea);
                } else if (json.get("status").getAsInt() == 500) {
                    Log.d("cest", "AttendanceResultActivity - " + json.get("error").getAsJsonObject().toString());
                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


    }


    public void updateChild(JsonArray array, HashMap<Integer, ArrayList<AttendanceResult>> child, BaseExpandableAdapter bea) {
        if (child == null) {
            child = new HashMap<Integer, ArrayList<AttendanceResult>>();
        } else {
            child.clear();
        }

        for (int i = 0; i < array.size(); i++) {
            JsonObject jo = array.get(i).getAsJsonObject();
            AttendanceResult ar = new AttendanceResult();
            if (!jo.get("uuid").isJsonNull()) {
                ar.setUuid(jo.get("uuid").getAsString());
            }
            ar.setC_user_id(jo.get("c_user_id").getAsInt());
            ar.setName(jo.get("name").getAsString());
            if (!jo.get("reply_time").isJsonNull()) {
                ar.setReply_time(jo.get("reply_time").getAsString());
            }
            ar.setResult(jo.get("result").getAsInt());
            ar.setUser_id(jo.get("user_id").getAsString());

            if (child.get(ar.getResult()) == null) {
                ArrayList<AttendanceResult> list = new ArrayList<AttendanceResult>();
                list.add(ar);
                child.put(ar.getResult(), list);
            } else {
                child.get(ar.getResult()).add(ar);
            }

        }
        bea.notifyDataSetChanged();
    }

    public class BaseExpandableAdapter extends BaseExpandableListAdapter {


        private ArrayList<AttendanceTitle> groupList = null;
        private ViewTitle viewTitle = null;
        private HashMap<Integer, ArrayList<AttendanceResult>> childList = null;

        private LayoutInflater inflater = null;
        private ViewClass viewClass = null;


        public BaseExpandableAdapter(Context c, HashMap<Integer, ArrayList<AttendanceResult>> childList) {
            super();
            this.inflater = LayoutInflater.from(c);
            this.groupList = new ArrayList<AttendanceTitle>();
            init(this.groupList);
            this.childList = childList;
        }


        private void init(ArrayList<AttendanceTitle> group) {
            group.add(new AttendanceTitle(3, "결석", "#E91E63"));
            group.add(new AttendanceTitle(2, "지각", "#9E9E9E"));
            group.add(new AttendanceTitle(1, "출석", "#3F51B5"));

        }

        public class AttendanceTitle {
            int result;
            String result_name;
            String color;

            public AttendanceTitle(int result, String result_name, String color) {
                this.result = result;
                this.result_name = result_name;
                this.color = color;
            }

            public int getResult() {
                return result;
            }

            public String getResult_name() {
                return result_name;
            }

            public String getColor() {
                return color;
            }
        }

        // 그룹 포지션을 반환한다.
        @Override
        public AttendanceTitle getGroup(int groupPosition) {
            return groupList.get(groupPosition);
        }

        // 그룹 사이즈를 반환한다.
        @Override
        public int getGroupCount() {
            return groupList.size();
        }

        // 그룹 ID를 반환한다.
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }


        class ViewTitle {
            public TextView tv_title;
        }


        // 그룹뷰 각각의 ROW
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                viewTitle = new ViewTitle();
                v = inflater.inflate(R.layout.item_attendance_title, parent, false);
                viewTitle.tv_title = (TextView) v.findViewById(R.id.item_attendance_title_tv);

                v.setTag(viewTitle);
            } else {
                viewTitle = (ViewTitle) v.getTag();
            }

            // 그룹을 펼칠때와 닫을때 아이콘을 변경해 준다.
            AttendanceTitle vo = getGroup(groupPosition);
            viewTitle.tv_title.setText(vo.getResult_name());
            viewTitle.tv_title.setTextColor(Color.parseColor(vo.getColor()));


            ((ExpandableListView) parent).expandGroup(groupPosition);
            return v;
        }

        // 차일드뷰를 반환한다.
        @Override
        public AttendanceResult getChild(int groupPosition, int childPosition) {
            if (childList.get(groupList.get(groupPosition).getResult()) == null) {
                return null;
            } else {
                return childList.get(groupList.get(groupPosition).getResult()).get(childPosition);
            }


        }

        // 차일드뷰 사이즈를 반환한다.
        @Override
        public int getChildrenCount(int groupPosition) {
            if (childList.get(groupList.get(groupPosition).getResult()) == null) {
                return 0;
            } else {
                return childList.get(groupList.get(groupPosition).getResult()).size();
            }
        }

        // 차일드뷰 ID를 반환한다.
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        // 차일드뷰 각각의 ROW
        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                viewClass = new ViewClass();
                v = inflater.inflate(R.layout.item_attendance_result, null);
                viewClass.tv_student_num = (TextView) v.findViewById(R.id.item_attendance_result_tv_num);
                viewClass.tv_student_name = (TextView) v.findViewById(R.id.item_attendance_result_tv_name);
                viewClass.tv_attendance_result = (TextView) v.findViewById(R.id.item_attendance_result_tv_result);
                v.setTag(viewClass);
            } else {
                viewClass = (ViewClass) v.getTag();
            }

            AttendanceResult ar = getChild(groupPosition, childPosition);
            viewClass.tv_student_num.setText(ar.getUser_id());
            viewClass.tv_student_name.setText(ar.getName());


            if (ar.getResult() == 1) {
                viewClass.tv_attendance_result.setText("출석");
                viewClass.tv_attendance_result.setTextColor(Color.parseColor("#3F51B5"));
            } else if (ar.getResult() == 2) {
                viewClass.tv_attendance_result.setText("지각");
                viewClass.tv_attendance_result.setTextColor(Color.parseColor("#9E9E9E"));
            } else {
                viewClass.tv_attendance_result.setText("결석");
                viewClass.tv_attendance_result.setTextColor(Color.parseColor("#E91E63"));
            }

            return v;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        class ViewClass {
            public TextView tv_student_num;
            public TextView tv_student_name;
            public TextView tv_attendance_result;

        }


    }
}
