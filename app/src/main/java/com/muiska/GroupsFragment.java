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

import com.muiska.clases.Group;
import com.muiska.clases.Adapters.GroupAdapter;
import com.muiska.clases.Adapters.RecyclerViewClickListener;
import com.muiska.clases.User;

import java.util.ArrayList;

public class GroupsFragment extends Fragment implements RecyclerViewClickListener {
    private ArrayList<Group> grupos;
    private GroupAdapter adapter;
    private User usuario;

    public GroupsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usuario = ((MainActivity) requireActivity()).getUsuario();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewGrupos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        grupos = new ArrayList<>();
        adapter = new GroupAdapter(grupos, this);
        recyclerView.setAdapter(adapter);


        /*
        root.child("groups").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged") // solo hace que no se muestre un warning en en adapter.notifyDataSetChanged()
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Group grupo = dataSnapshot.getValue(Group.class);
                    grupos.add(grupo);
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
        bundle.putString("nombre", grupos.get(position).getName());
        bundle.putString("descripcion", grupos.get(position).getDescription());
        bundle.putString("link", grupos.get(position).getLink_poster());

        usuario.replaceFragment(new JoinGroupFragment(), bundle);
    }

    @Override
    public void onItemLongCliked(int position) {
        //TODO implementar algo xd
    }
}