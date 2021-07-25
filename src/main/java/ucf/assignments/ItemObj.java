package ucf.assignments;
/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 John Kelleher
 */

import java.text.NumberFormat;

public class ItemObj {
    private String price;
    private String serial;
    private String name;
    NumberFormat nf = NumberFormat.getCurrencyInstance();
    public ItemObj(String ItemPrice,String SN, String ItemName){
        this.price = "$"+ItemPrice;
        this.serial = SN;
        this.name = ItemName;
    }
    public String getPrice(){return price;}
    public String getSerial(){return serial;}
    public String getName(){return name;}
    public void setName(String nn){
        this.name = nn;
    }
    public void setValue(String price){
        this.price = price;
    }
    public void setSerial(String seriall){
        this.serial = seriall;
    }
}
