package com.muiska;

import static org.chromium.base.ThreadUtils.runOnUiThread;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.muiska.clases.Adapters.RecyclerViewClickListener;
import com.muiska.clases.Adapters.ReqConvAdapter;
import com.muiska.clases.PeticionIngresoConvocatoria;
import com.muiska.clases.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConvSolicitudesFragment extends Fragment implements RecyclerViewClickListener {
    private ArrayList<PeticionIngresoConvocatoria> peticionIngresoConvocatorias;
    private ReqConvAdapter adapter;
    private int conv;
    private User usuario;
    private FragmentActivity context;

    public ConvSolicitudesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_conv_solicitudes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = requireActivity();
        usuario = ((MainActivity) context).getUsuario();

        if (getArguments() != null){
            conv = getArguments().getInt("idConv"); // id de la convocatoria
            Toast.makeText(context, getArguments().getString("convNombre"), Toast.LENGTH_SHORT).show();
        }

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewConvSolicitudes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        peticionIngresoConvocatorias = new ArrayList<>();
        adapter = new ReqConvAdapter(peticionIngresoConvocatorias, context, this, usuario);
        recyclerView.setAdapter(adapter);

        fetch();
    }

    @Override
    public void onItemCliked(int position) {
        Toast.makeText(context, "Esta petición no tiene archivos relacionados", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongCliked(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_goto, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        Button aceptar = dialogView.findViewById(R.id.confirmar);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                Bundle bundle = new Bundle();
                bundle.putInt("usrId", peticionIngresoConvocatorias.get(position).getUsuarioIdUsuario());
                usuario.replaceFragment(new SeeUserInfoFragment(), bundle);
            }
        });

        Button cancelar = dialogView.findViewById(R.id.cancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void fetch(){
        usuario.getExecutor().execute(() -> {
            String consulta = "SELECT * FROM Ingreso JOIN Peticion ON idPeticion = Peticion_idPeticion JOIN Usuario ON Usuario_idUsuario = idUsuario JOIN Publicacion ON Convocatoria_Publicacion_idPublicacion = idPublicacion WHERE Convocatoria_Publicacion_idPublicacion = ?";
            try (PreparedStatement fetch = usuario.getConnection().prepareStatement(consulta)) {

                fetch.setInt(1, conv); // se coloca el id de la convocatoria

                ResultSet rs = fetch.executeQuery();

                while(rs.next()){
                    if (rs.getObject("Estado") == null) {
                        peticionIngresoConvocatorias.add(new PeticionIngresoConvocatoria(
                                rs.getInt("idUsuario"),
                                rs.getString("Nombre"),
                                rs.getString("Apellidos"),
                                rs.getString("Email"),
                                rs.getDate("FechaEnvio"),
                                rs.getString("CartaMotivacion"),
                                rs.getInt("Convocatoria_Publicacion_idPublicacion"),
                                rs.getString("Titulo"),
                                rs.getInt("Peticion_idPeticion")
                        ));
                    }
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