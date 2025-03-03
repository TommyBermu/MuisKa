package com.muiska;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SeeUserInfoFragment extends Fragment {
    FragmentActivity context;
    String Email;
    TextView tv_name, tv_surname, tv_email, tv_name_madre, tv_surname_madre, tv_name_padre, tv_surname_padre, tv_cumpleanios, tv_sexo, tv_clan, tv_cargo;

    public SeeUserInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireActivity();

        if (getArguments() != null) {
            Email = getArguments().getString("email");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_see_user_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_name = view.findViewById(R.id.namevisualizer);
        tv_surname = view.findViewById(R.id.surnamevisualizer);
        tv_email = view.findViewById(R.id.emailvisualizer);
        tv_name_madre = view.findViewById(R.id.nameMother);
        tv_surname_madre = view.findViewById(R.id.surnameMother);
        tv_name_padre = view.findViewById(R.id.nameFather);
        tv_surname_padre = view.findViewById(R.id.surnameFather);
        tv_cumpleanios = view.findViewById(R.id.birthdayDate);
        tv_cargo = view.findViewById(R.id.profession);


        /* TODO la info se saca con el Email :D (ez)
        db.collection("users").document(Email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User comunero = documentSnapshot.toObject(User.class);
                assert comunero != null: "Comuero es null en SeeUserInfoFragment";

                tv_name.setText(comunero.getNombre());
                tv_surname.setText(comunero.getApellidos());
                tv_email.setText(comunero.getEmail());

                db.collection("info_comunero").document(Email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        tv_name_madre.setText(documentSnapshot.getString("nombre Madre"));
                        tv_surname_madre.setText(documentSnapshot.getString("apellidos Madre"));
                        tv_name_padre.setText(documentSnapshot.getString("nombre Padre"));
                        tv_surname_padre.setText(documentSnapshot.getString("apellidos Padre"));
                        tv_cumpleanios.setText(documentSnapshot.getString("fecha de nacimiento"));
                        tv_sexo.setText(documentSnapshot.getString("sexo"));
                        tv_clan.setText("" + documentSnapshot.getDouble("clan")); // TODO pasar de numero entero a clan con la lista que esta en el firebase
                        tv_cargo.setText(documentSnapshot.getString("profesion"));
                    }
                });
            }
        });

         */
    }
}