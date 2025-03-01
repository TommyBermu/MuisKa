package com.muiska.clases;

import android.app.Activity;
import android.content.Context;
import android.credentials.GetCredentialException;
import android.os.CancellationSignal;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;

import androidx.annotation.NonNull;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.fragment.app.FragmentActivity;

import com.muiska.R;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GoogleSignInHelper {
    private static final String TAG = "GoogleSignInHelper";
    private final Context activity;
    private final CredentialManager credentialManager;
    private final Executor executor;

    public GoogleSignInHelper(Context activity, ActivityResultCallback<ActivityResult> callback) {
        this.activity = activity;
        this.credentialManager = CredentialManager.create(activity);
        this.executor = Executors.newSingleThreadExecutor();
    }
/*
    public void signInWithGoogle() {
        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(
                        new GetGoogleIdOption.Builder()
                                .setFilterByAuthorizedAccounts(true)
                                .setAutoSelectEnabled(true)
                                .setServerClientId(String.valueOf(R.string.default_web_client_id))
                                .setNonce("")
                                .build()
                ).build();
         TODO ver en el futuro a ver por que no funciona
        credentialManager.getCredentialAsync(
                activity,
                request,
                executor,
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse getCredentialResponse) {
                        handleSignInResponse(getCredentialResponse);
                    }

                    @Override
                    public void onError(@NonNull GetCredentialException e) {
                        Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

    private void handleSignInResponse(@NonNull GetCredentialResponse response) {
        Credential credential = response.getCredential();
        if ("google.com".equals(credential.getType())) {
            Log.d(TAG, "Inicio de sesión exitoso con Google: " + credential.getData());
            // Aquí puedes obtener el token para autenticar en tu backend si es necesario
        } else {
            Log.e(TAG, "Credencial no compatible.");
        }
    }*/
}
