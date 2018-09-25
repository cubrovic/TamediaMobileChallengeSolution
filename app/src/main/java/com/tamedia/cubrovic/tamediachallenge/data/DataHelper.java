package com.tamedia.cubrovic.tamediachallenge.data;

import com.tamedia.cubrovic.tamediachallenge.com.tamedia.cubrovic.tamediachallenge.events.EventLoadedPosts;
import com.tamedia.cubrovic.tamediachallenge.com.tamedia.cubrovic.tamediachallenge.events.EventLoadedUsers;
import com.tamedia.cubrovic.tamediachallenge.com.tamedia.cubrovic.tamediachallenge.events.EventNetworkError;
import com.tamedia.cubrovic.tamediachallenge.com.tamedia.cubrovic.tamediachallenge.events.EventSendNewPost;
import com.tamedia.cubrovic.tamediachallenge.domain.Post;
import com.tamedia.cubrovic.tamediachallenge.domain.PostsData;
import com.tamedia.cubrovic.tamediachallenge.domain.User;

import java.util.List;

/**
 * DataHelper interface that define contract for dealing with data from Model class
 */
public interface DataHelper {

    PostsData loadPostsData();

    PostsData loadPostsData(String jsonPost);

    String exportPostData(PostsData postsData);

    void saveData(final PostsData postsData);

    String loadLastEmail();

    void postEvent(Object event);

    void saveLastEmail(String lastEmail);

    void loadPosts(String email, List<User> usersToLoad, EventLoadedUsers eventLoadedUsers, EventNetworkError eventNetworkError);

    void loadPosts(String userid, List<Post> listPosts, EventLoadedPosts eventLoadedPosts, EventNetworkError eventNetworkError);

    void sendNewPost(Post postToAdd, Post postNew,
                     EventSendNewPost eventSendNewPost, EventNetworkError eventNetworkError);
}
