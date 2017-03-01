
package com.ideahunters.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {


    @SerializedName("comment_id")
    @Expose
    private String commentId;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("submitted_by")
    @Expose
    private String submittedBy;
    @SerializedName("commented_by")
    @Expose
    private String commentedBy;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("report")
    @Expose
    private String report;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public String getCommentedBy() {
        return commentedBy;
    }

    public void setCommentedBy(String commentedBy) {
        this.commentedBy = commentedBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

}