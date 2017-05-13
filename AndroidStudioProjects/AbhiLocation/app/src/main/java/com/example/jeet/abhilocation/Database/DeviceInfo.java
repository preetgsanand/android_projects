package com.example.jeet.abhilocation.Database;

import com.orm.SugarRecord;

/**
 * Created by jeet on 28/10/16.
 */
public class DeviceInfo extends SugarRecord{
    private String number;
    public DeviceInfo() {

    }
    public DeviceInfo(String number) {
        this.number = number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getNumber() {
        return this.number;
    }
}
