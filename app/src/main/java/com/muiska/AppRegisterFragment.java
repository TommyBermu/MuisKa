package com.muiska;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.muiska.clases.User;

import java.util.Objects;

public class AppRegisterFragment extends Fragment {
    private EditText tv1, tv2;
    private Button setatributes;
    User usuario;

    public AppRegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usuario = ((AuthActivity) requireActivity()).getUsuario();

        tv1 = view.findViewById(R.id.nombre);
        tv2 = view.findViewById(R.id.apellido);

        setatributes = view.findViewById(R.id.button2);
        setatributes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String name = tv1.getText().toString();
                    String surname = tv2.getText().toString();
                    if (name.isEmpty() || surname.isEmpty())
                        throw new IllegalArgumentException("Requiere rellenar todos los campos");

                    //sale el cuadro de díalogo para seleccionar la relacion con la comunidad
                    createUser(name, surname, FirebaseAuth.getInstance().getCurrentUser().getEmail());

                } catch (IllegalArgumentException e) {
                    Log.w("EmailPassword", "createDocument: failure", e);
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void createUser(String name, String surname, String email) {
        //se muestra el cuadro de dialogo
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_auth, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        Button ok = dialogView.findViewById(R.id.btnOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario.createUser(name, surname, User.Cargo.COMUNERO, email);
                dialog.cancel();
            }
        });
    }
}