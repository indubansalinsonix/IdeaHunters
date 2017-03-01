package com.ideahunters.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Likes {

@SerializedName("status")
@Expose
private String status;
@SerializedName("message")
@Expose
private String message;
@SerializedName("likecount")
@Expose
private String likecount;
    @SerializedName("likebyme")
    @Expose
    private String  likebyme;


public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public String getLikecount() {
return likecount;
}

public void setLikecount(String likecount) {
this.likecount = likecount;
}


    public String getLikebyme() {
        return likebyme;
    }

    public void setLikebyme(String likebyme) {
        this.likebyme = likebyme;
    }
}