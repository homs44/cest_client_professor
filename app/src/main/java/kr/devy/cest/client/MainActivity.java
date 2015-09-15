package kr.devy.cest.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import kr.devy.cest.R;
import kr.devy.cest.db.ClassInfo;
import kr.devy.cest.db.DayOfTheWeek;
import kr.devy.cest.http.APIManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends Activity {

    ExpandableListView el;
    BaseExpandableAdapter adapter;
    HashMap<Integer, ArrayList<ClassInfo>> classList;
    String semester;
    TextView tv_semester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        semester = "2015-S";
        tv_semester = (TextView)findViewById(R.id.main_semester);
        tv_semester.setText(semester);
        el = (ExpandableListView) findViewById(R.id.main_el);
        classList = new HashMap<Integer, ArrayList<ClassInfo>>();
        adapter = new BaseExpandableAdapter(getApplicationContext(), classList);
        el.setAdapter(adapter);
        el.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
                ClassInfo info = adapter.getChild(groupPosition,childPosition);
                Intent i = new Intent(MainActivity.this,MenuActivity.class);
                i.putExtra("classinfo",info);
                startActivity(i);

                return true;
            }
        });
        getMyClass();
    }


    private void getMyClass() {
        //수정



        SharedPreferences pref = getSharedPreferences(C.PREF_NAME,MODE_PRIVATE);
        int c_user_id = pref.getInt(C.PREF_C_USER_ID,-1);

        APIManager.getInstance().getAPI().getMyClass(c_user_id,semester, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                JsonObject json = jsonElement.getAsJsonObject();
                JsonArray array = json.get("myclasses").getAsJsonArray();
                adapter.updateChild(array);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("cest", error.toString());
            }
        });


    }


    public class BaseExpandableAdapter extends BaseExpandableListAdapter {


        private ArrayList<DayOfTheWeek> groupList = null;
        private ViewDay viewDay = null;
        private HashMap<Integer, ArrayList<ClassInfo>> childList = null;

        private LayoutInflater inflater = null;
        private ViewClass viewClass = null;


        public BaseExpandableAdapter(Context c, HashMap<Integer, ArrayList<ClassInfo>> childList) {
            super();
            this.inflater = LayoutInflater.from(c);
            this.groupList = new ArrayList<DayOfTheWeek>();
            init(this.groupList);
            this.childList = childList;
        }

        public void updateChild(JsonArray array) {
            if (childList == null) {
                childList = new HashMap<Integer, ArrayList<ClassInfo>>();
            } else {
                childList.clear();
            }

            for (int i = 0; i < array.size(); i++) {
                JsonObject jo = array.get(i).getAsJsonObject();
                ClassInfo ci = new ClassInfo();
                ci.setC_class_id(jo.get("c_class_id").getAsInt());
                ci.setName(jo.get("name").getAsString());
                ci.setCode(jo.get("code").getAsString());
                ci.setRoom(jo.get("room").getAsString());
                ci.setProfessor(jo.get("c_user_id").getAsInt());
                ci.setSemester(jo.get("semester").getAsString());
                ci.setDay_of_the_week(jo.get("day_of_the_week").getAsInt());
                ci.setStart_hour(jo.get("start_hour").getAsInt());
                ci.setEnd_hour(jo.get("end_hour").getAsInt());
                ci.setTime(jo.get("time").getAsString());
                ci.setMac_addresses(jo.get("mac_addresses").getAsString());

                if (childList.get(ci.getDay_of_the_week()) != null) {
                    childList.get(ci.getDay_of_the_week()).add(ci);

                } else {
                    ArrayList<ClassInfo> temp = new ArrayList<ClassInfo>();
                    temp.add(ci);
                    childList.put(ci.getDay_of_the_week(), temp);
                }
            }

            for (DayOfTheWeek temp : groupList) {
                if (childList.get(temp.getNum()) != null) {
                    Collections.sort(childList.get(temp.getNum()), new TimeCompare());
                }
            }


            this.notifyDataSetChanged();
        }


        private void init(ArrayList<DayOfTheWeek> group) {
            group.add(new DayOfTheWeek(1, "월", "#424242"));
            group.add(new DayOfTheWeek(2, "화", "#424242"));
            group.add(new DayOfTheWeek(3, "수", "#424242"));
            group.add(new DayOfTheWeek(4, "목", "#424242"));
            group.add(new DayOfTheWeek(5, "금", "#424242"));
            group.add(new DayOfTheWeek(6, "토", "#2979FF"));
            group.add(new DayOfTheWeek(7, "일", "#C62828"));

        }

        // 그룹 포지션을 반환한다.
        @Override
        public DayOfTheWeek getGroup(int groupPosition) {
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


        class ViewDay {
            public TextView tv_day;
        }


        // 그룹뷰 각각의 ROW
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                v = inflater.inflate(R.layout.item_day, parent, false);
                viewDay = new ViewDay();
                viewDay.tv_day = (TextView) v.findViewById(R.id.item_day);

                v.setTag(viewDay);
            } else {
                viewDay = (ViewDay) v.getTag();
            }

            // 그룹을 펼칠때와 닫을때 아이콘을 변경해 준다.
            DayOfTheWeek vo = getGroup(groupPosition);
            if(viewDay==null){
                Log.d(C.TAG,"시팔 널이네");
            }
            if(vo==null){

                Log.d(C.TAG,"시팔 널이네sdfdf");
            }
            viewDay.tv_day.setText(vo.getName());
            viewDay.tv_day.setTextColor(Color.parseColor(vo.getColor()));
            ((ExpandableListView) parent).expandGroup(groupPosition);
            return v;
        }

        // 차일드뷰를 반환한다.
        @Override
        public ClassInfo getChild(int groupPosition, int childPosition) {
            if (childList.get(groupList.get(groupPosition).getNum()) == null) {
                return null;
            } else {
                return childList.get(groupList.get(groupPosition).getNum()).get(childPosition);
            }


        }

        // 차일드뷰 사이즈를 반환한다.
        @Override
        public int getChildrenCount(int groupPosition) {
            if (childList.get(groupList.get(groupPosition).getNum()) == null) {
                return 0;
            } else {
                return childList.get(groupList.get(groupPosition).getNum()).size();
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
                v = inflater.inflate(R.layout.item_class, null);
                viewClass.tv_name = (TextView) v.findViewById(R.id.class_tv_name);
                viewClass.tv_room = (TextView) v.findViewById(R.id.class_tv_room);
                viewClass.tv_time = (TextView) v.findViewById(R.id.class_tv_time);
                v.setTag(viewClass);
            } else {
                viewClass = (ViewClass) v.getTag();
            }

            viewClass.tv_name.setText(getChild(groupPosition, childPosition).getName());
            viewClass.tv_room.setText(getChild(groupPosition, childPosition).getRoom());
            viewClass.tv_time.setText(getChild(groupPosition, childPosition).getTime());
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
            public TextView tv_name;
            public TextView tv_room;
            public TextView tv_time;

        }


    }

    private class TimeCompare implements Comparator<ClassInfo> {
        @Override
        public int compare(ClassInfo arg0, ClassInfo arg1) {
            return arg0.getStart_hour() < arg1.getStart_hour() ? -1 : arg0.getStart_hour() > arg1.getStart_hour() ? 1 : 0;
        }


    }
}
