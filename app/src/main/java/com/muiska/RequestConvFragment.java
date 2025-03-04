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

import com.muiska.clases.Adapters.ConvAdapter;
import com.muiska.clases.Adapters.RecyclerViewClickListener;
import com.muiska.clases.PeticionIngresoConvocatoria;
import com.muiska.clases.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RequestConvFragment extends Fragment implements RecyclerViewClickListener {
    private ArrayList<PeticionIngresoConvocatoria> convs;
    private ConvAdapter adapter;
    private User usuario;


    public RequestConvFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_request_conv, container, false);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usuario = ((MainActivity) requireActivity()).getUsuario();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewRequestConv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        convs = new ArrayList<>();
        adapter = new ConvAdapter(convs, this);
        recyclerView.setAdapter(adapter);

        usuario.getExecutor().execute(() -> {
            String consulta = "SELECT DISTINCT Convocatoria_Publicacion_idPublicacion, Titulo FROM Ingreso JOIN Publicacion ON Convocatoria_Publicacion_idPublicacion = idPublicacion"; // se cogen todas las publiaciones con peticiones de ingreso
            try (PreparedStatement consultarConvs = usuario.getConnection().prepareStatement(consulta)) {
                ResultSet rs = consultarConvs.executeQuery();

                while (rs.next()){
                    convs.add(new PeticionIngresoConvocatoria(
                        rs.getString("Titulo"),
                        rs.getInt("Convocatoria_Publicacion_idPublicacion")
                    ));
                }

                runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                });

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onItemCliked(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("convNombre", convs.get(position).getConvNombre());
        bundle.putInt("idConv", convs.get(position).getConvocatoriaPublicacionIdPublicacion());
        usuario.replaceFragment(new ConvSolicitudesFragment(), bundle);
    }

    @Override
    public void onItemLongCliked(int position) {}
}