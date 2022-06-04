package com.SH.asset_calculator.repository;

import com.SH.asset_calculator.domain.Member;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Slf4j
@Repository
public class InfoRepository {
    private static final String FIELDNAME = "price_latest";
    private static final String CONFIGNAME = "configuration";

    private Firestore FIREBASE;

    private void getFirebase(){
        FIREBASE = FirestoreClient.getFirestore();
    }

    public void setConfig(Member member, HashMap param){
        getFirebase();

        FIREBASE.collection(CONFIGNAME).document(member.getUid())
                .set(param);

    }

    public DocumentSnapshot getConfig(Member member){
        getFirebase();


        DocumentSnapshot documentSnapshot = null;
        try {
            documentSnapshot = FIREBASE.collection(CONFIGNAME).document(member.getUid())
                    .get().get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        return documentSnapshot;
    }


    // todo uid를 받지말고 Member로 전부 바꾸자
    public boolean insertField(String uid, HashMap<String, String> manual_field, HashMap<String, ArrayList<String>> custom_field){
        getFirebase();
        HashMap<String, HashMap> param = new HashMap<>();
        param.put("manual_field", manual_field);
        param.put("custom_field", custom_field);

        try {

            FIREBASE.collection(FIELDNAME).document(uid)
                    .set(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }

        log.info("Successfully Insert To Firebase 'UID:"+uid+"'");

        return true;
    }

    public DocumentSnapshot getField(Member member){
        getFirebase();

        DocumentSnapshot documentSnapshot1 = null;

        try {
            documentSnapshot1 = FIREBASE.collection(FIELDNAME).document(member.getUid())
                    .get().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return documentSnapshot1;
    }
}
