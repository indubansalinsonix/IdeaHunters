
package com.ideahunters.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IdeaslistData implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("idea_title")
    @Expose
    private String ideaTitle;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("posted_by")
    @Expose
    private String postedBy;
    @SerializedName("company_id")
    @Expose
    private String companyId;
    @SerializedName("explain_idea")
    @Expose
    private String explainIdea;
    @SerializedName("key_result")
    @Expose
    private String keyResult;
    @SerializedName("submitted_date")
    @Expose
    private String submittedDate;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("subcat_id")
    @Expose
    private String subcatId;
    @SerializedName("comment")
    @Expose
    private List<Object> comment = null;
    @SerializedName("likes")
    @Expose
    private String likes;
    @SerializedName("likesCount")
    @Expose
    private String likesCount;
    @SerializedName("report")
    @Expose
    private String report;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdeaTitle() {
        return ideaTitle;
    }

    public void setIdeaTitle(String ideaTitle) {
        this.ideaTitle = ideaTitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getExplainIdea() {
        return explainIdea;
    }

    public void setExplainIdea(String explainIdea) {
        this.explainIdea = explainIdea;
    }

    public String getKeyResult() {
        return keyResult;
    }

    public void setKeyResult(String keyResult) {
        this.keyResult = keyResult;
    }

    public String getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(String submittedDate) {
        this.submittedDate = submittedDate;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getSubcatId() {
        return subcatId;
    }

    public void setSubcatId(String subcatId) {
        this.subcatId = subcatId;
    }

    public List<Object> getComment() {
        return comment;
    }

    public void setComment(List<Object> comment) {
        this.comment = comment;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(String likesCount) {
        this.likesCount = likesCount;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

}