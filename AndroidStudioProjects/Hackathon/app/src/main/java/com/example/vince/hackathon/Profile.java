package com.example.vince.hackathon;

import com.orm.SugarRecord;

import java.util.ArrayList;

public class Profile extends SugarRecord{

    private String phone;
    private String name;
    private String email;
    private String role;
    private ArrayList<Job> jobs;

    public Profile() {

    }
    public Profile(String phone,String name,String email,String role,ArrayList<Job> jobs) {
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.role = role;
        this.jobs = jobs;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setRole(String email) {
        this.role = role;
    }

    public String getRole() {
        return this.role;
    }

    public ArrayList<Job> getJobList() {
        return this.jobs;
    }

    public void addJob(Job job) {
        this.jobs.add(job);
    }
}
