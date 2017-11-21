package com.example.sharangirdhani.homework07;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sharangirdhani on 11/20/17.
 */

public class Post implements Serializable{
    private String postId;
    private String username;
    private String created;
    private String postData;

    public Post() {

    }

    public Post(String postId, String username, String created, String postData) {
        this.postId = postId;
        this.username = username;
        this.created = created;
        this.postData = postData;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getPostData() {
        return postData;
    }

    public void setPostData(String postData) {
        this.postData = postData;
    }

    public String getPrettyTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date(0);
        try {
            date = dateFormat.parse(created);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        PrettyTime p = new PrettyTime();
        return p.format(new Date(date.getTime() - (5*60*60*1000)));
    }
}
