package com.github.dimonium_239.workuaapi.firebase.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dimonium_239.workuaapi.services.PostCollectingService;
import com.github.dimonium_239.workuaapi.utils.HTTPUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;


public class Posts implements Iterable<Post>{

    @Getter @Setter
    private List<Post> data;

    @Getter @Setter
    private Paging paging;


    @Override
    public Iterator<Post> iterator() {
        Iterator<Post> it = new Iterator<Post>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return (currentIndex < data.size() || paging.getNext() != null) && !data.isEmpty();
            }

            @SneakyThrows
            @Override
            public Post next() {
                if(hasNext()){
                    if(currentIndex < data.size()){
                        return data.get(currentIndex++);
                    } else {
                        currentIndex = 0;
                        String nextURL = paging.getNext();
                        String response = HTTPUtils.sendGET(nextURL);
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
                        Posts posts = objectMapper.readValue(response, Posts.class);
                        data = posts.getData();
                        paging = posts.getPaging();
                        return data.get(currentIndex++);
                    }
                }
                return null;
            }
        };
        return it;
    }
}
