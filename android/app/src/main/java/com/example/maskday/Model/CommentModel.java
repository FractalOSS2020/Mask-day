package com.example.maskday.Model;

public class CommentModel {
    public String commentContent;
    public String commentUser;
    public String commentTimeStamp;

    public String getCommentTimeStamp() {
        return commentTimeStamp;
    }

    public void setCommentTimeStamp(String commentTimeStamp) {
        this.commentTimeStamp = commentTimeStamp;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(String commentUser) {
        this.commentUser = commentUser;
    }
}
