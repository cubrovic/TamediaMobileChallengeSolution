package com.tamedia.cubrovic.tamediachallenge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.tamedia.cubrovic.tamediachallenge.com.tamedia.cubrovic.tamediachallenge.events.EventLoadedPosts;
import com.tamedia.cubrovic.tamediachallenge.com.tamedia.cubrovic.tamediachallenge.events.EventLoadedUsers;
import com.tamedia.cubrovic.tamediachallenge.com.tamedia.cubrovic.tamediachallenge.events.EventNetworkError;
import com.tamedia.cubrovic.tamediachallenge.com.tamedia.cubrovic.tamediachallenge.events.EventSendNewPost;
import com.tamedia.cubrovic.tamediachallenge.data.DataHelper;
import com.tamedia.cubrovic.tamediachallenge.data.DataHelperImpl;
import com.tamedia.cubrovic.tamediachallenge.domain.Post;
import com.tamedia.cubrovic.tamediachallenge.posts.PostPresenter;
import com.tamedia.cubrovic.tamediachallenge.posts.PostPresenterImpl;
import com.tamedia.cubrovic.tamediachallenge.posts.PostView;
import com.tamedia.cubrovic.tamediachallenge.util.ControlBundle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements PostView {

    private PostPresenter presenter;

    @BindView(R.id.editTextMail)
    EditText editTextMail;

    @BindView(R.id.editTextNewPostTitle)
    EditText editTextNewPostTitle;

    @BindView(R.id.editTextNewPostText)
    EditText editTextNewPostText;

    @BindView(R.id.listviewPosts)
    ListView listview;


    @BindView(R.id.linearLayoutNewPost)
    android.widget.LinearLayout linearLayoutNewPost;


    @BindString(R.string.error_networking)
    String errorNetworkingMessage;


    @OnClick(R.id.buttonGo)
    void go() {
        presenter.loadUserForEmail();
    }

    @OnClick(R.id.buttonAdd)
    void addNew() {
        presenter.addNewPost();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ControlBundle.writeToBundle(outState, presenter.getState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final String data = ControlBundle.getFromBundle(savedInstanceState);
        presenter.loadState(data);
        presenter.sendPendingPosts();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        android.content.SharedPreferences sharedPref = getPreferences(android.content.Context.MODE_PRIVATE);
        DataHelper dataHelper = new DataHelperImpl(sharedPref);


        presenter = new PostPresenterImpl();
        presenter.bind(this, dataHelper);
        presenter.loadData();
        presenter.sendPendingPosts();

    }


    @Override
    public void setLastEmail(String lastEmail) {
        editTextMail.setText(lastEmail);
    }

    @Override
    public void clearNewPostTitle() {
        editTextNewPostTitle.setText("");
    }

    @Override
    public void clearNewPostBody() {
        editTextNewPostText.setText("");
    }

    @Override
    public void showPosts(List<Post> listPosts) {
        final ListAdapterPosts adapter = new ListAdapterPosts(this, listPosts);
        listview.setAdapter(adapter);
    }

    @Override
    public void showNewPost() {
        linearLayoutNewPost.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNewPost() {
        linearLayoutNewPost.setVisibility(View.GONE);
    }

    @Override
    public String getEmail() {
        final String email = editTextMail.getText().toString().trim();
        return email;
    }

    @Override
    public String getNewPostTitle() {
        final String title = editTextNewPostTitle.getText().toString().trim();
        return title;
    }

    @Override
    public String getNewPostBody() {
        final String body = editTextNewPostText.getText().toString().trim();
        return body;
    }

    @Override
    public void showOKMessageNewPost() {
        String okMessage = getString(R.string.ok_message_new_post);
        showMessage(okMessage);
    }

    @Override
    public void showErrorNetworking() {
        String errorMessage = getString(R.string.error_networking);
        showMessage(errorMessage);
    }

    @Override
    public void showErrorWrongEmailInput() {
        String errorMessage = getString(R.string.error_wrong_email_input);
        showMessage(errorMessage);
    }

    @Override
    public void showErrorTitleNotPopulated() {
        String errorMessage = getString(R.string.error_wrong_new_post_title);
        showMessage(errorMessage);
    }

    @Override
    public void showErrorBodyNotPopulated() {
        String errorMessage = getString(R.string.error_wrong_new_post_text);
        showMessage(errorMessage);
    }

    @Override
    public void showErrorNoUsersForThisEmail() {
        String errorMessage = getString(R.string.error_no_user_for_this_email);
        showMessage(errorMessage);
    }

    @Override
    public void showErrorNoPostsForThisUser() {
        String errorMessage = getString(R.string.error_no_posts_for_this_user);
        showMessage(errorMessage);
    }

    private void showMessage(String errorMessage) {
        android.widget.Toast.makeText(this, errorMessage, android.widget.Toast.LENGTH_LONG).show();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventLoadedUsers event) {
        presenter.processLoadedUsersForEmail();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventLoadedPosts event) {
        presenter.processLoadedPostsForUser();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventSendNewPost event) {
        presenter.newPostSend();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventNetworkError event) {
        showErrorNetworking();
    }


}
