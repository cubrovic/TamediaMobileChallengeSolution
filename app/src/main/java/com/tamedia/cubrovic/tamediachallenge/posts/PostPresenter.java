package com.tamedia.cubrovic.tamediachallenge.posts;

import com.tamedia.cubrovic.tamediachallenge.data.DataHelper;
/**
 * Presenter interface that define contract for interacting with View
 */
public interface PostPresenter {
    void bind(PostView view, DataHelper dataHelper);

    void loadData();

    String getState();

    void loadState(String data);

    void sendPendingPosts();

    void loadUserForEmail();

    void addNewPost();

    boolean isValidEmail(String email);

    void processLoadedUsersForEmail();

    void processLoadedPostsForUser();

    void newPostSend();
}
