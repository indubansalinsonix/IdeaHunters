package com.ideahunters.presenter;

import android.graphics.Bitmap;

/**
 * Created by root on 22/2/17.
 */

public class PostSubmitData {

    private static PostSubmitData userInstance;
    String category_name;
    String subcategory_name;
    String idea_submit;
    String idea_title;
    String key_result;
    Bitmap thumbnail;
    boolean UPDATE_IDEA;
    String sug_id;

    private PostSubmitData(){}
    public static PostSubmitData getInstance()
    {
        if (userInstance == null)
        {
            userInstance = new PostSubmitData();
        }
        return userInstance;
    }


    public PostSubmitData(String category_name, String subcategory_name, String idea_submit, String idea_title, String key_result, Bitmap thumbnail, boolean UPDATE_IDEA, String sug_id) {
        this.category_name = category_name;
        this.subcategory_name = subcategory_name;
        this.idea_submit = idea_submit;
        this.idea_title = idea_title;
        this.key_result = key_result;
        this.thumbnail = thumbnail;
        this.UPDATE_IDEA = UPDATE_IDEA;
        this.sug_id = sug_id;
    }

    public String getSubcategory_name() {
        return subcategory_name;
    }

    public void setSubcategory_name(String subcategory_name) {
        this.subcategory_name = subcategory_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getIdea_submit() {
        return idea_submit;
    }

    public void setIdea_submit(String idea_submit) {
        this.idea_submit = idea_submit;
    }

    public String getIdea_title() {
        return idea_title;
    }

    public void setIdea_title(String idea_title) {
        this.idea_title = idea_title;
    }

    public String getKey_result() {
        return key_result;
    }

    public void setKey_result(String key_result) {
        this.key_result = key_result;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public boolean isUPDATE_IDEA() {
        return UPDATE_IDEA;
    }

    public void setUPDATE_IDEA(boolean UPDATE_IDEA) {
        this.UPDATE_IDEA = UPDATE_IDEA;
    }

    public String getSug_id() {
        return sug_id;
    }

    public void setSug_id(String sug_id) {
        this.sug_id = sug_id;
    }

    @Override
    public String toString() {
        return "PostSubmitData{" +
                "category_name='" + category_name + '\'' +
                ", subcategory_name='" + subcategory_name + '\'' +
                ", idea_submit='" + idea_submit + '\'' +
                ", idea_title='" + idea_title + '\'' +
                ", key_result='" + key_result + '\'' +
                ", thumbnail=" + thumbnail +
                ", UPDATE_IDEA=" + UPDATE_IDEA +
                ", sug_id='" + sug_id + '\'' +
                '}';
    }
}
