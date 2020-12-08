package com.daleel.ghazeihdaleel;

public class Contact {
    String Location, description, facebook, name, phone, pic, website, category;
    int likes;



    public Contact() {}

    public Contact(String location, String description, String facebook, String name, String phone, String pic, String website, String category) {
        Location = location;
        this.description = description;
        this.facebook = facebook;
        this.name = name;
        this.phone = phone;
        this.pic = pic;
        this.website = website;
        this.category = category;

    }

    public Contact(String location, String description, String facebook, String name, String phone, String pic, String website) {
        Location = location;
        this.description = description;
        this.facebook = facebook;
        this.name = name;
        this.phone = phone;
        this.pic = pic;
        this.website = website;
        //likes = 100 ; // sorted inversly (0 first 100 last)
    }

    public String getLocation() {
        return Location;
    }


    public String getDescription() {
        return description;
    }


    public String getFacebook() {
        return facebook;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }


    public String getPic() {
        return pic;
    }


    public String getWebsite() {
        return website;
    }



}
