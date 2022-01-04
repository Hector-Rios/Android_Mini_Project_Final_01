package com.hrios_practice.android_mini_project_final_01;

import android.content.Intent;

public class User {
    public String id = "";
    public String name = "";
    public String username = "";
    public String email = "";
    public Address address;

    public User(String gName, String gName2, String gID, String gEmail)
    {
        name = gName; username = gName2; id = gID; email = gEmail;
        address = new Address();
    }

    public User() {
        address = new Address();
    }

    public String getName() {
        return name;
    }

    public String getId()
    {   return id;   }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", address=" + address +
                '}';
    }

    public Address getAddress()
    { return address; }

    public void updateAddress(String street, String suite, String city, String zipcode)
    {
        address.setStreet(street); address.setSuite(suite);
        address.setCity(city);     address.setZipcode(zipcode);
    }

    public void updatePersonal(String iName, String iUserName, String iEmail, String i_id)
    {
        name = iName;   username = iUserName;
        email = iEmail; id = i_id;
    }

    // Class Creation Address Here.
    public class Address
    {
        String street = "";
        String suite  = "";
        String city   = "";
        String zipcode= "";
        String phone  = "";

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        // Setters - Getters.
        public String getStreet()
        {   return street;   }

        public void setStreet(String street)
        {   this.street = street;   }

        public String getSuite() {
            return suite;
        }

        public void setSuite(String suite) {
            this.suite = suite;
        }

        @Override
        public String toString() {
            return "Address{" +
                    "street='" + street + '\'' +
                    ", suite='" + suite + '\'' +
                    ", city='" + city + '\'' +
                    ", zipcode='" + zipcode + '\'' +
                    ", phone='" + phone + '\'' +
                    '}';
        }
    }
}