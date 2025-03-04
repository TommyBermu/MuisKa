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

import com.muiska.clases.Adapters.GroupAdapter;
import com.muiska.clases.Adapters.RecyclerViewClickListener;
import com.muiska.clases.Group;
import com.muiska.clases.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RequestGroupFragment extends Fragment implements RecyclerViewClickListener {
    private ArrayList<Group> groups;
    private GroupAdapter adapter;
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
        adapter = new GroupAdapter(groups, this);
        recyclerView.setAdapter(adapter);

        fetch();
    }

    @Override
    public void onItemCliked(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("grupo", groups.get(position).getNombre());
        usuario.replaceFragment(new GroupSolicitudesFragment(), bundle);

    }

    @Override
    public void onItemLongCliked(int position) {}

    @SuppressLint("NotifyDataSetChanged")
    private void fetch(){
        usuario.getExecutor().execute(() -> {
            String consulta = "SELECT idGrupo, Nombre FROM Grupo JOIN Ingreso ON Grupo_idGrupo = idGrupo";
            try (PreparedStatement consultarGrupos = usuario.getConnection().prepareStatement(consulta)){

                ResultSet rs = consultarGrupos.executeQuery();

                while (rs.next()){
                    groups.add(new Group( // TODO creo que aca no es grupo sino peticion ingreso grupo xd
                        rs.getInt("idGrupo"),
                        rs.getString("Nombre")
                    ));
                }

                runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                });

            } catch (SQLException e){
                e.printStackTrace();
            }
        });
    }
}