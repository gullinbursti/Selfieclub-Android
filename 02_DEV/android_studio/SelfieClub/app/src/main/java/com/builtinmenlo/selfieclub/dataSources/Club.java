package com.builtinmenlo.selfieclub.dataSources;

import org.json.JSONArray;

/**
 * Created by Leonardo on 6/11/14.
 */
public class Club {
    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public ClubType getClubType() {
        return clubType;
    }

    public void setClubType(String strClubType) {
        if(strClubType.equalsIgnoreCase("USER_GENERATED"))this.clubType=ClubType.USER_GENERATED;
        else if (strClubType.equalsIgnoreCase("FEATURED"))this.clubType=ClubType.FEATURED;
        else if (strClubType.equalsIgnoreCase("SCHOOL")) this.clubType=ClubType.SCHOOL;
        else if (strClubType.equalsIgnoreCase("NEARBY")) this.clubType=ClubType.NEARBY;
        else if (strClubType.equalsIgnoreCase("SUGGESTED")) this.clubType=ClubType.SUGGESTED;
        else if (strClubType.equalsIgnoreCase("STAFF")) this.clubType=ClubType.STAFF;
        else if (strClubType.equalsIgnoreCase("SPONSORED")) this.clubType=ClubType.SPONSORED;
        else this.clubType=ClubType.UNKNOW;

    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubDescription() {
        return clubDescription;
    }

    public void setClubDescription(String clubDescription) {
        this.clubDescription = clubDescription;
    }

    public String getClubImage() {
        return clubImage;
    }

    public void setClubImage(String clubImage) {
        this.clubImage = clubImage;
    }

    public String getClubTotalMembers() {
        return clubTotalMembers;
    }

    public void setClubTotalMembers(String clubTotalMembers) {
        this.clubTotalMembers = clubTotalMembers;
    }

    public String getAdded() {
        return added;
    }

    public void setAdded(String added) {
        this.added = added;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public User getClubOwner() {
        return clubOwner;
    }

    public void setClubOwner(User clubOwner) {
        this.clubOwner = clubOwner;
    }

    public JSONArray getClubMembers() {
        return clubMembers;
    }

    public void setClubMembers(JSONArray clubMembers) {
        this.clubMembers = clubMembers;
    }

    public JSONArray getClubPendingMembers() {
        return clubPendingMembers;
    }

    public void setClubPendingMembers(JSONArray clubPendingMembers) {
        this.clubPendingMembers = clubPendingMembers;
    }

    public JSONArray getClubBlockedMembers() {
        return clubBlockedMembers;
    }

    public void setClubBlockedMembers(JSONArray clubBlockedMembers) {
        this.clubBlockedMembers = clubBlockedMembers;
    }

    public JSONArray getClubSubmissions() {
        return clubSubmissions;
    }

    public void setClubSubmissions(JSONArray clubSubmissions) {
        this.clubSubmissions = clubSubmissions;
    }

    public enum ClubType{
        USER_GENERATED,
        FEATURED,
        SCHOOL,
        NEARBY,
        SUGGESTED,
        STAFF,
        SPONSORED,
        UNKNOW
    }


    private String clubId;
    private ClubType clubType;
    private String clubName;
    private String clubDescription;
    private String clubImage;
    private String clubTotalMembers;
    private String added;
    private String updated;
    private User clubOwner;
    private JSONArray clubMembers;
    private JSONArray clubPendingMembers;
    private JSONArray clubBlockedMembers;
    private JSONArray clubSubmissions;
}
