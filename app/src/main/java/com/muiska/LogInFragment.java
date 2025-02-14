package com.muiska;

import static android.app.Activity.RESULT_OK;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.credentials.CredentialManagerCallback;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.muiska.clases.User;
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.credentials.CredentialManager;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import android.os.CancellationSignal;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import java.util.Objects;

public class LogInFragment extends Fragment {
    private EditText tv1;
    private TextInputLayout tv2;
    private static final String TAG = "EmailPassword";
    //private GoogleSignInClient gsc;
    private final FirebaseFirestore db  = FirebaseFirestore.getInstance();
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
        Toast.makeText(getActivity(), "Funcionalidad en desarrollo", Toast.LENGTH_SHORT).show();

        /*
        CredentialManager credentialManager = CredentialManager.create(requireActivity());
        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(new GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        Executor executor = Executors.newSingleThreadExecutor();
        CancellationSignal cancellationSignal = new CancellationSignal();

        credentialManager.getCredentialAsync(
                requireActivity(), // Asegúrate de tener una referencia válida al contexto
                request,
                cancellationSignal,
                executor,
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        String googleIdToken = String.valueOf(result.getCredential().getData());
                        Toast.makeText(requireActivity(), googleIdToken, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(GetCredentialException error) {
                        // Manejar error
                    }
                }
        );


        Intent intent = gsc.getSignInIntent();
        gsc.signOut();
        activityResultLauncher.launch(intent);
        */
    }

    public void facebookSignIn(){
        Toast.makeText(getActivity(), "Funcionalidad no disponible", Toast.LENGTH_SHORT).show();
        /*TODO esperar a que facebook se le de la gana de volver a implementar las cuentas de prueba para poder probar el login con facebook

        LoginManager.getInstance().logInWithReadPermissions(AuthActivity.this, Collections.singletonList("email"));

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken token = loginResult.getAccessToken();

                AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
                authUser(credential);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(@NonNull FacebookException exception) {
                Toast.makeText(AuthActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
     */
    }
    /*
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    int result = o.getResultCode();
                    Intent data = o.getData();

                    //callbackManager.onActivityResult(1, result, data);  * para facebook *

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
*/
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
    }

    public void signUp() {
        usuario.replaceFragment(new RegisterFragment());
    }
}