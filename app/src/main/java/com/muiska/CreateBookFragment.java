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

import android.util.Log;
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
import com.muiska.clases.User;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateBookFragment extends Fragment {
    private ImageButton imageButton;
    private EditText title, description;
    private User usuario;

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
        usuario = ((MainActivity) requireActivity()).getUsuario();

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
                /*
                if (pdfUri != null && !title.getText().toString().isEmpty() && !description.getText().toString().isEmpty())
                    uploadTofirebase(pdfUri);
                else
                    Toast.makeText(getContext(), "Please select a file or fill all the fields", Toast.LENGTH_SHORT).show();

                 */
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
                        // pdfUri = data.getData();
                        imageButton.setImageResource(R.drawable.baseline_check_circle_24);  //setImageURI(pdfUri)
                        Toast.makeText(getContext(), "Archivo seleccionado.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private void upload(){
        usuario.getExecutor().execute(() -> {
            String consulta = "INSERT INTO Libro () VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement subirLibro = usuario.getConnection().prepareStatement(consulta)) {

                //se agrega a la base de datos SQL
                subirLibro.setInt(1, usuario.getId()); // TODO editar para hacer la consulta xd. esq me tengo que ir D:

                // int filasAfectadas = subirPublicacion.executeUpdate();

                Toast.makeText(getContext(), "Publicación creada", Toast.LENGTH_SHORT).show();
                imageButton.setImageResource(R.drawable.baseline_add_photo_alternate_270_p);
                title.setText("");
                description.setText("");
                Log.i("CONSULTA", "la consutla se realizo con exito");

            } catch (SQLException ex) {
                Log.e("CONSULTA", "Imposible realizar consulta '"+ consulta +"' ... FAIL");
                ex.printStackTrace();
            }
        });
    }
}