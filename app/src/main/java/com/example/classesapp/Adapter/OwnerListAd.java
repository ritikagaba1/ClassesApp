package com.example.classesapp.Adapter;

public class OwnerListAd {

    String batch_name;
    String batch_status;


    public String getBatch_status() {
        return batch_status;
    }

    public void setBatch_status(String batch_status) {
        this.batch_status = batch_status;
    }

    public String getBatch_name() {
        return batch_name;
    }

    public void setBatch_name(String batch_name) {
        this.batch_name = batch_name;
    }

    public OwnerListAd(String batch_name,String batch_status) {

        this.batch_name = batch_name;
        this.batch_status=batch_status;


    }

}
