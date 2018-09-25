package com.tamedia.cubrovic.tamediachallenge.posts;

import com.tamedia.cubrovic.tamediachallenge.com.tamedia.cubrovic.tamediachallenge.events.EventNetworkError;
import com.tamedia.cubrovic.tamediachallenge.com.tamedia.cubrovic.tamediachallenge.events.EventSendNewPost;
import com.tamedia.cubrovic.tamediachallenge.data.DataHelper;
import com.tamedia.cubrovic.tamediachallenge.domain.Post;
import com.tamedia.cubrovic.tamediachallenge.domain.PostsData;
import com.tamedia.cubrovic.tamediachallenge.util.ControlConverter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PostDataModelTest {


    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    DataHelper mockDataHelper;

    PostsDataModel postsDataModel;

    @Before
    public void setUp() {
        postsDataModel = new PostsDataModel(mockDataHelper);
    }


    @Test
    public void isValidEmailTrue() {
        assertTrue(postsDataModel.isValidEmail("david@gmail.com"));
        assertTrue(postsDataModel.isValidEmail("dan.aykroyd@movie.com"));
        assertTrue(postsDataModel.isValidEmail("michael.jones@subdomain.domain.com"));
        assertTrue(postsDataModel.isValidEmail("support123@gmail.com"));
    }

    @Test
    public void isValidEmailFalse() {
        assertFalse(postsDataModel.isValidEmail("plaintext"));
        assertFalse(postsDataModel.isValidEmail("@domain.com"));
        assertFalse(postsDataModel.isValidEmail("David Peter <david@peter.com>"));
        assertFalse(postsDataModel.isValidEmail("sub.domain.com"));
        assertFalse(postsDataModel.isValidEmail("name@-domain.com"));
    }


    @Test
    public void isAddNewPost_ValidSize() {
        Post post1=new Post();
        post1.setUserId("1");
        post1.setTitle("title1");
        post1.setBody("body1");
        postsDataModel.addNewPost(post1);

        assertEquals(1,postsDataModel.getListPosts().size());


        Post post2=new Post();
        post2.setUserId("1");
        post2.setTitle("title2");
        post2.setBody("body2");
        postsDataModel.addNewPost(post2);

        assertEquals(2,postsDataModel.getListPosts().size());

    }


    @Test
    public void isClearListPosts_ValidSize() {
        Post post1=new Post();
        post1.setUserId("1");
        post1.setTitle("title1");
        post1.setBody("body1");
        postsDataModel.addNewPost(post1);

        Post post2=new Post();
        post2.setUserId("1");
        post2.setTitle("title2");
        post2.setBody("body2");
        postsDataModel.addNewPost(post2);

        postsDataModel.clearListPosts();
        assertEquals(0,postsDataModel.getListPosts().size());
    }

    @Test
    public void isAddPendingPost_ValidSize() {
        Post post1=new Post();
        post1.setUserId("1");
        post1.setTitle("title1");
        post1.setBody("body1");

        postsDataModel.addPendingPost(post1);
        assertEquals(1,postsDataModel.getListPendingPosts().size());
    }

    @Test
    public void isSendPendingPost_ValidSize() {
        Answer<Object> answer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation)  {
                Post postToAdd = invocation.getArgumentAt(0, Post.class);
                Post postNew = invocation.getArgumentAt(1, Post.class);
//                EventSendNewPost eventSendNewPost = invocation.getArgumentAt(2, EventSendNewPost.class);
//                EventNetworkError eventNetworkError = invocation.getArgumentAt(3, EventNetworkError.class);

                postNew.setId(postToAdd.getId());
                postNew.setBody(postToAdd.getBody());
                postNew.setTitle(postToAdd.getTitle());
                postNew.setUserId(postToAdd.getUserId());

                postsDataModel.newPostSend();

                assertEquals(1,postsDataModel.getListPosts().size());
                assertEquals(0,postsDataModel.getListPendingPosts().size());

                return null;
            }
        };

        doAnswer(answer).when(mockDataHelper).sendNewPost(any(Post.class), any(Post.class), any(EventSendNewPost.class), any(EventNetworkError.class));

        postsDataModel.sendNewPost("title1","body1");

    }

    @Test
    public void isGetState_ValidContent() {
        PostsData postsData1 = any(PostsData.class);
        Answer<String> answer = new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                PostsData postsData = invocation.getArgumentAt(0, PostsData.class);
                return ControlConverter.convertPostsDataToJson(postsData);
            }
        };

        when(mockDataHelper.exportPostData(postsData1)).thenAnswer(answer);

        Post post1=new Post();
        post1.setUserId("1");
        post1.setTitle("title1");
        post1.setBody("body1");

        Post post2=new Post();
        post2.setUserId("1");
        post2.setTitle("title2");
        post2.setBody("body2");

        PostsData postsData=new PostsData();
        postsData.getListPosts().add(post1);
        postsData.getListPosts().add(post2);

        postsDataModel.addNewPost(post2);
        postsDataModel.addNewPost(post1);

        String state = postsDataModel.getState();
        String expected = "{\"listPosts\":[{\"body\":\"body1\",\"title\":\"title1\",\"userId\":\"1\"},{\"body\":\"body2\",\"title\":\"title2\",\"userId\":\"1\"}],\"listPendingPosts\":[]}";
        assertEquals(expected,state);
    }


    @Test
    public void isLoadState_ValidPostsSize() {
        String data = any(String.class);
        Answer<PostsData> answer = new Answer<PostsData>() {
            public PostsData answer(InvocationOnMock invocation) throws Throwable {
                String jsonPost = invocation.getArgumentAt(0, String.class);
                PostsData postsData = ControlConverter.convertJsonToPostsData(jsonPost);
                return postsData;
            }
        };

        when(mockDataHelper.loadPostsData(data)).thenAnswer(answer);

        String state = "{\"listPosts\":[{\"body\":\"body1\",\"title\":\"title1\",\"userId\":\"1\"},{\"body\":\"body2\",\"title\":\"title2\",\"userId\":\"1\"}],\"listPendingPosts\":[]}";
        postsDataModel.loadState(state);
        List<Post> listPosts = postsDataModel.getListPosts();
        assertEquals(2,listPosts.size());
    }
}