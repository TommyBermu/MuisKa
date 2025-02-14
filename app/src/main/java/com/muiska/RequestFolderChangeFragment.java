package com.muiska;

import static com.muiska.clases.FileDownloader.REQUEST_CODE;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.muiska.clases.Adapters.FolderChangeAdapter;
import com.muiska.clases.FileDownloader;
import com.muiska.clases.FolderChange;
import com.muiska.clases.Adapters.RecyclerViewClickListener;
import com.muiska.clases.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class RequestFolderChangeFragment extends Fragment implements RecyclerViewClickListener {
    private ArrayList<FolderChange> cambios;
    private FolderChangeAdapter adapter;
    private User usuario;
    private FragmentActivity context;

    private final DatabaseReference root = FirebaseDatabase.getInstance().getReference();

    public RequestFolderChangeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_request_folder_change, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = requireActivity();
        usuario = ((MainActivity) context).getUsuario();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewCarpeta);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        cambios = new ArrayList<>();
        adapter = new FolderChangeAdapter(cambios, context, this);
        recyclerView.setAdapter(adapter);

        root.child("requests-folder_change").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged") // solo hace que no se muestre un warning en en adapter.notifyDataSetChanged()
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    FolderChange change = dataSnapshot.getValue(FolderChange.class);
                    cambios.add(change);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public void onItemCliked(int position) {
        Toast.makeText(context, "Descargando Documento y Carta...", Toast.LENGTH_SHORT).show();
        FolderChange fc = cambios.get(position);

        FileDownloader fileDownloader = new FileDownloader();

        if (ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

        } else {
            fileDownloader.downloadFile(requireActivity(), fc.getDocument_url(), "Documento " + fc.getName());
            fileDownloader.downloadFile(requireActivity(), fc.getLetter_url(), "Carta " + fc.getName());
            Toast.makeText(requireActivity(), "La descarga de los archivos ha finalizado", Toast.LENGTH_LONG).show();
        }
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
                bundle.putString("email", cambios.get(position).getEmail());
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
}