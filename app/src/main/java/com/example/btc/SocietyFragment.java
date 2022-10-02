package com.example.btc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SocietyFragment extends Fragment {
    String api="2a2429ecaaa4496680cf6d23b9e8dc0a";
    ArrayList<ModelClass> modelClassArrayList;
    NewsViewAdapter adapter;
    private RecyclerView recyclerViewofSociety;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.society_layout, null);
        recyclerViewofSociety = v.findViewById(R.id.recycleviewofsociety);
        modelClassArrayList = new ArrayList<>();
        recyclerViewofSociety.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsViewAdapter(getContext(), modelClassArrayList, "SocietyNews", new AdapterSelecterListener() {
            @Override
            public void onUpvoteClick(ModelClass c, int position, String tabName) {
                adapter.setTabName("SocietyNews");

                VotingUtils.updateUpVotes(position, tabName);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onDownVoteClick(ModelClass c, int position, String tabName) {
                adapter.setTabName("SocietyNews");

                VotingUtils.updateDownVotes(position, tabName);
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCommentClick(ModelClass c, int position, String tabName) {
                VotingUtils.goToComments(getContext(), c, position, tabName);
            }
        });
        adapter.setTabName("SocietyNews");

        recyclerViewofSociety.setAdapter(adapter);
        findNews();
        return v;
    }

    private void findNews() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("articles");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.child("SocietyNews").getChildren()){
                    modelClassArrayList.add(ds.getValue(ModelClass.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}