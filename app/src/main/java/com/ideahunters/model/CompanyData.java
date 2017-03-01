
package com.ideahunters.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompanyData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("code_id")
    @Expose
    private String codeId;
    @SerializedName("code_name")
    @Expose
    private String codeName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }
    @Override
    public String toString() {
        return company;
    }

}


