package com.muiska;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.muiska.clases.Adapters.RecyclerViewClickListener;
import com.muiska.clases.Adapters.StringAdapter;
import com.muiska.clases.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestConvFragment extends Fragment implements RecyclerViewClickListener {
    private ArrayList<String> convs;
    private StringAdapter adapter;
    private User usuario;

    private final DatabaseReference root = FirebaseDatabase.getInstance().getReference();

    public RequestConvFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_request_conv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usuario = ((MainActivity) requireActivity()).getUsuario();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewRequestConv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        convs = new ArrayList<>();
        adapter = new StringAdapter(convs, this);
        recyclerView.setAdapter(adapter);

        root.child("requests-convs").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged") // solo hace que no se muestre un warning en en adapter.notifyDataSetChanged()
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    convs.add(dataSnapshot.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public void onItemCliked(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("convocatoria", convs.get(position));
        usuario.replaceFragment(new ConvSolicitudesFragment(), bundle);
    }

    @Override
    public void onItemLongCliked(int position) {}
}