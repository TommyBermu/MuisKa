package com.muiska;

import static android.app.Activity.RESULT_OK;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.muiska.clases.Publicacion;
import com.google.android.gms.tasks.OnSuccessListener;
import com.muiska.clases.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CreatePublishFragment extends Fragment {
    private ImageButton imageButton;
    private Uri imageUri;
    private EditText title, description;
    private String end_date;
    private TextView show_date;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private User usuario;

    public CreatePublishFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_publish, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usuario = ((MainActivity) requireActivity()).getUsuario();

        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);

        imageButton = view.findViewById(R.id.imagePublication);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                activityResultLauncher.launch(gallery);
            }
        });

        Button publish = view.findViewById(R.id.publish);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null && !title.getText().toString().isEmpty() && !description.getText().toString().isEmpty() && end_date != null)
                    crearPublicacion(imageUri, title.getText().toString(), description.getText().toString(), end_date);
                else
                    Toast.makeText(getContext(), "Please select an image or fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });

        show_date = view.findViewById(R.id.date);
        show_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(requireActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat sdf_end = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                end_date = dayOfMonth + "/" + (month+1) + "/" + year;
                try {
                    if (sdf_end.parse(end_date).after(new Date())){
                        show_date.setText(end_date);
                    } else {
                        Toast.makeText(requireActivity(), "Seleccione una fecha a partir de mañana.", Toast.LENGTH_SHORT).show();
                        show_date.setText("");
                        end_date = null;
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    Intent data = o.getData();
                    if (o.getResultCode() == RESULT_OK && data != null) {
                        imageUri = data.getData();
                        imageButton.setImageURI(imageUri);
                    }
                }
            });

    private void crearPublicacion(Uri imageUri, String titulo, String descripcion, String endDate){
        usuario.getExecutor().execute(() -> {
            // TODO hay que crear para subir un anuncio porque solo se creo el metodo para subir una convocatoria
            String consulta = "CALL subirConvocatoria()";
            try (PreparedStatement subirPublicacion = usuario.getConnection().prepareStatement(consulta)) {

                //se agrega a la base de datos SQL
                subirPublicacion.setInt(1, usuario.getId()); // TODO editar para hacer la consulta xd. esq me tengo que ir D:

                int filasAfectadas = subirPublicacion.executeUpdate();

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