package com.example.classesapp.Adapter;

import androidx.appcompat.app.AppCompatActivity;

public class AttendanceMarkAd  {
    String student_id,student_name,batch_id,date;

    public String getBatch_id() {
        return batch_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public AttendanceMarkAd(String StudentID, String StudentName,String batchid,String date){
        this.student_id=StudentID;
        this.student_name=StudentName;
        this.batch_id=batchid;
        this.date=date;
    }
}
