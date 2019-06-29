package com.example.android.bookclublatest.Admin.RequestsForMember;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.bookclublatest.Base.BaseActivity;
import com.example.android.bookclublatest.R;
import com.example.android.bookclublatest.RequestForMember.RequestForMemberActivity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestsForMember extends BaseActivity implements RequetsForMemberContract.View {


@BindView(R.id.allrequestformember)
    RecyclerView allrequestformember;

RequetsForMemberModel requetsForMemberModel;
ArrayList<RequetsForMemberModel> arrayList;

RequestsForMemberAdapter requestsForMemberAdapter;


FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference;
FirebaseStorage firebaseStorage;


RequetsForMemberContract.Presenter<RequetsForMemberContract.View> mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_request_for_member);
        mPresenter=new RequetsForMemberPresenter<>();
        ButterKnife.bind(this);

        arrayList=new ArrayList<>();
        allrequestformember.setHasFixedSize(false);
        allrequestformember.setLayoutManager(new LinearLayoutManager(this));
        requestsForMemberAdapter=new RequestsForMemberAdapter(this,arrayList);
        allrequestformember.setAdapter(requestsForMemberAdapter);


        databaseReference=FirebaseDatabase.getInstance().getReference("requstsformember");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           for(DataSnapshot ds : dataSnapshot.getChildren())
           {
               final String emailm=ds.getValue().toString();
               final String emailx=emailm.replace('.',',');

               final DatabaseReference databaseReference2=FirebaseDatabase.getInstance().getReference("users");


               databaseReference2.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        if(dataSnapshot1.getKey().equals(emailx)) {

                            String name1 = dataSnapshot1.child("name").getValue().toString();
                            String email1 = dataSnapshot1.child("email").getValue().toString();
                            String phone1 = dataSnapshot1.child("phonenumber").getValue().toString();
                            String admission1 = dataSnapshot1.child("admissionnumber").getValue().toString();
                            RequetsForMemberModel requetsForMemberModel = new RequetsForMemberModel(name1, admission1, email1, phone1);
                            arrayList.add(requetsForMemberModel);
                        }

                   }



                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });

           }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        requestsForMemberAdapter.notifyDataSetChanged();

    }
}