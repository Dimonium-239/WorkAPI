package com.github.dimonium_239.workuaapi.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dimonium_239.workuaapi.firebase.dto.Posts;
import com.github.dimonium_239.workuaapi.firebase.repository.DateRepository;
import com.github.dimonium_239.workuaapi.services.PostCollectingService;
import com.github.dimonium_239.workuaapi.utils.HTTPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

@RestController
public class FBPostController {

    private final Logger logger = LoggerFactory.getLogger(FBPostController.class);

    final DateRepository dateRepository;
    final PostCollectingService postCollectingService;

    public FBPostController(DateRepository dateRepository, PostCollectingService postCollectingService) {
        this.dateRepository = dateRepository;
        this.postCollectingService = postCollectingService;
    }

    @GetMapping(value="/fbPosts", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getPosts() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        return objectMapper.convertValue(postCollectingService.getFirstPostPage(), String.class);
    }

    @GetMapping("/getLastSavedPostDate")
    public String getLastSavedPostDate() throws Exception {
        return dateRepository.getLastDate().toString();
    }

    @GetMapping("/transfer")
    public ResponseEntity<String> transfer(){
        try {
            postCollectingService.fromFbToViber();
            return new ResponseEntity<>("Transfer done", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Some exception", HttpStatus.EXPECTATION_FAILED);
        }
    }

}
