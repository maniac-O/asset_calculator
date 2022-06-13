package com.SH.asset_calculator.repository;

import com.SH.asset_calculator.domain.FirebaseCollectionEnum;
import com.SH.asset_calculator.domain.Member;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.SH.asset_calculator.domain.FirebaseCollectionEnum.FIELDNAME;
import static com.SH.asset_calculator.domain.FirebaseCollectionEnum.PUBLIC_SCOPE;

@Repository
public class BoardRepository {
    private Integer PAGE_LIMIT = 5;
    private Firestore FIREBASE;

    private void getFirebase(){
        FIREBASE = FirestoreClient.getFirestore();
    }

    public List<QueryDocumentSnapshot> getBoard(){
        getFirebase();

        // start pagination
        CollectionReference public_scope = FIREBASE.collection(PUBLIC_SCOPE.label());
        Query q = public_scope.orderBy("index", Query.Direction.DESCENDING).limit(PAGE_LIMIT);
/*
        // 셈플자료 20개 입력
        try {
            DocumentSnapshot documentSnapshot = FIREBASE.collection(FIELDNAME.label()).document(member.getUid()).get().get();
            for (int i = 0; i < 20; i++) {
                ApiFuture<WriteResult> set = FIREBASE.collection(PUBLIC_SCOPE.label()).document(member.getUid() + i).set(documentSnapshot);
                System.out.println("set = " + set);
                if (set != null) {
                    HashMap<String, Object> param = new HashMap<>();

                    param.put("displayName", member.getDisplayName());
                    param.put("updateTime", new java.sql.Timestamp(System.currentTimeMillis()));
                    int index = Integer.parseInt(FIREBASE.collection(PUBLIC_SCOPE.label()).orderBy("index", Query.Direction.DESCENDING).limit(1).get().get().getDocuments().get(0).getData().get("index").toString()) + 1;
                    System.out.println("index = " + index);
                    param.put("index", index);

                    FIREBASE.collection(PUBLIC_SCOPE.label()).document(member.getUid() + i).update(param);
                }

                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

 */

        try {
            List<QueryDocumentSnapshot> docs = q.get().get(30, TimeUnit.SECONDS).getDocuments();
            /*
            for (QueryDocumentSnapshot doc : docs) {
                ("doc = " + doc.getData());
            }

             */
            return docs;
            /*
            QueryDocumentSnapshot lastDoc = docs.get(docs.size() - 1);
            // 무한 로딩 페이지
            Query q2 = public_scope.orderBy("updateTime").startAfter(lastDoc).limit(PAGE_LIMIT);
            List<QueryDocumentSnapshot> docs2 = q2.get().get(30, TimeUnit.SECONDS).getDocuments();
            for (QueryDocumentSnapshot doc2 : docs2) {
                System.out.println("doc2 = " + doc2.getData().get("updateTime"));
            }

             */

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        // end pagination

        return null;
    }

    public List<QueryDocumentSnapshot> getMoreBoard(String index){
        getFirebase();
        CollectionReference public_scope = FIREBASE.collection(PUBLIC_SCOPE.label());
        //QueryDocumentSnapshot
        try {
            QueryDocumentSnapshot startDoc = public_scope.whereEqualTo("index", Integer.parseInt(index)).get().get().getDocuments().get(0);

            if (startDoc == null)
                return null;

            Query q = public_scope.orderBy("updateTime", Query.Direction.DESCENDING).startAfter(startDoc).limit(PAGE_LIMIT);
            List<QueryDocumentSnapshot> docs = q.get().get(30, TimeUnit.SECONDS).getDocuments();

            return docs;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        catch (TimeoutException e) {
            e.printStackTrace();
        }

        return null;
    }
}
