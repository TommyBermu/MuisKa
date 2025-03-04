package com.muiska;

import static org.chromium.base.ThreadUtils.runOnUiThread;

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
import com.muiska.clases.Publicacion;
import com.muiska.clases.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

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

        fetch();
    }

    @Override
    public void onItemCliked(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("nombre", grupos.get(position).getNombre());
        bundle.putString("descripcion", grupos.get(position).getDescripcion());
        bundle.putByteArray("link", grupos.get(position).getLinkPoster());

        usuario.replaceFragment(new JoinGroupFragment(), bundle);
    }

    @Override
    public void onItemLongCliked(int position) {
        //TODO implementar algo xd
    }

    private void fetch(){
        usuario.getExecutor().execute(() -> {
            String consulta = "SELECT * FROM Grupo";
            try (PreparedStatement consularGrupos = usuario.getConnection().prepareStatement(consulta)){

                ResultSet rs = consularGrupos.executeQuery();

                while(rs.next()){
                    grupos.add(new Group(
                            rs.getInt("idGrupo"),
                            rs.getInt("Administrador_Usuario_idUsuario"),
                            rs.getInt("Miembros"),
                            rs.getInt("Acceso"),
                            rs.getString("Nombre"),
                            rs.getString("Descripcion"),
                            rs.getBytes("linkPortada")
                    ));

                    runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                    });
                }

            } catch (SQLException e){
                e.printStackTrace();
            }
        });
    }
}