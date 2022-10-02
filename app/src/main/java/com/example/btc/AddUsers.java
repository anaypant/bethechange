package com.example.btc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AddUsers extends Activity {

    RecyclerView recView;
    AdapterAddUsers adapterAddUsers;
    List<ModelClassAddUsers> users;
    ImageButton backArrow;
    EditText searchField;
    public AddUsers(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_users);
        recView = findViewById(R.id.addUsersRecyclerFragment);
        backArrow = findViewById(R.id.backArrow);

        recView.setHasFixedSize(true);
        recView.setLayoutManager(new LinearLayoutManager(AddUsers.this));
        users = new ArrayList<>();
        searchField = findViewById(R.id.UserSearchBar);


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(AddUsers.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                },0);
            }
        });
        searchField.setMaxHeight(30);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                getAllUsers();
            }
        });
    }



        private void getAllUsers() {
            FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    users.clear();
                    for(DataSnapshot ds: snapshot.getChildren()){
                        ModelClassAddUsers mU = ds.getValue(ModelClassAddUsers.class);

                        assert fUser != null;
                        if(!searchField.getText().toString().trim().equals("")){
                            if (!mU.getUid().equals(fUser.getUid()) && mU.getName().contains(searchField.getText().toString().trim())) {
                                users.add(mU);
                            }

                            adapterAddUsers = new AdapterAddUsers(AddUsers.this,users);
                            recView.setAdapter(adapterAddUsers);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        // Inflate the layout for this fragment

}