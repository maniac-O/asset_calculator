package com.SH.asset_calculator.repository;

import com.SH.asset_calculator.domain.Member;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.SH.asset_calculator.domain.FirebaseCollectionEnum.*;

@Slf4j
@Repository
public class InfoRepository {


    private Firestore FIREBASE;

    private void getFirebase(){
        FIREBASE = FirestoreClient.getFirestore();
    }

    public Boolean setScope(Member member, String stat){
        getFirebase();

        HashMap<String, Object> param = new HashMap<>();

        param.put("displayName", member.getDisplayName());
        param.put("updateTime", new Timestamp(System.currentTimeMillis()));


        WriteResult writeResult = null;
        if(stat.equals("true")) {
            try {
                param.put("index", FIREBASE.collection(FIELDNAME.label()).document(member.getUid()).get().get().get("index"));

                // field를 가져와서 public scope에 넣어줘야함
                writeResult = FIREBASE.collection(PUBLIC_SCOPE.label()).document(member.getUid()).set(getField(member)).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            } catch (ExecutionException e) {
                e.printStackTrace();
                return false;
            }

            if (writeResult.getUpdateTime() != null)
                FIREBASE.collection(PUBLIC_SCOPE.label()).document(member.getUid()).set(param, SetOptions.merge());
        }else if(stat.equals("false")){
            FIREBASE.collection(PUBLIC_SCOPE.label()).document(member.getUid()).delete();
        }


        log.info("successfully change scope stat 'UID: " + member.getUid() + "', 'changed stat: " + stat + "'");
        return true;
    }

    public void setConfig(Member member, HashMap param){
        getFirebase();

        FIREBASE.collection(CONFIGNAME.label()).document(member.getUid())
                .set(param);

    }

    public DocumentSnapshot getConfig(Member member){
        getFirebase();


        DocumentSnapshot documentSnapshot = null;
        try {
            documentSnapshot = FIREBASE.collection(CONFIGNAME.label()).document(member.getUid())
                    .get().get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        return documentSnapshot;
    }

    public boolean setField(String target, Member member, HashMap<String, HashMap<String, HashMap>> param, Integer index){
        getFirebase();
        try {

            WriteBatch batch = FIREBASE.batch();
            DocumentReference target_price_latest = FIREBASE.collection(target).document(member.getUid());
            DocumentReference target_price_history = FIREBASE.collection(PRICE_HISTORY.label()).document(member.getUid());

            // target : price_latest
            batch.set(target_price_latest, param);
            batch.update(target_price_latest, "index", index);

            // target : price_history
            HashMap<String, Object> history_param = new HashMap<>();
            history_param.put(index.toString(), param);

            batch.set(target_price_history, history_param, SetOptions.merge());
            batch.update(target_price_history, "index", index);

            batch.commit();

        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }

        log.info("Successfully Insert To Firebase 'UID:"+member.getUid()+"'");

        return true;
    }

    public DocumentSnapshot getField(Member member){
        getFirebase();

        DocumentSnapshot documentSnapshot1 = null;

        try {
            documentSnapshot1 = FIREBASE.collection(FIELDNAME.label()).document(member.getUid())
                    .get().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return documentSnapshot1;
    }

    public Integer getIndex(){
        getFirebase();
        try {
            return Integer.parseInt(FIREBASE.collection(FIELDNAME.label()).orderBy("index", Query.Direction.DESCENDING).limit(1)
                    .get().get().getDocuments().get(0).getData().get("index").toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }
}
