package kr.devy.cest.db;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pc on 2015-07-31.
 */
public class ClassInfo implements Parcelable {
//"c_class_id":2,
// "name":"Logic Circuit",
// "code":"EECS247003",
// "room":"IT-1 207ȣ",
// "c_user_id":2,
// "semester":"2015-S",
// "c_class_time_id":4,
// "start_hour":1,
// "end_hour":3,
// "day_of_the_week":4,
// "time":"9A9B10A"}

    int c_class_id;
    String name;
    String code;
    String room;
    int professor;
    String semester;
    String mac_addresses;
    int start_hour;
    int end_hour;
    int day_of_the_week;
    String time;
    public ClassInfo(){

    }
    public ClassInfo(Parcel in){
       c_class_id=in.readInt();
        name = in.readString();
        code = in.readString();
        room = in.readString();
        professor = in.readInt();
        semester = in.readString();
        start_hour = in.readInt();
        end_hour = in.readInt();
        day_of_the_week = in.readInt();
        time = in.readString();
        mac_addresses = in.readString();
    }

    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(c_class_id);
        dest.writeString(name);
        dest.writeString(code);
        dest.writeString(room);
        dest.writeInt(professor);
        dest.writeString(semester);
        dest.writeInt(start_hour);
        dest.writeInt(end_hour);
        dest.writeInt(day_of_the_week);
        dest.writeString(time);
        dest.writeString(mac_addresses);

    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ClassInfo createFromParcel(Parcel in) {
            return new ClassInfo(in);
        }

        public ClassInfo[] newArray(int size) {
            return new ClassInfo[size];
        }
    };


    public String getMac_addresses() {
        return mac_addresses;
    }

    public void setMac_addresses(String mac_addresses) {
        this.mac_addresses = mac_addresses;
    }

    public int getC_class_id() {
        return c_class_id;
    }

    public void setC_class_id(int c_class_id) {
        this.c_class_id = c_class_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDay_of_the_week() {

        return day_of_the_week;
    }

    public void setDay_of_the_week(int day_of_the_week) {
        this.day_of_the_week = day_of_the_week;
    }

    public int getEnd_hour() {

        return end_hour;
    }

    public void setEnd_hour(int end_hour) {
        this.end_hour = end_hour;
    }

    public int getStart_hour() {

        return start_hour;
    }

    public void setStart_hour(int start_hour) {
        this.start_hour = start_hour;
    }

    public String getSemester() {

        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public int getProfessor() {

        return professor;
    }

    public void setProfessor(int professor) {
        this.professor = professor;
    }

    public String getRoom() {

        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ClassInfo{" +
                "c_class_id=" + c_class_id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", room='" + room + '\'' +
                ", professor=" + professor +
                ", semester='" + semester + '\'' +
                ", start_hour=" + start_hour +
                ", end_hour=" + end_hour +
                ", day_of_the_week=" + day_of_the_week +
                ", time='" + time + '\'' +
                '}';
    }
}
