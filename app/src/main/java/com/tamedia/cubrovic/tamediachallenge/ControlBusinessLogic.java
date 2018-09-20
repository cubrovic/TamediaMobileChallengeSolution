package com.tamedia.cubrovic.tamediachallenge;


import com.tamedia.cubrovic.tamediachallenge.business.Post;
import com.tamedia.cubrovic.tamediachallenge.business.PostsData;
import com.tamedia.cubrovic.tamediachallenge.business.User;

import java.util.List;


public class ControlBusinessLogic {
    public final static boolean isValidEmail(CharSequence email) {
        if (email == null) {
            return false;
        }
        if (email.length() == 0) {
            return false;
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void loadUserForEmail(final android.app.Activity activity, final PostsData postsData) {

        final android.widget.EditText editTextMail = activity.findViewById(R.id.editTextMail);
        final String email = editTextMail.getText().toString().trim();
        if (ControlBusinessLogic.isValidEmail(email)) {

            com.androidnetworking.AndroidNetworking.get(ConstBusinessLogic.getUsersApiPath()).addQueryParameter(ConstBusinessLogic.getUsersApiEmailParamName(), email).setPriority(com.androidnetworking.common.Priority.HIGH).build().getAsObjectList(User.class, new com.androidnetworking.interfaces.ParsedRequestListener<List<User>>() {
                @Override
                public void onResponse(List<User> users) {
                    handleUserForEmail(activity, postsData, users, email);
                }

                @Override
                public void onError(com.androidnetworking.error.ANError anError) {
                    String errorMessage = activity.getString(R.string.error_networking);
                    android.widget.Toast.makeText(activity, errorMessage, android.widget.Toast.LENGTH_LONG).show();
                }
            });

        } else {
            String errorMessage = activity.getString(R.string.error_wrong_email_input);
            android.widget.Toast.makeText(activity, errorMessage, android.widget.Toast.LENGTH_LONG).show();
        }
    }

    public void handleUserForEmail(final android.app.Activity activity, final PostsData postsData, final List<User> users, final String email) {
        android.widget.LinearLayout linearLayoutNewPost = activity.findViewById(R.id.linearLayoutNewPost);
        linearLayoutNewPost.setVisibility(android.view.View.GONE);
        postsData.clearListPosts();
        if (users.size() == 0) {
            String errorMessage = activity.getString(R.string.error_no_user_for_this_email);
            android.widget.Toast.makeText(activity, errorMessage, android.widget.Toast.LENGTH_LONG).show();
        } else {
            //this is a valid user so we saved email address
            android.content.SharedPreferences sharedPref = activity.getPreferences(android.content.Context.MODE_PRIVATE);
            android.content.SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(ConstBusinessLogic.getSharedPrefLastEmailKey(), email);
            editor.commit();

            linearLayoutNewPost.setVisibility(android.view.View.VISIBLE);


            final User user = users.get(0);
            loadPostsForUser(activity, postsData, user);

        }

    }


    public void loadPostsForUser(final android.app.Activity activity, final PostsData postsData, final User user) {
        final String userid = user.getId();
        com.androidnetworking.AndroidNetworking.get(ConstBusinessLogic.getPostsApiPath()).addQueryParameter(ConstBusinessLogic.getPostsApiUseridParamName(), userid).setPriority(com.androidnetworking.common.Priority.HIGH).build().getAsObjectList(Post.class, new com.androidnetworking.interfaces.ParsedRequestListener<List<Post>>() {
            @Override
            public void onResponse(final List<Post> posts) {
                handlePostsForUser(activity, postsData, posts, user);
            }

            @Override
            public void onError(com.androidnetworking.error.ANError anError) {
                String errorMessage = activity.getString(R.string.error_networking);
                android.widget.Toast.makeText(activity, errorMessage, android.widget.Toast.LENGTH_LONG).show();
            }
        });
    }

    public void handlePostsForUser(final android.app.Activity activity, final PostsData postsData, final List<Post> posts, final com.tamedia.cubrovic.tamediachallenge.business.User user) {
        if (posts.size() == 0) {
            String errorMessage = activity.getString(R.string.error_no_posts_for_this_user);
            android.widget.Toast.makeText(activity, errorMessage, android.widget.Toast.LENGTH_LONG).show();

        }

        postsData.setListPostsAfterLoad(posts);
        String userId = user.getId();
        postsData.setUserid(userId);
        final android.widget.ListView listview = activity.findViewById(R.id.listviewPosts);
        final ListAdapterPosts adapter = new ListAdapterPosts(activity, posts);
        listview.setAdapter(adapter);

        saveData(activity, postsData);
    }


    public void sendNewPost(final android.app.Activity activity, final PostsData postsData) {
        final android.widget.EditText editTextNewPostTitle = activity.findViewById(R.id.editTextNewPostTitle);
        final android.widget.EditText editTextNewPostText = activity.findViewById(R.id.editTextNewPostText);


        final String title = editTextNewPostTitle.getText().toString().trim();
        final String body = editTextNewPostText.getText().toString().trim();

        if (title.trim().length() == 0) {
            String errorMessage = activity.getString(R.string.error_wrong_new_post_input);
            android.widget.Toast.makeText(activity, errorMessage, android.widget.Toast.LENGTH_LONG).show();
            return;
        }
        if (body.trim().length() == 0) {
            String errorMessage = activity.getString(R.string.error_wrong_new_post_text);
            android.widget.Toast.makeText(activity, errorMessage, android.widget.Toast.LENGTH_LONG).show();
            return;
        }

        String userId = postsData.getUserid();

        final Post postToAdd = new Post();
        postToAdd.setTitle(title);
        postToAdd.setBody(body);
        postToAdd.setUserId(userId);

        postsData.addPendingPost(postToAdd);
        sendNewPost(activity, postsData, postToAdd);

        saveData(activity, postsData);


    }

    public static com.google.gson.Gson createGson() {
        final com.google.gson.GsonBuilder gsonBuilder = new com.google.gson.GsonBuilder();
        final com.google.gson.Gson gson = gsonBuilder.create();
        return gson;
    }

    public void saveData(final android.app.Activity activity, final PostsData postsData) {
        final com.google.gson.Gson gson = createGson();
        final String jsonPosts = gson.toJson(postsData);
        android.content.SharedPreferences sharedPref = activity.getPreferences(android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(ConstBusinessLogic.getSharedPrefLastDataKey(), jsonPosts);
        editor.commit();
    }

    public void sendNewPost(final android.app.Activity activity, final PostsData postsData, final Post postToAdd) {
        com.androidnetworking.AndroidNetworking.post(ConstBusinessLogic.getPostsApiPath()).addBodyParameter(postToAdd).setTag("test").setPriority(com.androidnetworking.common.Priority.HIGH).build().getAsObject(Post.class, new com.androidnetworking.interfaces.ParsedRequestListener<Post>() {
            @Override
            public void onResponse(final Post post) {
                String okMessage = activity.getString(R.string.ok_message_new_post);
                android.widget.Toast.makeText(activity, okMessage, android.widget.Toast.LENGTH_LONG).show();
                postsData.removePendingPost(postToAdd);
                postsData.addNewPost(post);

                java.util.List<Post> posts = postsData.getListPosts();
                final android.widget.ListView listview = activity.findViewById(R.id.listviewPosts);
                final ListAdapterPosts adapter = new ListAdapterPosts(activity, posts);
                listview.setAdapter(adapter);

                final android.widget.EditText editTextNewPostTitle = activity.findViewById(R.id.editTextNewPostTitle);
                final android.widget.EditText editTextNewPostText = activity.findViewById(R.id.editTextNewPostText);
                editTextNewPostTitle.setText("");
                editTextNewPostText.setText("");


                saveData(activity, postsData);

            }

            @Override
            public void onError(com.androidnetworking.error.ANError anError) {
                String errorMessage = activity.getString(R.string.error_networking);
                android.widget.Toast.makeText(activity, errorMessage, android.widget.Toast.LENGTH_LONG).show();

            }
        });
    }
}
