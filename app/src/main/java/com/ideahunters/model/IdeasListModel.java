
package com.ideahunters.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IdeasListModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<IdeaslistData> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<IdeaslistData> getData() {
        return data;
    }

    public void setData(List<IdeaslistData> data) {
        this.data = data;
    }

}
