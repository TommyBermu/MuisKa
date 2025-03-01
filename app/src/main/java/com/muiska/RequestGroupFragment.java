package com.muiska;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.muiska.clases.Adapters.GroupAdapter;
import com.muiska.clases.Adapters.RecyclerViewClickListener;
import com.muiska.clases.Adapters.StringAdapter;
import com.muiska.clases.User;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestGroupFragment extends Fragment implements RecyclerViewClickListener {
    private ArrayList<String> groups;
    private StringAdapter adapter;
    private User usuario;

    public RequestGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_request_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usuario = ((MainActivity) requireActivity()).getUsuario();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewRequestGroup);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        groups = new ArrayList<>();
        adapter = new StringAdapter(groups, this);
        recyclerView.setAdapter(adapter);


        /*
        root.child("requests-groups").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged") // solo hace que no se muestre un warning en en adapter.notifyDataSetChanged()
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    groups.add(dataSnapshot.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

         */
    }

    @Override
    public void onItemCliked(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("grupo", groups.get(position));
        usuario.replaceFragment(new GroupSolicitudesFragment(), bundle);

    }

    @Override
    public void onItemLongCliked(int position) {}
}