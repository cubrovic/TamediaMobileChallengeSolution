package com.tamedia.cubrovic.tamediachallenge.domain;

public class PostsData {
    private java.util.List<Post> listPosts;

    private java.util.List<Post> listPendingPosts;

    private Post lastNewPost;

    private String userid;

    public PostsData() {
        listPosts = new java.util.ArrayList<>();
        listPendingPosts = new java.util.ArrayList<>();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public java.util.List<Post> getListPosts() {
        return listPosts;
    }

    public java.util.List<Post> getListPendingPosts() {
        return listPendingPosts;
    }

    public Post getLastNewPost() {
        return lastNewPost;
    }

    public void setLastNewPost(Post lastNewPost) {
        this.lastNewPost = lastNewPost;
    }
}
