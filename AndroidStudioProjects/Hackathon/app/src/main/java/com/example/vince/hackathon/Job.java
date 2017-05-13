package com.example.vince.hackathon;

import com.orm.SugarRecord;

/**
 * Created by vince on 1/26/17.
 */
public class Job extends SugarRecord {
    private String title;
    private String description;
    private String addedDate;
    private String deadlineDate;
    private String status;
    public Job() {

    }

    public Job(String title,String description,String addedDate,String deadlineDate,String status) {
        this.title = title;
        this.description = description;
        this.addedDate = addedDate;
        this.deadlineDate = deadlineDate;
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public String getAddedDate() {
        return this.addedDate;
    }

    public void setDeadlineDate(String deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public String getDeadlineDate() {
        return this.deadlineDate;
    }
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

