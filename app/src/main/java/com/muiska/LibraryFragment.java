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
import com.muiska.clases.Adapters.RecyclerViewClickListener;
import com.muiska.clases.Libro;
import com.muiska.clases.Adapters.LibroAdapter;
import com.muiska.clases.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class LibraryFragment extends Fragment implements RecyclerViewClickListener {
    private ArrayList<Libro> libros;
    private LibroAdapter adapter;
    private User usuario;

    private final DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    public LibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewLibrary);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        libros = new ArrayList<>();
        adapter = new LibroAdapter(libros, this); //TODO
        recyclerView.setAdapter(adapter);

        root.child("library").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged") // solo hace que no se muestre un warning en en adapter.notifyDataSetChanged()
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Libro libro = dataSnapshot.getValue(Libro.class);
                    libros.add(libro);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public void onItemCliked(int position) {/*
        Bundle bundle = new Bundle();
        bundle.putString("nombre", grupos.get(position).getName());
        bundle.putString("descripcion", grupos.get(position).getDescription());
        bundle.putString("link", grupos.get(position).getLink_poster());
        requireActivity().getSupportFragmentManager().setFragmentResult("data", bundle);
        usuario.replaceFragment(new JoinGroupFragment());*/
    }

    @Override
    public void onItemLongCliked(int position) {
        //TODO implementar algo xd
    }
}