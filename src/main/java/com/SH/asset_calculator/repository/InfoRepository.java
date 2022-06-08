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

    public void setScope(Member member, String stat){
        getFirebase();

        if (stat.equals("true")) {
            HashMap<String, Object> param = new HashMap<>();

            param.put("displayName", member.getDisplayName());
            param.put("updateTime", new Timestamp(System.currentTimeMillis()));


            WriteResult writeResult = null;

            try {

                param.put("index", Integer.parseInt(FIREBASE.collection(PUBLIC_SCOPE.label()).orderBy("index", Query.Direction.DESCENDING).limit(1).get().get().getDocuments().get(0).getData().get("index").toString())+1);

                // field를 가져와서 public scope에 넣어줘야함
                writeResult = FIREBASE.collection(PUBLIC_SCOPE.label()).document(member.getUid()).set(getField(member)).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if ( writeResult.getUpdateTime() != null)
                FIREBASE.collection(PUBLIC_SCOPE.label()).document(member.getUid()).set(param, SetOptions.merge());





        }else if (stat.equals("false")){
            FIREBASE.collection(PUBLIC_SCOPE.label()).document(member.getUid()).delete();
        }

        log.info("successfully change scope stat 'UID: "+ member.getUid() +"', 'changed stat: "+stat+"'");
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

    public boolean insertField(Member member, HashMap<String, String> manual_field, HashMap<String, ArrayList<String>> custom_field, HashMap<String, String> manual_percent, HashMap<String, String> custom_percent ){
        getFirebase();
        HashMap<String, HashMap<String, HashMap>> param = new HashMap<>();
        HashMap<String, HashMap> p1 = new HashMap<>();
        HashMap<String, HashMap> p2 = new HashMap<>();
        p1.put("manual_field", manual_field);
        p1.put("custom_field", custom_field);
        p2.put("manual_percent", manual_percent);
        p2.put("custom_percent", custom_percent);
        param.put("finance", p1);
        param.put("percent", p2);

        try {

            FIREBASE.collection(FIELDNAME.label()).document(member.getUid())
                    .set(param);
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
}
