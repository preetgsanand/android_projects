package com.example.vince.shivaexpo;

import com.orm.SugarRecord;

public class Item extends SugarRecord{
    private String code;
    private String item_table;
    private String size;
    private String mp_cbm;
    private String ip_mp;
    private String fob;
    private String quantity;
    private String rate;
    public Item() {

    }
    public Item( String code,String item_table,String size,String mp_cbm
                ,String ip_mp,String fob,String quantity,String rate) {
        this.code = code;
        this.item_table = item_table;
        this.size = size;
        this.mp_cbm = mp_cbm;
        this.ip_mp = ip_mp;
        this.fob = fob;
        this.quantity = quantity;
        this.rate = rate;
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getItem_table() {
        return this.item_table;
    }
    public void setItem_table(String table) {
        this.item_table = table;
    }
    public String getSize() {
        return this.size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public String getMp_cbm() {
        return this.mp_cbm;
    }
    public void setMp_cbm(String mp_cbm) {
        this.mp_cbm = mp_cbm;
    }
    public String getIp_mp() {
        return this.ip_mp;
    }
    public void setIp_mp(String ip_mp) {
        this.ip_mp = ip_mp;
    }
    public String getFob() {
        return this.fob;
    }
    public void setFob(String fob) {
        this.fob = fob;
    }
    public String getQuantity() {
        return this.quantity;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    public String getRate() {
        return this.rate;
    }
    public void setRate(String rate) {
        this.rate = rate;
    }
}
