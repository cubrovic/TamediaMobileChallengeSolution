package com.tamedia.cubrovic.tamediachallenge.data;

import android.content.SharedPreferences;

import com.tamedia.cubrovic.tamediachallenge.com.tamedia.cubrovic.tamediachallenge.events.EventLoadedPosts;
import com.tamedia.cubrovic.tamediachallenge.com.tamedia.cubrovic.tamediachallenge.events.EventLoadedUsers;
import com.tamedia.cubrovic.tamediachallenge.com.tamedia.cubrovic.tamediachallenge.events.EventNetworkError;
import com.tamedia.cubrovic.tamediachallenge.com.tamedia.cubrovic.tamediachallenge.events.EventSendNewPost;
import com.tamedia.cubrovic.tamediachallenge.domain.Post;
import com.tamedia.cubrovic.tamediachallenge.domain.PostsData;
import com.tamedia.cubrovic.tamediachallenge.domain.User;
import com.tamedia.cubrovic.tamediachallenge.util.ConstData;
import com.tamedia.cubrovic.tamediachallenge.util.ControlConverter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * DataHelperImpl class define Android related implementation of dealing with data
 */
public class DataHelperImpl implements DataHelper {

    private SharedPreferences sharedPreferences;

    public DataHelperImpl(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void saveData(final PostsData postsData) {
        String jsonPosts = exportPostData(postsData);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ConstData.getSharedPrefLastDataKey(), jsonPosts);
        editor.commit();
    }

    @Override
    public String exportPostData(PostsData postsData) {
        return ControlConverter.convertPostsDataToJson(postsData);
    }

    @Override
    public PostsData loadPostsData() {
        String jsonPost = sharedPreferences.getString(ConstData.getSharedPrefLastDataKey(), "");
        return loadPostsData(jsonPost);
    }

    @Override
    public PostsData loadPostsData(String jsonPost) {
        PostsData postsData = ControlConverter.convertJsonToPostsData(jsonPost);
        return postsData;
    }

    @Override
    public String loadLastEmail() {
        String lastEmail = sharedPreferences.getString(ConstData.getSharedPrefLastEmailKey(), "");
        return lastEmail;
    }

    @Override
    public void saveLastEmail(String email) {
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ConstData.getSharedPrefLastEmailKey(), email);
        editor.commit();
    }

    @Override
    public void postEvent(Object event) {
        EventBus.getDefault().post(event);
    }

    @Override
    public void loadPosts(String email, List<User> usersToLoad, EventLoadedUsers eventLoadedUsers, EventNetworkError eventNetworkError) {
        ControlNetwork.loadPosts(email, usersToLoad, this, new EventLoadedUsers(), new EventNetworkError());
    }

    @Override
    public void loadPosts(String userid, List<Post> listPosts, EventLoadedPosts eventLoadedPosts, EventNetworkError eventNetworkError) {
        ControlNetwork.loadPosts(userid, listPosts, this, new EventLoadedPosts(), new EventNetworkError());
    }

    @Override
    public void sendNewPost(Post postToAdd, Post postNew, EventSendNewPost eventSendNewPost, EventNetworkError eventNetworkError) {
        ControlNetwork.sendNewPost(postToAdd, postNew, this, new EventSendNewPost(), new EventNetworkError());
    }
}
