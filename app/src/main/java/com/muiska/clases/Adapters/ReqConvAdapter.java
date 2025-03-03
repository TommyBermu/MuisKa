package com.muiska.clases.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.muiska.R;
import java.util.ArrayList;
import java.util.HashMap;


public class ReqConvAdapter extends RecyclerView.Adapter<ReqConvAdapter.ReqConvViewHolder> implements RecyclerViewClickListener{
    private ArrayList<HashMap<String, Object>> mHashMaps;
    private RecyclerViewClickListener listener;
    private Context mContext;

    public ReqConvAdapter(ArrayList<HashMap<String, Object>> mHashMaps, Context context, RecyclerViewClickListener listener) {
        this.mHashMaps = mHashMaps;
        this.listener = listener;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ReqConvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_request, parent, false);
        return new ReqConvViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ReqConvViewHolder holder, int position) {
        HashMap<String, Object> hashMap = mHashMaps.get(position);

        holder.nombre.setText(hashMap.get("name").toString());
        holder.email.setText(hashMap.get("email").toString());

        holder.deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarPeticion(false, hashMap);
            }
        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarPeticion(true, hashMap);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHashMaps.size();
    }

    @Override
    public void onItemCliked(int position) {}

    @Override
    public void onItemLongCliked(int position) {}

    private void actualizarPeticion(boolean accepted, @NonNull HashMap<String, Object> mapa) {
        if (accepted){
            Toast.makeText(mContext, "Petición aceptada, pero no se cambia en la BD", Toast.LENGTH_SHORT).show();
            // putDA(true, mapa);
        }
        else {
            Toast.makeText(mContext, "Petición denegada, pero no se cambia en la BD", Toast.LENGTH_SHORT).show();
            // putDA(false, mapa);
        }
        mHashMaps.remove(mapa);
    }

    /*
    private void putDA(boolean b, HashMap<String, Object> mapa){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        String reference = mapa.get("ref").toString();
        if (tipo.equals(Tipo.CONVOCATORIA)){
            root.child("requests-convs").child(folder).child(reference).child("accepted").setValue(b);
        } else if (tipo.equals(Tipo.GRUPO)){
            root.child("requests-groups").child(folder).child(reference).child("accepted").setValue(b);
        }
    }
    */

    public enum Tipo{
        CONVOCATORIA,
        GRUPO
    }

    public static class ReqConvViewHolder extends RecyclerView.ViewHolder{
        TextView nombre, email;
        Button deny, accept;

        public ReqConvViewHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
            super(itemView);

            nombre = itemView.findViewById(R.id.nombre_comunero);
            email = itemView.findViewById(R.id.email_comunero);
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
