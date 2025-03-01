package com.muiska;

import android.app.Activity;
import android.content.Intent;
import android.credentials.Credential;
import android.credentials.GetCredentialRequest;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.credentials.CredentialManager;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
// import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.muiska.clases.GoogleSignInHelper;
import com.muiska.clases.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LogInFragment extends Fragment {
    private EditText tv1;
    private TextInputLayout tv2;
    private static final String TAG = "EmailPassword";
    //private GoogleSignInClient gsc;

    //private final CallbackManager callbackManager = CallbackManager.Factory.create(); * para facebook *
    private User usuario;

    public LogInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usuario = ((AuthActivity)requireActivity()).getUsuario();

        tv1 = view.findViewById(R.id.email);
        tv2 = view.findViewById(R.id.password_container);

        Button enter = view.findViewById(R.id.enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario.logIn(tv1.getText().toString(), tv2.getEditText().getText().toString());
            }
        });

        Button google = view.findViewById(R.id.googlesignin);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });

        Button facebook = view.findViewById(R.id.facebooksignin);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookSignIn();
            }
        });

        Button register = view.findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    public void googleSignIn(){
        Toast.makeText(getActivity(), "Funcionalidad no disponible", Toast.LENGTH_SHORT).show();
        /*
        GoogleSignInHelper googleSignInHelper = new GoogleSignInHelper(requireActivity(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Log.d(TAG, "Inicio de sesión exitoso");
                } else {
                    Log.e(TAG, "Error en el inicio de sesión");
                }
            }
        });

        googleSignInHelper.signInWithGoogle();
        */
    }

    public void facebookSignIn(){
        Toast.makeText(getActivity(), "Funcionalidad no disponible", Toast.LENGTH_SHORT).show();
        //TODO esperar a que facebook se le de la gana de volver a implementar las cuentas de prueba para poder probar el login con facebook
    }
    /*
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    int result = o.getResultCode();
                    Intent data = o.getData();

                    if (result == RESULT_OK){
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            if (account != null){
                                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                                authUser(credential);
                            }
                        } catch (Exception e){
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    private void authUser(AuthCredential credential){
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success
                    Log.d(TAG, "signInWithApp: success");
                    //ir a la google register activity si ho nay datos en la db
                    String Email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    db.collection("users").document(Email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                usuario.agregarPreferencias(documentSnapshot, Email);
                                Intent main = new Intent(getActivity(), MainActivity.class);
                                startActivity(main);
                            } else {
                                usuario.replaceFragment(new AppRegisterFragment());
                            }
                        }
                    });
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithApp: failure", task.getException());
                    Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/

    public void signUp() {
        usuario.replaceFragment(new RegisterFragment());
    }
}