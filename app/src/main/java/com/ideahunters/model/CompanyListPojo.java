
package com.ideahunters.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompanyListPojo {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<CompanyData> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CompanyData> getData() {
        return data;
    }

    public void setData(List<CompanyData> data) {
        this.data = data;
    }

}
