package com.sanshy.buysellinventory;

/**
 * Created by sbswami on 3/3/2018.
 */

public class sitem {

    String name;
    String company;
    String phone;
    String city;
    String address;
    String nameCity;
    String companyCity;


    public sitem()
    {

    }

    public sitem(String name, String company, String phone, String city, String address, String nameCity, String companyCity) {
        this.name = name;
        this.company = company;
        this.phone = phone;
        this.city = city;
        this.address = address;
        this.nameCity = nameCity;
        this.companyCity = companyCity;
    }

    public String getNameCity() {
        return nameCity;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
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
}
