package com.example.classesapp.Adapter;

public class StudentListAd {
String student_name,student_birth,student_gender,student_batch;


public StudentListAd(String student_name,String student_batch,String student_gender ,String student_birth){
    this.student_batch=student_batch;
    this.student_birth=student_birth;
    this.student_gender=student_gender;
    this.student_name=student_name;

}

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_birth() {
        return student_birth;
    }

    public void setStudent_birth(String student_birth) {
        this.student_birth = student_birth;
    }

    public String getStudent_gender() {
        return student_gender;
    }

    public void setStudent_gender(String student_gender) {
        this.student_gender = student_gender;
    }

    public String getStudent_batch() {
        return student_batch;
    }

    public void setStudent_batch(String student_batch) {
        this.student_batch = student_batch;
    }
}
