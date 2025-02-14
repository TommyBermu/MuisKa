package com.muiska.clases.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.muiska.R;
import java.util.ArrayList;

public class StringAdapter extends RecyclerView.Adapter<StringAdapter.StringViewHolder> implements RecyclerViewClickListener {
    private ArrayList<String> mStrings;
    private RecyclerViewClickListener listener;

    public StringAdapter(ArrayList<String> mStrings, RecyclerViewClickListener listener) {
        this.mStrings = mStrings;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StringAdapter.StringViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_groups, parent, false);
        return new StringViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull StringViewHolder holder, int position) {
        holder.nombre.setText(mStrings.get(position));
    }

    @Override
    public int getItemCount() {
        return mStrings.size();
    }

    @Override
    public void onItemCliked(int position) {}

    @Override
    public void onItemLongCliked(int position) {}

    public static class StringViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;

        public StringViewHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
            super(itemView);

            nombre = itemView.findViewById(R.id.name_group);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int pos = getAbsoluteAdapterPosition();
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
                        int pos = getAbsoluteAdapterPosition();
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
