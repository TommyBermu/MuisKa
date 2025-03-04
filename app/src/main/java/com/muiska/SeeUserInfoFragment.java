package com.muiska;

import static org.chromium.base.ThreadUtils.runOnUiThread;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.muiska.clases.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SeeUserInfoFragment extends Fragment {
    FragmentActivity context;
    int usrId;
    TextView tv_name, tv_surname, tv_email, tv_name_madre, tv_surname_madre, tv_name_padre, tv_surname_padre, tv_cumpleanios, tv_cargo;
    User usuario;

    public SeeUserInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireActivity();
        usuario = ((MainActivity) requireActivity()).getUsuario();

        if (getArguments() != null) {
            usrId = getArguments().getInt("usrId");
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

        usuario.getExecutor().execute(() -> {
            String consulta = "SELECT * FROM Usuario WHERE idUsuario = ?";
            try (PreparedStatement verInfo = usuario.getConnection().prepareStatement(consulta)) {

                verInfo.setInt(1, usrId);

                ResultSet rs = verInfo.executeQuery();

                while (rs.next()) {
                    String nombre = rs.getString("Nombre");
                    String apellidos = rs.getString("Apellidos");
                    String email = rs.getString("Email");
                    String nombreMadre = rs.getString("nombreMadre");
                    String apellidosMadre = rs.getString("apellidosMadre");
                    String nombrePadre = rs.getString("nombrePadre");
                    String apellidosPadre = rs.getString("apellidosPadre");
                    String fechaNacimiento = rs.getDate("fechaNacimiento").toString();
                    String cargo = rs.getString("Cargo");

                    runOnUiThread(() -> {
                        tv_name.setText(nombre);
                        tv_surname.setText(apellidos);
                        tv_email.setText(email);
                        tv_name_madre.setText(nombreMadre);
                        tv_surname_madre.setText(apellidosMadre);
                        tv_name_padre.setText(nombrePadre);
                        tv_surname_padre.setText(apellidosPadre);
                        tv_cumpleanios.setText(fechaNacimiento);
                        tv_cargo.setText(cargo);
                    });
                }



            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}