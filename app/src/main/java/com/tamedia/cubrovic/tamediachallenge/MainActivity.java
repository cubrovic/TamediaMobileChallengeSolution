package com.tamedia.cubrovic.tamediachallenge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tamedia.cubrovic.tamediachallenge.business.Post;
import com.tamedia.cubrovic.tamediachallenge.business.PostsData;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ControlBusinessLogic controlBusinessLogic = new ControlBusinessLogic();
    private PostsData postsData = new com.tamedia.cubrovic.tamediachallenge.business.PostsData();

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        final com.google.gson.Gson gson = ControlBusinessLogic.createGson();
        final String jsonPosts = gson.toJson(postsData);
        outState.putString(ConstBusinessLogic.getSavedDataKey(), jsonPosts);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final String jsonPost = savedInstanceState.getString(ConstBusinessLogic.getSavedDataKey());
        final com.google.gson.Gson gson = ControlBusinessLogic.createGson();
        com.tamedia.cubrovic.tamediachallenge.business.PostsData postsDataParam = gson.fromJson(jsonPost, com.tamedia.cubrovic.tamediachallenge.business.PostsData.class);
        if (postsDataParam != null) {
            android.widget.Toast.makeText(MainActivity.this, "log restored post data: " + postsDataParam.getListPosts().size(), android.widget.Toast.LENGTH_LONG).show();

            postsData = postsDataParam;
            List<Post> listPosts = postsData.getListPosts();
            final android.widget.ListView listview = findViewById(R.id.listviewPosts);
            final ListAdapterPosts adapter = new ListAdapterPosts(this, listPosts);
            listview.setAdapter(adapter);

            if (listPosts.size() > 0) {
                android.widget.LinearLayout linearLayoutNewPost = findViewById(R.id.linearLayoutNewPost);
                linearLayoutNewPost.setVisibility(android.view.View.VISIBLE);
            }

            sendPendingPosts();

        }
    }

    public void sendPendingPosts() {
        List<Post> listPendingPosts = postsData.getListPendingPosts();
        for (Post post : listPendingPosts) {
            controlBusinessLogic.sendNewPost(this, postsData, post);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final android.widget.EditText editTextMail = findViewById(R.id.editTextMail);
        final android.widget.Button buttonGo = findViewById(R.id.buttonGo);
        final android.widget.Button buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                controlBusinessLogic.sendNewPost(MainActivity.this, postsData);
            }
        });

        android.content.SharedPreferences sharedPref = getPreferences(android.content.Context.MODE_PRIVATE);
        String lastEmail = sharedPref.getString(ConstBusinessLogic.getSharedPrefLastEmailKey(), "");
        editTextMail.setText(lastEmail);
        buttonGo.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                controlBusinessLogic.loadUserForEmail(MainActivity.this, postsData);
            }
        });


        String jsonPost = sharedPref.getString(ConstBusinessLogic.getSharedPrefLastDataKey(), "");

        final com.google.gson.Gson gson = ControlBusinessLogic.createGson();
        com.tamedia.cubrovic.tamediachallenge.business.PostsData postsDataParam = gson.fromJson(jsonPost, com.tamedia.cubrovic.tamediachallenge.business.PostsData.class);
        if (postsDataParam != null) {
            android.widget.Toast.makeText(MainActivity.this, "log restored post data: " + postsDataParam.getListPosts().size(), android.widget.Toast.LENGTH_LONG).show();

            postsData = postsDataParam;
            List<Post> listPosts = postsData.getListPosts();
            final android.widget.ListView listview = findViewById(R.id.listviewPosts);
            final ListAdapterPosts adapter = new ListAdapterPosts(this, listPosts);
            listview.setAdapter(adapter);

            if (listPosts.size() > 0) {
                android.widget.LinearLayout linearLayoutNewPost = findViewById(R.id.linearLayoutNewPost);
                linearLayoutNewPost.setVisibility(android.view.View.VISIBLE);
            }

            sendPendingPosts();

        }


        sendPendingPosts();

    }


}
