package com.muiska;

import static org.chromium.base.ThreadUtils.runOnUiThread;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.muiska.clases.Adapters.RecyclerViewClickListener;
import com.muiska.clases.Libro;
import com.muiska.clases.Adapters.LibroAdapter;
import com.muiska.clases.Publicacion;
import com.muiska.clases.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class LibraryFragment extends Fragment implements RecyclerViewClickListener {
    private ArrayList<Libro> libros;
    private LibroAdapter adapter;
    private User usuario;

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

        usuario = ((MainActivity) requireActivity()).getUsuario();
        libros = new ArrayList<>();
        adapter = new LibroAdapter(libros, this);
        recyclerView.setAdapter(adapter);
        fetchBooks();

        //TODO implemantar un boton para refrecar los libros
    }

    @Override
    public void onItemCliked(int position) {
        //TODO implementar algo xd
    }

    @Override
    public void onItemLongCliked(int position) {
        //TODO implementar algo xd
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchBooks(){
        usuario.getExecutor().execute(() -> {
            String consulta = "SELECT * FROM Libro";
            try (PreparedStatement fetch = usuario.getConnection().prepareStatement(consulta)) {
                ResultSet rs = fetch.executeQuery();

                while(rs.next()){
                    libros.add(new Libro(
                            rs.getInt("idLibro"),
                            rs.getInt("Acceso"),
                            rs.getString("Titulo"),
                            rs.getBytes("linkDescarga"),
                            rs.getString("Descripcion"),
                            rs.getString("Autor")
                    ));

                    runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                    });
                }
            } catch (SQLException ex) {
                Log.e("CONSULTA", "Imposible realizar consulta '"+ consulta +"' ... FAIL");
                ex.printStackTrace();
            }
        });
    }
}