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

public class EconomyFragment extends Fragment {
    ArrayList<ModelClass> modelClassArrayList;
    NewsViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.econ_layout, null);
        RecyclerView recyclerViewofEconomy = v.findViewById(R.id.recycleviewofeconomy);
        modelClassArrayList = new ArrayList<>();
        recyclerViewofEconomy.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsViewAdapter(getContext(), modelClassArrayList, "EconomyNews", new AdapterSelecterListener() {
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
        adapter.setTabName("EconomyNews");

        recyclerViewofEconomy.setAdapter(adapter);

        findNews();
        for (int z = 0; z < modelClassArrayList.size();z++){
            String content = modelClassArrayList.get(z).getDescription();
            String head = modelClassArrayList.get(z).getTitle();
            String urltoImg = modelClassArrayList.get(z).getUrlToImage();
            if(content == null || head == null || urltoImg==null){
                modelClassArrayList.remove(z);
                z--;
            }
        }
        return v;
    }

    private void findNews() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("articles");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.child("EconomyNews").getChildren()){
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
