package com.builtinmenlo.selfieclub.dataSources;

import org.json.JSONArray;

import java.io.Serializable;

/**
 * Created by Leonardo on 6/26/14.
 */
public class NewsItem implements Serializable {

    private String userName;
    private String clubName;
    private String timestamp;
    private String avatarUrl;
    private String imageUrl;
    private JSONArray status;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public JSONArray getStatus() {
        return status;
    }

    public void setStatus(JSONArray status) {
        this.status = status;
    }
}
