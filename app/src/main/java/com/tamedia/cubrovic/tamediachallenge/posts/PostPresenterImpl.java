package com.tamedia.cubrovic.tamediachallenge.posts;

import com.tamedia.cubrovic.tamediachallenge.data.DataHelper;
import com.tamedia.cubrovic.tamediachallenge.domain.Post;

import java.util.List;
/**
 * Presenter class that connects Business model and View
 */
public class PostPresenterImpl implements PostPresenter {
    private PostView view;

    private PostsDataModel postsDataModel;



    @Override
    public void bind(PostView view, DataHelper dataHelper) {
        this.view = view;
        this.postsDataModel = new PostsDataModel(dataHelper);
    }

    @Override
    public void loadData() {
        postsDataModel.loadData();
        String lastEmail = postsDataModel.getLastEmail();
        view.setLastEmail(lastEmail);
        showPosts();
    }

    @Override
    public String getState() {
        return postsDataModel.getState();
    }

    @Override
    public void loadState(String data) {
        postsDataModel.loadState(data);
        showPosts();
    }

    private void showPosts() {
        List<Post> listPosts = postsDataModel.getListPosts();
        view.showPosts(listPosts);
        if (listPosts.size() > 0) {
            view.showNewPost();
        }
    }

    @Override
    public void sendPendingPosts() {
        postsDataModel.sendPendingPosts();
    }


    @Override
    public void loadUserForEmail() {
        final String email = view.getEmail();
        if (isValidEmail(email)) {
            postsDataModel.loadUsers(email);
        } else {
            view.showErrorWrongEmailInput();
        }
    }

    @Override
    public void processLoadedUsersForEmail() {
        postsDataModel.clearListPosts();
        if (postsDataModel.haveLoadedUsersForEmail()) {
            view.showNewPost();
            //this is a valid user so we saved email address
            postsDataModel.saveLastEmail();
            postsDataModel.loadPostsForUser();


        } else {
            view.hideNewPost();
            view.showErrorNoUsersForThisEmail();
        }
    }

    @Override
    public void processLoadedPostsForUser() {
        if (!postsDataModel.haveLoadedPostsForUser()) {
            view.showErrorNoPostsForThisUser();
        }
        showPosts();
        postsDataModel.saveData();
    }

    @Override
    public boolean isValidEmail(String email) {
        return postsDataModel.isValidEmail(email);
    }

    @Override
    public void addNewPost() {
        final String title = view.getNewPostTitle();
        if (postsDataModel.notPopulated(title)) {
            view.showErrorTitleNotPopulated();
            return;
        }
        final String body = view.getNewPostBody();
        if (postsDataModel.notPopulated(body)) {
            view.showErrorBodyNotPopulated();
            return;
        }
        postsDataModel.saveData();

        postsDataModel.sendNewPost(title, body);
    }

    @Override
    public void newPostSend() {

        view.clearNewPostTitle();
        view.clearNewPostBody();
        view.showOKMessageNewPost();
        postsDataModel.newPostSend();

        postsDataModel.saveData();

        showPosts();

    }
}

