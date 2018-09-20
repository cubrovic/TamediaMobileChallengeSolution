package com.tamedia.cubrovic.tamediachallenge.business;

public class PostsData {
    private java.util.List<Post> listPosts;
    private java.util.List<Post> listPendingPosts;

    private String userid;

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public java.util.List<Post> getListPosts() {
        return listPosts;
    }

    public java.util.List<Post> getListPendingPosts() {
        return listPendingPosts;
    }

    public void setListPostsAfterLoad(java.util.List<Post> listPosts) {
        this.listPosts.clear();
        this.listPosts.addAll(listPosts);
    }

    public PostsData() {
        listPosts = new java.util.ArrayList();
        listPendingPosts = new java.util.ArrayList();
    }

    public void clearListPosts() {
        listPosts.clear();
        listPendingPosts.clear();
    }

    public void addNewPost(final Post post) {
        listPosts.add(0, post);
    }

    public void addPendingPost(final Post post) {
        listPendingPosts.add(0, post);
    }

    public void removePendingPost(final Post post) {
        java.util.Iterator<Post> iterator = listPosts.iterator();
        while (iterator.hasNext()) {
            Post postCurrent = iterator.next();
            if (post.getTitle().equals(postCurrent.getTitle()) && post.getBody().equals(postCurrent.getBody())) {
                iterator.remove();
            }
        }
    }

}
