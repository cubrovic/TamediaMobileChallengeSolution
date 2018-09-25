package com.tamedia.cubrovic.tamediachallenge.data;

import com.tamedia.cubrovic.tamediachallenge.com.tamedia.cubrovic.tamediachallenge.events.EventLoadedPosts;
import com.tamedia.cubrovic.tamediachallenge.com.tamedia.cubrovic.tamediachallenge.events.EventLoadedUsers;
import com.tamedia.cubrovic.tamediachallenge.com.tamedia.cubrovic.tamediachallenge.events.EventNetworkError;
import com.tamedia.cubrovic.tamediachallenge.com.tamedia.cubrovic.tamediachallenge.events.EventSendNewPost;
import com.tamedia.cubrovic.tamediachallenge.domain.Post;
import com.tamedia.cubrovic.tamediachallenge.domain.User;
import com.tamedia.cubrovic.tamediachallenge.util.ConstData;

import java.util.List;
/**
 * ControlNetwork class encapsulates network logic in it. Using events via EventBus to notify UI about loaded data or errors.
 */
public class ControlNetwork {

    public static void loadPosts(String email, final List<User> usersToLoad, final DataHelper dataHelper, final EventLoadedUsers eventLoadedUsers, final EventNetworkError eventNetworkError) {
        com.androidnetworking.AndroidNetworking.get(ConstData.getUsersApiPath()).addQueryParameter(ConstData.getUsersApiEmailParamName(), email).setPriority(com.androidnetworking.common.Priority.HIGH).build().getAsObjectList(User.class, new com.androidnetworking.interfaces.ParsedRequestListener<List<User>>() {
            @Override
            public void onResponse(List<User> users) {
                usersToLoad.addAll(users);
                dataHelper.postEvent(eventLoadedUsers);
            }

            @Override
            public void onError(com.androidnetworking.error.ANError anError) {
                dataHelper.postEvent(eventNetworkError);
            }
        });
    }

    public static void loadPosts(String userid, final List<Post> listPosts, final DataHelper dataHelper, final EventLoadedPosts eventLoadedPosts, final EventNetworkError eventNetworkError) {
        com.androidnetworking.AndroidNetworking.get(ConstData.getPostsApiPath()).addQueryParameter(ConstData.getPostsApiUseridParamName(), userid).setPriority(com.androidnetworking.common.Priority.HIGH).build().getAsObjectList(Post.class, new com.androidnetworking.interfaces.ParsedRequestListener<List<Post>>() {
            @Override
            public void onResponse(final List<Post> posts) {
                listPosts.addAll(posts);
                dataHelper.postEvent(eventLoadedPosts);
            }

            @Override
            public void onError(com.androidnetworking.error.ANError anError) {
                dataHelper.postEvent(eventNetworkError);
            }
        });
    }

    public static void sendNewPost(final Post postToAdd, final Post postNew, final DataHelper dataHelper, final EventSendNewPost eventSendNewPost, final EventNetworkError eventNetworkError) {
        com.androidnetworking.AndroidNetworking.post(ConstData.getPostsApiPath()).addBodyParameter(postToAdd).setTag("test").setPriority(com.androidnetworking.common.Priority.HIGH).build().getAsObject(Post.class, new com.androidnetworking.interfaces.ParsedRequestListener<Post>() {
            @Override
            public void onResponse(final Post post) {
                postNew.setId(post.getId());
                postNew.setBody(post.getBody());
                postNew.setTitle(post.getTitle());
                postNew.setUserId(post.getUserId());

                dataHelper.postEvent(eventSendNewPost);
            }

            @Override
            public void onError(com.androidnetworking.error.ANError anError) {
                dataHelper.postEvent(eventNetworkError);
            }
        });
    }
}
