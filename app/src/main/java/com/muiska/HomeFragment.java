package com.muiska;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Button;
import android.widget.Toast;

import com.muiska.clases.Publicacion;
import com.muiska.clases.Adapters.PublicacionAdapter;
import com.muiska.clases.Adapters.RecyclerViewClickListener;
import com.muiska.clases.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

public class HomeFragment extends Fragment implements RecyclerViewClickListener {
    private ArrayList<Publicacion> publicaciones;
    private PublicacionAdapter adapter;
    private User usuario;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usuario = ((MainActivity) requireActivity()).getUsuario();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewHome);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        publicaciones = new ArrayList<>();
        adapter = new PublicacionAdapter(publicaciones, requireActivity(), this);
        recyclerView.setAdapter(adapter);

        fetch();

        // TODO crear un boton para actualizar el feed y que llame a la funcion fetch()
    }

    @Override
    public void onItemCliked(int position) {
        String titulo = publicaciones.get(position).getTitulo();
        boolean tipo = publicaciones.get(position).getTipo();

        if (!tipo) { // vamos a coger al anuncio como false, osea el tipo, no que sea falso o algo asi xd
            return; // entones si es un anuncio pues que no pase nada
        }

        if (usuario.getInscripciones().contains(titulo)) {
            Toast.makeText(requireActivity(), "Ya te has inscrito en: " + titulo, Toast.LENGTH_SHORT).show();// si titulo no está en el set de inscripciones
            return;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_confirm, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        Button confirm = dialogView.findViewById(R.id.confirmar);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario.getExecutor().execute(() -> {
                    String consulta = "CALL PeticionIngresoConvocatoria(?, ?, ?)";
                    try (PreparedStatement inscribir = usuario.getConnection().prepareStatement(consulta)) {

                        //se agrega a la base de datos SQL
                        inscribir.setInt(1, usuario.getId());
                        inscribir.setString(2, "nulo por ahora xd"); // TODO link de la carta
                        inscribir.setInt(3, publicaciones.get(position).getPubId());
                        int filasAfectadas = inscribir.executeUpdate();

                        usuario.addInscripcion(titulo);
                        Toast.makeText(requireActivity(), "Te has inscrito en: " + titulo, Toast.LENGTH_SHORT).show();
                        Log.i("CONSULTA", "la consutla se realizo con exito");

                    } catch (SQLException ex) {
                        Log.e("CONSULTA", "Imposible realizar consulta '"+ consulta +"' ... FAIL");
                        ex.printStackTrace();
                    }
                });
                dialog.cancel();
            }
        });
        Button discard = dialogView.findViewById(R.id.cancelar);
        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    @Override
    public void onItemLongCliked(int position) {
        //TODO implementar lo de des-inscribirse
    }

    @SuppressLint("NotifyDataSetChanged")
    public void fetch(){
        usuario.getExecutor().execute(() -> {
            // TODO solo se consulta en la tabla de publicacion porq es la unica con datos que se muestran, pero hay que tambien buscar en las otras tablas
            String consulta = "SELECT * FROM Publicacion";
            try (PreparedStatement fetch = usuario.getConnection().prepareStatement(consulta)) {

                ResultSet rs = fetch.executeQuery();

                while(rs.next()){
                    publicaciones.add(new Publicacion(
                            rs.getInt("idPublicacion"),
                            rs.getString("Titulo"),
                            rs.getBytes("Imagen"),
                            rs.getString("Descripcion"),
                            rs.getString("FechaFinalizacion"),
                            rs.getString("FechaPublicacion"),
                            rs.getBoolean("Tipo")
                    ));
                    Collections.sort(publicaciones);
                    adapter.notifyDataSetChanged();
                }
            } catch (SQLException ex) {
                Log.e("CONSULTA", "Imposible realizar consulta '"+ consulta +"' ... FAIL");
                ex.printStackTrace();
            }
        });

    }
}