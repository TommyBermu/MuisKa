package com.muiska.clases.Adapters;

import static org.chromium.base.ThreadUtils.runOnUiThread;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.muiska.R;
import com.muiska.clases.PeticionIngresoConvocatoria;
import com.muiska.clases.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ReqConvAdapter extends RecyclerView.Adapter<ReqConvAdapter.ReqConvViewHolder> implements RecyclerViewClickListener{
    private ArrayList<PeticionIngresoConvocatoria> requests;
    private RecyclerViewClickListener listener;
    private Context mContext;
    private User usuario;

    public ReqConvAdapter(ArrayList<PeticionIngresoConvocatoria> requests, Context context, RecyclerViewClickListener listener, User usuario) {
        this.requests = requests;
        this.listener = listener;
        this.mContext = context;
        this.usuario = usuario;
    }

    @NonNull
    @Override
    public ReqConvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_request, parent, false);
        return new ReqConvViewHolder(view, listener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ReqConvViewHolder holder, int position) {
        PeticionIngresoConvocatoria peticionIngresoConvocatoria = requests.get(position);

        holder.nombre.setText(peticionIngresoConvocatoria.getNombre() + " " + peticionIngresoConvocatoria.getApellidos());
        holder.email.setText(peticionIngresoConvocatoria.getEmail());
        holder.fecha.setText(peticionIngresoConvocatoria.getFecha().toString());

        holder.deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarPeticion(false, peticionIngresoConvocatoria);
            }
        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarPeticion(true, peticionIngresoConvocatoria);
            }
        });
    }

    private void actualizarPeticion(boolean accepted, @NonNull PeticionIngresoConvocatoria peticion) {
        usuario.getExecutor().execute(() -> {
            String consulta = "CALL aceptarEnConvocatoria(?, ?, ?, ?, ?)";
            try (PreparedStatement gestionar = usuario.getConnection().prepareStatement(consulta)){
                Log.i("DATOOOOOOOOOOOOOOO", ""+usuario.getId());
                gestionar.setBoolean(1, accepted);
                gestionar.setInt(2, usuario.getId());
                gestionar.setInt(3, peticion.getUsuarioIdUsuario());
                gestionar.setInt(4, peticion.getConvocatoriaPublicacionIdPublicacion());
                gestionar.setInt(5, peticion.getPeticion_idPeticion());

                int filas = gestionar.executeUpdate();
                runOnUiThread(() -> {
                    if (accepted)
                        Toast.makeText(mContext, "Petición Aceptada", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(mContext, "Petición Rechazada", Toast.LENGTH_SHORT).show();
                });
            } catch (SQLException e){
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    @Override
    public void onItemCliked(int position) {}

    @Override
    public void onItemLongCliked(int position) {}

    public static class ReqConvViewHolder extends RecyclerView.ViewHolder{
        TextView nombre, email, fecha;
        Button deny, accept;

        public ReqConvViewHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
            super(itemView);

            nombre = itemView.findViewById(R.id.nombre_comunero);
            email = itemView.findViewById(R.id.email_comunero);
            fecha = itemView.findViewById(R.id.fecha_peticion);
            deny = itemView.findViewById(R.id.button_deny);
            accept = itemView.findViewById(R.id.button_accept);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            listener.onItemCliked(pos);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            listener.onItemLongCliked(pos);
                        }
                    }
                    return true;
                }
            });
        }
    }
}
