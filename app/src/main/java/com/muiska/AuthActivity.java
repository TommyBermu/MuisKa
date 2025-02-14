package com.muiska;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.muiska.clases.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthActivity extends AppCompatActivity {
    SharedPreferences prefs;
    FirebaseUser fUser;

    public User usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        prefs = getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser != null && fUser.isEmailVerified() && prefs.getString("email", null) != null) {
            Intent home = new Intent(this, MainActivity.class);
            startActivity(home);
        }

        //quita el SplashScreen y pone el de la app
        setTheme(R.style.Theme_MuisKa);
        usuario = new User(this);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragment_container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Window window = getWindow();
        // pone el Navigation Bar de color azul oscuro y el Status Bar de color beige
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.dark_blue));
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.light_beige));
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LogInFragment()).commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (fUser != null && fUser.isEmailVerified() && prefs.getString("email", null) != null) {
            Intent home = new Intent(this, MainActivity.class);
            startActivity(home);
        }
    }

    public User getUsuario(){
        return usuario;
    }

    public void createUser(String name, String surname, String email) {
        //se muestra el cuadro de dialogo
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_auth, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        Button ok = dialogView.findViewById(R.id.btnOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario.createUser(User.Cargo.COMUNERO, name, surname, email);
                dialog.cancel();
            }
        });
    }
}