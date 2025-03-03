package com.muiska.clases.Adapters;

import static com.muiska.clases.FileDownloader.REQUEST_CODE;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.muiska.R;
import com.muiska.clases.FileDownloader;
import com.muiska.clases.Libro;
import java.util.ArrayList;

public class LibroAdapter extends RecyclerView.Adapter<LibroAdapter.LibroViewHolder> implements RecyclerViewClickListener {
    private ArrayList<Libro> mLibros;
    private RecyclerViewClickListener listener;

    public LibroAdapter(ArrayList<Libro> mLibros, RecyclerViewClickListener listener){
        this.mLibros = mLibros;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LibroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_libros, parent, false);
        return new LibroViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull LibroViewHolder holder, int position) {
        Libro libro = mLibros.get(position);

        holder.title.setText(libro.getTitulo());
        holder.description.setText(libro.getDescripcion_libro());
        holder.autor.setText(libro.getAutor());

        holder.button_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] linkLibro = libro.getLink_libro();

                if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) v.getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                } else {
                    // TODO que hacer si se da click en el boton del libro: descargarlo xd
                    FileDownloader fileDownloader = new FileDownloader();
                    //f ileDownloader.downloadFile(v.getContext(), libro.getLink_libro(), libro.getTitulo());
                    Toast.makeText(v.getContext(), "La descarga de "+libro.getTitulo()+" ha finalizado", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mLibros.size();
    }

    @Override
    public void onItemCliked(int position) {}

    @Override
    public void onItemLongCliked(int position) {}

    public static class LibroViewHolder extends RecyclerView.ViewHolder {
        //ImageView image;  para caratula
        TextView title;
        TextView description;
        TextView autor;
        Button button_download;

        public LibroViewHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
            super(itemView);

            title = itemView.findViewById(R.id.nombre_libro);
            description = itemView.findViewById(R.id.description_libro);
            autor = itemView.findViewById(R.id.autor_libro);
            button_download = itemView.findViewById(R.id.btnDownloadBook);

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
