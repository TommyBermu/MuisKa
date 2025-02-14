package com.muiska;

import static android.app.Activity.RESULT_OK;


import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.muiska.clases.Libro;
import com.muiska.clases.Publicacion;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateBookFragment extends Fragment {
    private ImageButton imageButton; //para colocar un chulito verde despues de seleccionar el archivo
    private Uri pdfUri;
    private EditText title, description;

    private final DatabaseReference root = FirebaseDatabase.getInstance().getReference("library");
    private final StorageReference reference = FirebaseStorage.getInstance().getReference("library");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);

        imageButton = view.findViewById(R.id.imagePublication);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent files_selector = new Intent();
                files_selector.setAction(Intent.ACTION_GET_CONTENT);
                files_selector.setType("application/pdf");
                activityResultLauncher.launch(files_selector);
            }
        });

        Button uploadbook = view.findViewById(R.id.publish);
        uploadbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pdfUri != null && !title.getText().toString().isEmpty() && !description.getText().toString().isEmpty())
                    uploadTofirebase(pdfUri);
                else
                    Toast.makeText(getContext(), "Please select a file or fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult onr) {
                    Intent data = onr.getData();
                    if (onr.getResultCode() == RESULT_OK && data != null) {
                        pdfUri = data.getData();
                        imageButton.setImageResource(R.drawable.baseline_check_circle_24);  //setImageURI(pdfUri)
                        Toast.makeText(getContext(), "Archivo seleccionado.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private void uploadTofirebase(Uri pdfUri){
        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(pdfUri));
        fileRef.putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String modelId = root.push().getKey();
                        assert modelId != null;

                        root.child(modelId).setValue(new Libro(
                                title.getText().toString(),
                                uri.toString(),
                                description.getText().toString())).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "Libro añadido a la Biblioteca", Toast.LENGTH_SHORT).show();
                                imageButton.setImageResource(R.drawable.baseline_cloud_upload_24);
                                title.setText("");
                                description.setText("");
                            }
                        });
                    }
                });
            }
        });
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = requireActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}