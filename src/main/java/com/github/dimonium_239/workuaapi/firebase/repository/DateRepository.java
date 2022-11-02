package com.github.dimonium_239.workuaapi.firebase.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ExecutionException;

@Service
public class DateRepository {
    private static final Logger logger = LoggerFactory.getLogger(DateRepository.class);

    public Date save(Date date) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> apiFuture = db.collection("Post").document("Post").update("lastPostDate", date);
        WriteResult writeResult = apiFuture.get();
        logger.info("Successfully saved, updated time: {}", writeResult.getUpdateTime());
        return date;
    }

    public Timestamp getLastDate() throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<DocumentSnapshot> apiFuture = db.collection("Post").document("Post").get();

        DocumentSnapshot document = apiFuture.get();
        if (document.exists()) {
            logger.info("DB founded: {}", "Post");
            return (Timestamp) document.get("lastPostDate");
        }

        logger.info("Post not found: {}", "Post");
        return null;
    }
}