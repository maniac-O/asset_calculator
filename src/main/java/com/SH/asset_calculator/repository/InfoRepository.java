package com.SH.asset_calculator.repository;

import com.SH.asset_calculator.domain.Member;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@Slf4j
@Repository
public class InfoRepository {
    private static final String COLLECTION_NAME = "price_latest";

    public boolean insertField(String uid, HashMap<String, String> manual_field, HashMap<String, ArrayList<String>> custom_field){
        try {
            Firestore firestore = FirestoreClient.getFirestore();

            firestore.collection(COLLECTION_NAME).document(uid)
                    .update("manual_field", manual_field);

            firestore.collection(COLLECTION_NAME).document(uid)
                    .update("custom_field", custom_field);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }

        log.info("Successfully Insert To Firebase 'UID:"+uid+"'");

        return true;
    }

    public DocumentSnapshot getField(Member member){
        Firestore firestore = FirestoreClient.getFirestore();

        DocumentSnapshot documentSnapshot1 = null;

        try {
            documentSnapshot1 = firestore.collection(COLLECTION_NAME).document(member.getUid())
                    .get().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return documentSnapshot1;
    }
}
