package com.sanshy.buysellinventory;

/**
 * Created by sbswami on 3/3/2018.
 */

public class citem {


    String name;
    String phone;
    String city;
    String address;
    String namePhone;
    String nameCity;


    public citem()
    {

    }

    public String getNameCity() {
        return nameCity;
    }

    public citem(String name, String phone, String city, String address, String namePhone, String nameCity) {
        this.name = name;

        this.phone = phone;
        this.city = city;
        this.address = address;
        this.namePhone = namePhone;
        this.nameCity = nameCity;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getNamePhone(){
        return namePhone;
    }
}
