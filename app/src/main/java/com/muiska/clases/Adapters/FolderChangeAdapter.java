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
import com.muiska.clases.FolderChange;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class FolderChangeAdapter extends RecyclerView.Adapter<FolderChangeAdapter.FolderChangeViewHolder> implements RecyclerViewClickListener {
    private ArrayList<FolderChange> mChanges;
    private RecyclerViewClickListener listener;
    private Context mContext;

    public FolderChangeAdapter(ArrayList<FolderChange> mChanges, Context context, RecyclerViewClickListener listener) {
        this.mChanges = mChanges;
        this.listener = listener;
        this.mContext = context;
    }

    @NonNull
    @Override
    public FolderChangeAdapter.FolderChangeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_request, parent, false);
        return new FolderChangeAdapter.FolderChangeViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderChangeAdapter.FolderChangeViewHolder holder, int position) {
        FolderChange folderChange = mChanges.get(position);

        holder.nombre.setText(folderChange.getName());
        holder.email.setText(folderChange.getEmail());

        holder.deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Presione de nuevo para confirmar la denegación", Toast.LENGTH_SHORT).show();
                holder.deny.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // actualizarPeticion(false, folderChange);
                        Toast.makeText(mContext, "Petición denegada, pero no se cambia en la BD", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Presione de nuevo para confirmar la aceptación", Toast.LENGTH_SHORT).show();
                holder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // actualizarPeticion(true, folderChange);
                        Toast.makeText(mContext, "Petición aceptada, pero no se cambia en la BD", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mChanges.size();
    }

    @Override
    public void onItemCliked(int position) {}

    @Override
    public void onItemLongCliked(int position) {}

    /*
    private void actualizarPeticion(boolean accepted, @NonNull FolderChange folderChange) {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        root.child("requests-folder_change").child(folderChange.getRef()).removeValue();
        FirebaseStorage storageReference = FirebaseStorage.getInstance();
        storageReference.getReferenceFromUrl(folderChange.getDocument_url()).delete();
        storageReference.getReferenceFromUrl(folderChange.getLetter_url()).delete();
        mChanges.remove(folderChange);

        if (accepted){
            db.collection("users").document(folderChange.getEmail()).update("carpeta", folderChange.getCarpeta()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    String key = root.push().getKey();
                    assert key != null;
                    root.child("folders").child(folderChange.getCarpeta()).child(key).setValue(folderChange.getEmail());
                    Toast.makeText(mContext, "Petición aceptada", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(mContext, "Petición denegada", Toast.LENGTH_SHORT).show();
        }
    }
    */

    public static class FolderChangeViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, email;
        Button deny, accept;

        public FolderChangeViewHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
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
