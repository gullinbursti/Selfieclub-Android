package com.builtinmenlo.selfieclub.dataSources;

import java.util.Date;

/**
 * Created by Leonardo on 6/2/14.
 */
public class ActivityItem {
    private String activityItemId;
    private int activityType;
    private User user;
    private Date time;
    private String message;
    private int type;

    public String getActivityItemId() {
        return activityItemId;
    }

    public void setActivityItemId(String activityItemId) {
        this.activityItemId = activityItemId;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
