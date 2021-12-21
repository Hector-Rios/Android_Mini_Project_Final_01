package com.hrios_practice.android_mini_project_final_01;

public class User {
    public String id;
    public String name;
    public String username;
    public String email;
    public Address address;

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

    public Address getAddress()
    { return address; }

    // Class Creation Address Here.
    public class Address
    {
        String street;
        String suite;

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

        String city;
        String zipcode;
        String phone;

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
    }
}