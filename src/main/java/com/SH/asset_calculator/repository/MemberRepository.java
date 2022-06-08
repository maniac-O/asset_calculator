package com.SH.asset_calculator.repository;

import com.SH.asset_calculator.domain.Member;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepository {

    private Firestore FIREBASE;

    private void getFirebase(){
        FIREBASE = FirestoreClient.getFirestore();
    }

    public List<Member> getMembersScopePublic(){
        getFirebase();
        //FIREBASE.

        return null;
    }
}
