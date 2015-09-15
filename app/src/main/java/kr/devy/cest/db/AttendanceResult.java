package kr.devy.cest.db;

/**
 * Created by pc on 2015-08-10.
 */
public class AttendanceResult {
    int id;
    String uuid;
    int c_user_id;
    int result;
    String reply_time;
    String user_id;
    String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getC_user_id() {
        return c_user_id;
    }

    public void setC_user_id(int c_user_id) {
        this.c_user_id = c_user_id;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getReply_time() {
        return reply_time;
    }

    public void setReply_time(String reply_time) {
        this.reply_time = reply_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AttendanceResult{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", c_user_id=" + c_user_id +
                ", result=" + result +
                ", reply_time='" + reply_time + '\'' +
                ", user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
