package com.jnu.student.content;

import java.io.Serializable;
public class MyTask implements Serializable{
    private String TTime;
    private String TTitle;
    private int TPoint;
    private int TNum;
    private int TNumFinish;
    private String TType;
    private String TTag;
    private String TState;

    public MyTask(String Ttime, String Ttitle, int Tpoint, int Tnum, int Tnumfinish, String Ttype, String Ttag, String Tstate){
        this.TTime =Ttime;
        this.TTitle =Ttitle;
        this.TPoint =Tpoint;
        this.TNum =Tnum;
        this.TNumFinish =Tnumfinish;
        this.TType =Ttype;
        this.TTag =Ttag;
        this.TState =Tstate;
    }

    public String getTime() {
        return TTime;
    }

    public void setTime(String time) {
        this.TTime =time;
    }

    public String getTitle() {
        return TTitle;
    }

    public void setTitle(String title) {
        TTitle = title;
    }

    public int getPoint() {
        return TPoint;
    }

    public void setTPoint(int TPoint) {
        this.TPoint = TPoint;
    }

    public int getTNum() {
        return TNum;
    }

    public void setTNum(int TNum) {
        this.TNum = TNum;
    }

    public int getTNumFinish() {
        return TNumFinish;
    }

    public void setTNumFinish(int TNumFinish) {
        this.TNumFinish = TNumFinish;
    }

    public String getTType() {
        return TType;
    }

    public void setTType(String TType) {
        this.TType = TType;
    }

    public String getTTag() {
        return TTag;
    }

    public void setTTag(String TTag) {
        this.TTag = TTag;
    }

    public String getTState() {
        return TState;
    }

    public void setTState(String TState) {
        this.TState = TState;
    }

    public String SHowString(){
        return "MyTarget{"+"Time"+ TTime +" | "
                +"Title"+ TTitle +" | "
                +"Point"+ TPoint +" | "
                +"Num"+ TNum +" | "
                +"NumFinish"+ TNumFinish +" | "
                +"Tag"+ TTag +" | "
                +"State"+ TState +" } ";
    }
}
