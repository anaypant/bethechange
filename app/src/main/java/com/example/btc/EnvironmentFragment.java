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

public class EnvironmentFragment extends Fragment {
    ArrayList<ModelClass> modelClassArrayList;
    NewsViewAdapter adapter;
    private RecyclerView recyclerViewofEnvironment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.environment_layout, null);
        recyclerViewofEnvironment = v.findViewById(R.id.recycleviewofenvironment);
        modelClassArrayList = new ArrayList<>();
        recyclerViewofEnvironment.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsViewAdapter(getContext(), modelClassArrayList, "EnvironmentNews", new AdapterSelecterListener() {
            @Override
            public void onUpvoteClick(ModelClass c, int position, String tabName) {
                VotingUtils.updateUpVotes(position, tabName);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onDownVoteClick(ModelClass c, int position, String tabName) {
                VotingUtils.updateDownVotes(position, tabName);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCommentClick(ModelClass c, int position, String tabName) {
                VotingUtils.goToComments(getContext(), c, position, tabName);
            }
        });
        adapter.setTabName("EnvironmentNews");

        recyclerViewofEnvironment.setAdapter(adapter);

        findNews();
        return v;
    }

    private void findNews() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("articles");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.child("EnvironmentNews").getChildren()){
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