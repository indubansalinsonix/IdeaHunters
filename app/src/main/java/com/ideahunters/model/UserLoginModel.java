
package com.ideahunters.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLoginModel {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("userdata")
    @Expose
    private List<UserData> userdata = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<UserData> getUserdata() {
        return userdata;
    }

    public void setUserdata(List<UserData> userdata) {
        this.userdata = userdata;
    }

}
