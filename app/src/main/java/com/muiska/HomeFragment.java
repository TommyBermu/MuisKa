package com.muiska;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.muiska.clases.Publicacion;
import com.muiska.clases.Adapters.PublicacionAdapter;
import com.muiska.clases.Adapters.RecyclerViewClickListener;
import com.muiska.clases.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

        /*
        root.child("publications").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged") // solo hace que no se muestre un warning en en adapter.notifyDataSetChanged()
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Publicacion publicacion = dataSnapshot.getValue(Publicacion.class);
                    publicaciones.add(publicacion);
                    Collections.sort(publicaciones);
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
        String titulo = publicaciones.get(position).getTitulo();

        if (usuario.getInscripciones().containsKey(titulo)) {
            Toast.makeText(requireActivity(), "Ya te has inscrito en: " + titulo, Toast.LENGTH_SHORT).show();// si titulo no está en el keyset de inscripciones
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
                Map<String, Object> mapa = new HashMap<>();
                mapa.put("email", usuario.getEmail());
                mapa.put("name", usuario.getNombre() + " " + usuario.getApellidos());
                mapa.put("accepted", null); // null porque, aunque se incriba, no significa que sea aceptado


                /*
                String path = root.push().getKey();
                assert path != null;

                mapa.put("ref", path);

                // se agrega la inscripcion a la convocatoria en la base de datos
                root.child("requests-convs").child(titulo).child(path).setValue(mapa).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            // primero se agrega a las preferencias que ya está inscrito en la convocatoria
                            SharedPreferences prefs = requireActivity().getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE);
                            SharedPreferences.Editor prefsEditor = prefs.edit();
                            HashSet<String> set = new HashSet<>(prefs.getStringSet("inscripciones", new HashSet<>()));
                            set.add(titulo);
                            prefsEditor.putStringSet("inscripciones", set);
                            prefsEditor.apply();

                            // luego se actualiza en el firebase un atributo del ususario que diga en qué convocatorias está inscrito
                            Map<String, HashMap<String, Boolean>> convs = new HashMap<>(); // es un mapa que guarda mapas de convocatorias (el equivalente al atributo inscipciones del User)
                            HashMap<String, Boolean> inscripciones = new HashMap<>();
                            inscripciones.put(titulo,false);
                            convs.put("inscripciones", inscripciones);
                            db.collection("users").document(usuario.getEmail()).set(convs, SetOptions.merge());

                            //luego el hashmap del usuario mismo
                            usuario.addInscripcion(titulo); //TODO sirve, pero como se crea el usuario según las preferencias, pues al cerrar sesión se borra el hashmap xd

                            Toast.makeText(requireActivity(), "Te has inscrito en: " + titulo, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireActivity(), "Algo ha salido mal: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                 */
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
        //TODO implementar algo xd
    }
}