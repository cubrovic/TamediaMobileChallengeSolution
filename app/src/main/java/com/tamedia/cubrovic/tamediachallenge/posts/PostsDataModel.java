package com.tamedia.cubrovic.tamediachallenge.posts;

import com.tamedia.cubrovic.tamediachallenge.com.tamedia.cubrovic.tamediachallenge.events.EventLoadedPosts;
import com.tamedia.cubrovic.tamediachallenge.com.tamedia.cubrovic.tamediachallenge.events.EventLoadedUsers;
import com.tamedia.cubrovic.tamediachallenge.com.tamedia.cubrovic.tamediachallenge.events.EventNetworkError;
import com.tamedia.cubrovic.tamediachallenge.com.tamedia.cubrovic.tamediachallenge.events.EventSendNewPost;
import com.tamedia.cubrovic.tamediachallenge.data.DataHelper;
import com.tamedia.cubrovic.tamediachallenge.domain.Post;
import com.tamedia.cubrovic.tamediachallenge.domain.PostsData;
import com.tamedia.cubrovic.tamediachallenge.domain.User;
import com.tamedia.cubrovic.tamediachallenge.util.ConstData;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Business logic class that holds all functionalities
 */
public class PostsDataModel {

    private PostsData postsData;

    private String lastEmail;

    private String lastEmailToLoad;

    private final DataHelper dataHelper;

    private List<User> users;

    public PostsDataModel(DataHelper dataHelper) {
        this.dataHelper = dataHelper;
        postsData = new PostsData();
    }


    public void setListPostsAfterLoad(java.util.List<Post> listPosts) {
        List<Post> posts = postsData.getListPosts();
        posts.clear();
        posts.addAll(listPosts);
    }

    public void clearListPosts() {
        List<Post> posts = postsData.getListPosts();
        posts.clear();
        List<Post> pendingPosts = postsData.getListPendingPosts();
        pendingPosts.clear();
    }

    public void clearSavedPosts() {
        List<Post> posts = postsData.getListPosts();
        posts.clear();
    }

    public void addNewPost(final Post post) {
        List<Post> posts = postsData.getListPosts();
        posts.add(0, post);
    }

    public void addPendingPost(final Post post) {
        List<Post> pendingPosts = postsData.getListPendingPosts();
        pendingPosts.add(0, post);
    }

    public void removePendingPost(final Post post) {
        List<Post> posts = postsData.getListPendingPosts();
        java.util.Iterator<Post> iterator = posts.iterator();
        while (iterator.hasNext()) {
            Post postCurrent = iterator.next();
            if (post.getTitle().equals(postCurrent.getTitle()) && post.getBody().equals(postCurrent.getBody())) {
                iterator.remove();
            }
        }
    }

    public PostsData getPostsData() {
        return postsData;
    }

    public void setPostsData(PostsData postsData) {
        this.postsData = postsData;
    }

    public void loadData() {
        lastEmail = dataHelper.loadLastEmail();
        PostsData postsDataFromDb = dataHelper.loadPostsData();
        if (postsDataFromDb != null) {
            postsData = postsDataFromDb;
            clearSavedPosts();
        }
    }

    public String getLastEmail() {
        return lastEmail;
    }

    public void setLastEmail(String lastEmail) {
        this.lastEmail = lastEmail;
    }

    public java.util.List<Post> getListPosts() {
        return postsData.getListPosts();
    }

    public String getState() {
        return dataHelper.exportPostData(postsData);
    }

    public void loadState(String data) {
        PostsData postsDataFromDb = dataHelper.loadPostsData(data);
        if (postsDataFromDb != null) {
            postsData = postsDataFromDb;
        }
    }

    public List<Post> getListPendingPosts() {
        List<Post> listPendingPosts = postsData.getListPendingPosts();
        return listPendingPosts;
    }

    public void loadUsers(String email) {
        setLastEmailToLoad(email);
        List<User> usersToLoad = new ArrayList<>();
        this.setUsers(usersToLoad);
        dataHelper.loadPosts(email, usersToLoad, new EventLoadedUsers(), new EventNetworkError());
    }

    public void loadPostsForUser() {
        final User user = users.get(0);
        loadPostsForUser(user);
    }

    private void loadPostsForUser(User user) {
        clearListPosts();
        List<Post> listPosts = postsData.getListPosts();
        final String userid = user.getId();
        dataHelper.loadPosts(userid, listPosts, new EventLoadedPosts(), new EventNetworkError());
    }

    private void setUsers(List<User> users) {
        this.users = users;
    }

    public String getLastEmailToLoad() {
        return lastEmailToLoad;
    }

    public void setLastEmailToLoad(String lastEmailToLoad) {
        this.lastEmailToLoad = lastEmailToLoad;
    }

    public boolean haveLoadedUsersForEmail() {
        return users.size() > 0;
    }

    public boolean haveLoadedPostsForUser() {
        final List<Post> listPosts = postsData.getListPosts();
        return listPosts.size() > 0;
    }

    public void saveLastEmail() {
        lastEmail = lastEmailToLoad;
        dataHelper.saveLastEmail(lastEmail);
    }

    public void saveData() {
        dataHelper.saveData(postsData);
    }

    public boolean notPopulated(String value) {
        return value.trim().length() == 0;
    }

    public void sendNewPost(String title, String body) {
        String userId = postsData.getUserid();
        final Post postToAdd = new Post();
        postToAdd.setTitle(title);
        postToAdd.setBody(body);
        postToAdd.setUserId(userId);
        addPendingPost(postToAdd);

        sendNewPost(postToAdd);
    }

    public void sendNewPost(Post postToAdd) {
        final Post postNew = new Post();
        postsData.setLastNewPost(postNew);
        dataHelper.sendNewPost(postToAdd, postNew, new EventSendNewPost(), new EventNetworkError());
    }

    public void newPostSend() {
        Post lastNewPost = postsData.getLastNewPost();
        if (lastNewPost != null) {
            removePendingPost(lastNewPost);
            addNewPost(lastNewPost);
        }
    }


    public boolean isValidEmail(CharSequence email) {
        if (email == null) {
            return false;
        }
        if (email.length() == 0) {
            return false;
        }
        Pattern emailAddress = ConstData.EMAIL_ADDRESS;
        return emailAddress.matcher(email).matches();
    }

    public void sendPendingPosts() {
        List<Post> listPendingPosts = getListPendingPosts();
        for (Post post : listPendingPosts) {
            sendNewPost(post);
        }
    }
}
