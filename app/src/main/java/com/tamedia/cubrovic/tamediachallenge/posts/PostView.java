package com.tamedia.cubrovic.tamediachallenge.posts;

import com.tamedia.cubrovic.tamediachallenge.domain.Post;

import java.util.List;
/**
 * View interface that define contract for interacting with Presenter
 */
public interface PostView {
    void setLastEmail(String lastEmail);

    void showPosts(List<Post> listPosts);

    void showNewPost();

    void hideNewPost();

    String getEmail();

    String getNewPostTitle();

    String getNewPostBody();

    void showErrorNetworking();

    void showErrorWrongEmailInput();

    void showErrorNoUsersForThisEmail();

    void showErrorNoPostsForThisUser();

    void showErrorTitleNotPopulated();

    void showErrorBodyNotPopulated();

    void clearNewPostTitle();

    void clearNewPostBody();

    void showOKMessageNewPost();
}
