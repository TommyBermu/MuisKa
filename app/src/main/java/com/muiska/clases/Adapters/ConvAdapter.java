package com.muiska.clases.Adapters;

import static org.chromium.base.ThreadUtils.runOnUiThread;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.muiska.R;
import com.muiska.clases.PeticionIngresoConvocatoria;

import java.util.ArrayList;

public class ConvAdapter extends RecyclerView.Adapter<ConvAdapter.StringViewHolder> implements RecyclerViewClickListener {
    private ArrayList<PeticionIngresoConvocatoria> ingresosConvocatorias;
    private RecyclerViewClickListener listener;

    public ConvAdapter(ArrayList<PeticionIngresoConvocatoria> ingresosConvocatorias, RecyclerViewClickListener listener) {
        this.ingresosConvocatorias = ingresosConvocatorias;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ConvAdapter.StringViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_convs, parent, false);
        return new StringViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull StringViewHolder holder, int position) {
        holder.nombre.setText(ingresosConvocatorias.get(position).getConvNombre());
    }

    @Override
    public int getItemCount() {
        return ingresosConvocatorias.size();
    }

    @Override
    public void onItemCliked(int position) {}

    @Override
    public void onItemLongCliked(int position) {}

    public static class StringViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;

        public StringViewHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
            super(itemView);

            nombre = itemView.findViewById(R.id.name_conv);

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
