package com.example.toolsapplication;

public class ToolClass {

    String id, tooname, description, userid, username, useremail;

    boolean available;

    public ToolClass(String id, String tooname, String description, String userid, String username, String useremail, boolean available) {
        this.id = id;
        this.tooname = tooname;
        this.description = description;
        this.userid = userid;
        this.username = username;
        this.useremail = useremail;
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public String getTooname() {
        return tooname;
    }

    public String getDescription() {
        return description;
    }

    public String getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    public String getUseremail() {
        return useremail;
    }

    public boolean getAvailable() {
        return available;
    }
}
