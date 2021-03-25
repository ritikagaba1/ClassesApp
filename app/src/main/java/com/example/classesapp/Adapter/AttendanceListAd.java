package com.example.classesapp.Adapter;

public class AttendanceListAd {
    String batch_name;
    String batch_time;
    String present;
    String absent;
    String total_leaves;

    public String getBatch_name() {
        return batch_name;
    }

    public void setBatch_name(String batch_name) {
        this.batch_name = batch_name;
    }

    public String getBatch_time() {
        return batch_time;
    }

    public void setBatch_time(String batch_time) {
        this.batch_time = batch_time;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public String getAbsent() {
        return absent;
    }

    public void setAbsent(String absent) {
        this.absent = absent;
    }

    public String getTotal_leaves() {
        return total_leaves;
    }

    public void setTotal_leaves(String total_leaves) {
        this.total_leaves = total_leaves;
    }

    public AttendanceListAd(String batch_name, String batch_time, String present, String absent, String total_leaves) {

        this.batch_name = batch_name;
        this.batch_time=batch_time;
        this.present=present;
        this.absent=absent;
        this.total_leaves=total_leaves;

    }

}
