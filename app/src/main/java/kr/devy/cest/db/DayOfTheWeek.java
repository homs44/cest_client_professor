package kr.devy.cest.db;

/**
 * Created by pc on 2015-07-31.
 */
public class DayOfTheWeek {
    int num;
    String name;
    String color;

    public DayOfTheWeek(int num,String name,String color){
        this.num =num;
        this.name = name;
        this.color = color;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
