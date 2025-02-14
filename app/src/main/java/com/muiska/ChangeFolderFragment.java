package com.muiska;

import static android.app.Activity.RESULT_OK;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.muiska.clases.User;
import com.muiska.clases.FolderChange;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ChangeFolderFragment extends Fragment {
    private User usuario;

    EditText editText, editText2, editText3;
    Button btn, btnDialog;
    AlertDialog dialog;

    StorageReference storageReference = FirebaseStorage.getInstance().getReference("requests-folder_change");
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("requests-folder_change");

    private Uri selectedPdfUri1;
    private Uri selectedPdfUri2;
    private int activeEditText = 0;

    public ChangeFolderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_folder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Llamar a la info de la persona
        usuario = ((MainActivity) requireActivity()).getUsuario();

        editText = view.findViewById(R.id.etSelectFile);
        editText2 = view.findViewById(R.id.etSelectFileLetter);
        editText3 = view.findViewById(R.id.folder_to_change);
        btn = view.findViewById(R.id.btnSendFile);
        btn.setEnabled(false);

        //Llama al formato de dialog_succes
        View alertCustomDialog = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_succes, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setView(alertCustomDialog);

        dialog = alertDialog.create();

        btnDialog = alertCustomDialog.findViewById(R.id.btnEntendido);
        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                usuario.replaceFragment(new HomeFragment());
            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeEditText = 1;
                selectPDF();
            }
        });

        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeEditText = 2;
                selectPDF();
            }
        });
    }

    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(intent, "PDF FILE SELECT"));

    }


    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedPdfUri = result.getData().getData();
                    // Manejar el URI del archivo PDF seleccionado
                    if (activeEditText == 1) {
                        selectedPdfUri1 = selectedPdfUri;
                        editText.setText(selectedPdfUri.getLastPathSegment());
                    } else if (activeEditText == 2) {
                        selectedPdfUri2 = selectedPdfUri;
                        editText2.setText(selectedPdfUri.getLastPathSegment());
                    }

                    btn.setEnabled(true);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //uploadPDFFileFirebase(result.getData().getData());
                            if (selectedPdfUri1 != null && selectedPdfUri2 != null) {
                                uploadPDFFile(selectedPdfUri1, selectedPdfUri2);

                                //muestra el dialogo de exito
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.show();
                            } else {
                                Toast.makeText(getActivity(), "Please select both files before uploading.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
    );

    private void uploadPDFFile(Uri data1, Uri data2) {
        storageReference.child("PDF" + System.currentTimeMillis() + ".pdf").putFile(data1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask1 = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask1.isComplete()); // Espera a que la URL esté disponible

                String path = databaseReference.push().getKey();
                assert path != null : "Path is null";
                storageReference.child("PDF" + System.currentTimeMillis() + ".pdf").putFile(data2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask2 = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask2.isComplete()); // Espera a que la URL esté disponible

                        String path = databaseReference.push().getKey();
                        assert path != null : "Path is null";

                        databaseReference.child(path).setValue(new FolderChange(
                                usuario.getNombre() + " " + usuario.getApellidos(),
                                usuario.getEmail(),
                                uriTask1.getResult().toString(),
                                uriTask2.getResult().toString(),
                                path,
                                editText3.getText().toString()));
                    }
                });
            }
        });
    }
}