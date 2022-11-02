package com.github.dimonium_239.workuaapi.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dimonium_239.workuaapi.controller.FBPostController;
import com.github.dimonium_239.workuaapi.firebase.dto.Post;
import com.github.dimonium_239.workuaapi.firebase.dto.Posts;
import com.github.dimonium_239.workuaapi.firebase.repository.DateRepository;
import com.github.dimonium_239.workuaapi.utils.HTTPUtils;
import com.google.cloud.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class PostCollectingService {

    @Value("${fb.accessToken}")
    private String fbAccessToken;
    private String fbURL = "https://graph.facebook.com/v15.0/105485158961068/published_posts?access_token=";
    private final Logger logger = LoggerFactory.getLogger(PostCollectingService.class);

    private List<Post> posts;

    final DateRepository dateRepository;

    public PostCollectingService(DateRepository dateRepository) {
        this.dateRepository = dateRepository;
    }

    public LinkedList<Post> getStackOfNewPosts() throws Exception {
        LinkedList<Post> stackOfPosts = new LinkedList<>();
        Date lastDate = dateRepository.getLastDate().toDate();
        Posts posts = getFirstPostPage();
        Iterator<Post> it = posts.iterator();
        Post lastPost = it.next();

        while (it.hasNext()) {
            if(lastPost.getCreated_time().after(lastDate)){
                stackOfPosts.add(lastPost);
            }
            lastPost = it.next();
        }
        lastPost = stackOfPosts.peek();
        if(lastPost != null) lastDate = lastPost.getCreated_time();

        if(lastPost != null && (!it.hasNext() || lastDate.before(lastPost.getCreated_time()))) {
            dateRepository.save(lastPost.getCreated_time());
        }
        return stackOfPosts;
    }

    public void fromFbToViber() throws Exception {
        LinkedList<Post> stackOfPosts = getStackOfNewPosts();
        while (!stackOfPosts.isEmpty()) {
            Post topPost = stackOfPosts.pop();
            //TODO: Add messaging to viber
            logger.info(topPost.toString());
        }
    }

    public Posts getFirstPostPage() throws IOException {
        String response = getPostsFromFB();
        logger.debug(response);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        return objectMapper.readValue(response, Posts.class);
    }

    public String getPostsFromFB() throws IOException {
        return getPostsFromFB(fbURL + fbAccessToken);
    }

    public String getPostsFromFB(String url) throws IOException {
        return HTTPUtils.sendGET(url);
    }
}
