package com.muiska.clases;

import static android.content.Context.MODE_PRIVATE;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.muiska.AuthActivity;
import com.muiska.MainActivity;
import com.muiska.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class User {
    // attributes
    private String nombre;
    private String apellidos;
    private Cargo cargo;
    private HashMap<String, Boolean> inscripciones;
    private HashMap<String, Boolean> grupos;
    private String carpeta;
    private Boolean completeInfo;
    private String email;

    //appInfo
    private FragmentActivity context;
    private FirebaseAuth mAuth;
    private FirebaseUser fUser;
    private SharedPreferences prefs;
    private FirebaseFirestore db;

    /**
     * empty constructor (required for Firestore)
     */
    public User(){}

    /**
     * constructor que se usa en las Activities
     * @param context contexto para ejecutar la mayoria de los metodos
     */
    public User(@NonNull FragmentActivity context){
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        prefs = context.getSharedPreferences(context.getString(R.string.prefs_file), MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();

        try {
            this.email = fUser.getEmail(); // si es null significa que está en la AuthActivity
            this.nombre = prefs.getString("name", "No hay datos");
            this.apellidos = prefs.getString("surname", "No hay datos");
            this.cargo = Cargo.valueOf(prefs.getString("cargo", "No hay datos"));
            this.completeInfo = prefs.getBoolean("completeInfo", false);
            this.carpeta = prefs.getString("carpeta", null);
            this.inscripciones = new HashMap<>();
            for (String s: prefs.getStringSet("inscripciones", new HashSet<>()))
                inscripciones.put(s, false); //TODO se coloca false por facilidad, pero toca ver si es true o false
            this.grupos = new HashMap<>();
            for (String s: prefs.getStringSet("grupos", new HashSet<>()))
                grupos.put(s, false);
        } catch (NullPointerException | IllegalArgumentException ignore){}
        this.context = context;
    }

    /**
     * Constructor que se envía a firebase
     * @param nombre nombre del usuario
     * @param apellidos apellidos del usuario
     * @param cargo cargo del usuario
     * @param inscripciones inscripciones del usuario
     * @param completeInfo si el usuario ha completado la información
     */
    public User(String nombre, String apellidos, Cargo cargo, HashMap<String, Boolean> inscripciones, HashMap<String, Boolean> grupos, String carpeta, Boolean completeInfo, String email) { //TODO si es externo no debería tener carpeta
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.cargo = cargo;
        this.inscripciones = inscripciones;
        this.grupos = grupos;
        this.carpeta = carpeta;
        this.completeInfo = completeInfo;
        this.email = email;
    }


    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public HashMap<String, Boolean> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(HashMap<String, Boolean> inscripciones) {
        this.inscripciones = inscripciones;
    }

    public void addInscripcion(String titulo){
        inscripciones.put(titulo, false);
    }

    public HashMap<String, Boolean> getGrupos() {
        return grupos;
    }

    public void setGrupos(HashMap<String, Boolean> grupos) {
        this.grupos = grupos;
    }

    public void addGrupo(String nombre) {
        grupos.put(nombre, false);
    }

    public String getCarpeta() {
        return carpeta;
    }

    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
    }

    public Boolean isCompleteInfo() {
        return completeInfo;
    }

    public void setCompleteInfo(Boolean completeInfo) {
        this.completeInfo = completeInfo;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public enum Cargo {
        EXTERNO,
        COMUNERO,
        ADMIN
    }

    public void inscribirse(){
        //TODO hacer que se inscriba en un grupo o en una convocatoria (debe pasar parámetros respectivos)
    }

    /**
     * Sube los datos del usuario en Firestore
     * @param cargo el cargo
     * @param name el nombre
     * @param surname el apellido
     * @param Email el email
     */
    public void createUser(Cargo cargo, String name, String surname, String Email) {
        final String TAG = "CreateUser:EmailPassword";
        db.collection("users").document(Email).set(new User(name, surname, cargo, new HashMap<>(), new HashMap<>(), null, false, Email)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "onSucces: docuemnto creado correctamente");
                    //enviar un código de verificación al email
                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                    logOut();
                } else {
                    fUser.delete();
                    Log.e(TAG, "onFailure, no se pudo crear el documento, se elimina el usuario : " + Objects.requireNonNull(task.getException()).getMessage());
                }
            }
        });
    }

    /**
     * Elimina la cuenta del usuario y su informacion en Firebase TODO (quitar de convocatorias y resto de informacion)
     * @param credential credencial para reautentificar el usuario
     */
    public void deleteUser(AuthCredential credential){
        fUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    fUser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            db.collection("users").document(email).delete();
                            Toast.makeText(context, "Cuenta eliminada", Toast.LENGTH_SHORT).show();
                            logOut();
                        }
                    });
                } else {
                    Toast.makeText(context, "Error al autenticar la cuenta", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Actualiza la información del usuario externo en Firestore
     * @param nombres nombres
     * @param apellidos apellidos
     */
    public void updateInfo(String nombres, String apellidos){
        DocumentReference docRef = db.collection("users").document(email);
        docRef.update("nombre", nombres);
        docRef.update("apellidos", apellidos);
        prefs.edit().putString("name", nombres).putString("surname", apellidos).apply();
        setNombre(nombres);
        setApellidos(apellidos);
    }

    /**
     * Actualiza la información del usuario comunero en Firestore
     * @param nombres nombres
     * @param apellidos apellidos
     * @param naneMadre nombre de la madre
     * @param surnameMadre apellido de la madre
     * @param namePadre nombre del padre
     * @param surnamePadre apellido del padre
     * @param fechaNacimiento fecha de nacimiento
     * @param sexo sexo
     * @param clan clan
     * @param prefesion profesion
     */
    public void updateInfo(String nombres, String apellidos, String naneMadre, String surnameMadre, String namePadre, String surnamePadre, String fechaNacimiento, String sexo, int clan, String prefesion){

        Map<String, Object> info = new HashMap<>();
        info.put("nombre Madre", naneMadre);
        info.put("apellidos Madre", surnameMadre);
        info.put("nombre Padre", namePadre);
        info.put("apellidos Padre", surnamePadre);
        info.put("fecha de nacimiento", fechaNacimiento);
        info.put("sexo", sexo);
        info.put("clan", clan);
        info.put("profesion", prefesion);

        if (completeInfo){
            db.collection("info_comunero").document(email).update(info).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    addInfo(info, nombres, apellidos);
                }
            });
        } else {
            db.collection("info_comunero").document(email).set(info).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    addInfo(info, nombres, apellidos);
                }
            });
        }
    }

    /**
     * Inicia sesión con email y contraseña
     * @param Email el email
     * @param Password la contraseña
     */
    public void logIn(@NonNull String Email, String Password){
        final String TAG = "LogIn:EmailPassword";

        if (Email.isEmpty() || Password.isEmpty()) {
            Log.w(TAG, "failure: requiere llenar todos lo campos");
            Toast.makeText(context, "requiere llenar todos lo campos", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                        //lo mismo, sale que puede ser NULL porque puede estar registrado con telefono, pero eso no está implementado.
                        Log.w(TAG, "Email is not verified");
                        Toast.makeText(context, "El correo no está verificado", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        return;
                    }
                    Log.d(TAG, "LogInWithEmailAndPassword: success");
                    //a veces no se conecta pero es por el android studio xd
                    db.collection("users").document(Email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                // si se recuperó el docuemto correctamente, agergar las preferencias e ir a la Main activity
                                Log.d(TAG, "onSuccess: added Preferences");
                                agregarPreferencias(task.getResult(), Email);
                                Intent main = new Intent(context, MainActivity.class);
                                context.startActivity(main);
                            }
                            else {
                                Log.e(TAG, "onFailure: Logging out", task.getException());
                                Toast.makeText(context, "Error al iniciar sesión, verifique su conexión a internet o intente nuevamente", Toast.LENGTH_LONG).show();
                                mAuth.signOut();
                            }
                        }
                    });
                } else {
                    Log.e(TAG, "signInWithEmail: failure", task.getException());
                    Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void ApplogIn(){ // TODO log in con cuentas externas (GOOGLE, Facebook)

    }

    /**
     * Cierra sesión
     */
    public void logOut(){
        final String TAG = "LogOut";

        prefs.edit().clear().apply();
        //LoginManager.getInstance().logOut(); TODO hablilitar cuando se haga lo de facebook
        FirebaseAuth.getInstance().signOut();
        Intent auth = new Intent(context, AuthActivity.class);
        context.startActivity(auth);
        Log.d(TAG, "LogOut: success");
    }

    /** ** ** ** ** ** ** *
     * Some minor methods *
     * ** ** ** * ** ** **/

    public void agregarPreferencias(@NonNull DocumentSnapshot document, String email) {
        User user = document.toObject(User.class);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        assert user != null: "Usuario nulo D:";

        prefsEditor.putString("name", user.getNombre());
        prefsEditor.putString("surname", user.getApellidos());
        prefsEditor.putString("cargo", user.getCargo().toString());
        prefsEditor.putStringSet("inscripciones", user.getInscripciones().keySet());
        prefsEditor.putStringSet("grupos", user.getGrupos().keySet());
        prefsEditor.putString("carpeta", user.getCarpeta());
        prefsEditor.putBoolean("completeInfo", user.isCompleteInfo());
        prefsEditor.putString("email", email);

        if (!user.getCargo().equals(Cargo.EXTERNO) && user.isCompleteInfo()){
            db.collection("info_comunero").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Map<String, Object> info = documentSnapshot.getData();
                    assert info != null;
                    for (String key : info.keySet()){
                        prefsEditor.putString(key, "" + info.get(key));
                    }
                    prefsEditor.apply();
                }
            });
        }
        prefsEditor.apply();
    }

    private void addInfo(@NonNull Map<String, Object> info, String nombres, String apellidos) {
        //edita el documento
        DocumentReference docRef = db.collection("users").document(email);
        docRef.update("nombre", nombres);
        docRef.update("apellidos", apellidos);
        //edita las preferencias
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("name", nombres).putString("surname", apellidos);
        for (String key : info.keySet()) {
            editor.putString(key, "" + info.get(key));
        }
        //edita el objeto
        setNombre(nombres);
        setApellidos(apellidos);
        if (!completeInfo){
            docRef.update("completeInfo", true);
            editor.putBoolean("completeInfo", true);
            setCompleteInfo(true);
        }
        editor.apply();
        Toast.makeText(context, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
    }

    public void replaceFragment(Fragment fragment){
        context.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    public void replaceFragment(Fragment fragment, Bundle bundle){
        fragment.setArguments(bundle);
        context.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @NonNull
    @Override
    public String toString() {
        return "nombre: " + this.nombre + ", apellidos: " + this.apellidos + ", cargo: " + this.cargo;
    }
}