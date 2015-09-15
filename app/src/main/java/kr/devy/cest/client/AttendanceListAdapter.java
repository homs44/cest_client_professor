package kr.devy.cest.client;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import kr.devy.cest.R;
import kr.devy.cest.db.ClassInfo;

import static android.view.View.*;

/**
 * Created by pc on 2015-08-10.
 */
public class AttendanceListAdapter extends RecyclerView.Adapter<AttendanceListAdapter.ViewHolder>{
    private JsonArray array;
    private Context context;
    private OnItemClickListener oicl;


    public AttendanceListAdapter(Context context, JsonArray array) {
        this.context = context;
        this.array = array;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance, parent, false), context);
        return vh;
    }

    @Override
    public int getItemCount() {
        return array.size();
    }


    public JsonObject getItem(int position) {
        return array.get(position).getAsJsonObject();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Log.d("gtd", "here --- " + position);
        JsonObject json = array.get(position).getAsJsonObject();
        String datetime = json.get("attendance_time").getAsString().replace("T"," ").replace(".000Z", "");
        Log.d(C.TAG,datetime);
        String[] temp = datetime.split("\\s");
        holder.tv_day.setText(temp[0]);
        holder.tv_time.setText(temp[1]);


    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements OnClickListener{
        public TextView tv_day;
        public TextView tv_time;
        public CardView cv;
        private Context context;

        public ViewHolder(View view, Context context) {
            super(view);
            this.context = context;
            tv_day = (TextView) view.findViewById(R.id.item_attendance_tv_day);
            tv_time = (TextView) view.findViewById(R.id.item_attendance_tv_time);
            cv = (CardView)view.findViewById(R.id.item_attendance_cv);
            cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            if(oicl!=null){
                oicl.onItemClick(view,getAdapterPosition());
            }
        }


    }



    public interface OnItemClickListener{
        public void onItemClick(View view , int position);
    }

    public void setOnItemClickListener(final OnItemClickListener oicl ){
        this.oicl =oicl;
    }
}
