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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class User {
    // attributes
    private int id, carpeta;
    private String nombre, apellidos, email;
    private Cargo cargo;
    private HashMap<String, Boolean> inscripciones;
    private HashMap<String, Boolean> grupos;
    private String profesion;

    //appInfo
    private FragmentActivity context;
    private FirebaseAuth mAuth;
    private SharedPreferences prefs;
    private Connection connection;
    private SQLConnection sqlConnection;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

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
        prefs = context.getSharedPreferences(context.getString(R.string.prefs_file), MODE_PRIVATE);

        sqlConnection = new SQLConnection();
        executor.execute(() -> {
            connection = sqlConnection.conectar();
            if (connection == null){
                Toast.makeText(context, "Error al conectar, intenta más tarde", Toast.LENGTH_SHORT).show();
                logOut();
            }
        });

        try {
            this.email = prefs.getString("email", "No hay datos"); // si es null significa que está en la AuthActivity
            this.nombre = prefs.getString("name", "No hay datos");
            this.apellidos = prefs.getString("surname", "No hay datos");
            this.cargo = Cargo.valueOf(prefs.getString("cargo", "No hay datos"));
            this.carpeta = prefs.getInt("carpeta", 0);

            // el true o false es si fue aceptado o no (creo) xd
            this.inscripciones = new HashMap<>();
            for (String s: prefs.getStringSet("inscripciones", new HashSet<>()))
                inscripciones.put(s, false); //TODO se coloca false por facilidad, pero toca ver si es true o false

            this.grupos = new HashMap<>();
            for (String s: prefs.getStringSet("grupos", new HashSet<>()))
                grupos.put(s, false);

        } catch (NullPointerException | IllegalArgumentException ignore){}
        this.context = context;
    }

    /*

    /**
     * Constructor que se envía a firebase
     * @param nombre nombre del usuario
     * @param apellidos apellidos del usuario
     * @param cargo cargo del usuario
     * @param inscripciones inscripciones del usuario
     * @param completeInfo si el usuario ha completado la información
     /*
    public User(String nombre, String apellidos, Cargo cargo, HashMap<String, Boolean> inscripciones, HashMap<String, Boolean> grupos, int carpeta, Boolean completeInfo, String email) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.cargo = cargo;
        this.inscripciones = inscripciones;
        this.grupos = grupos;
        this.carpeta = carpeta;
        this.completeInfo = completeInfo;
        this.email = email;
    }
    */

    public Connection getConnection(){
        return connection;
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

    public int getCarpeta() {
        return carpeta;
    }

    public void setCarpeta(int carpeta) {
        this.carpeta = carpeta;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public boolean isCompleteInfo(){
        return getProfesion() != null;
    }

    public enum Cargo {
        COMUNERO,
        ADMIN,
        PREFERENTE,
        LIBRERO,
        GESTOR_REDES,
        GESTOR_GRUPO,
        CREAODR_GRUPOS
    }

    public void inscribirse(){
        //TODO hacer que se inscriba en un grupo o en una convocatoria (debe pasar parámetros respectivos)
    }

    /**
     * Sube los datos del usuario en Firestore
     * @param cargo el cargo
     * @param name el nombre
     * @param surname el apellido
     * @param email el email
     */
    public void createUser(String name, String surname, Cargo cargo, String email) {
        final String TAG = "CreateUser:EmailPassword";
        executor.execute(()-> {
            String consulta = "INSERT INTO Usuario (Nombre, Apellidos, Cargo, Email, nombrePadre, apellidosPadre, nombreMadre, apellidosMadre, fechaNacimiento, Profesion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try {
                // Preparamos la actualización del registro

                PreparedStatement crearUsuario = connection.prepareStatement(consulta);
                crearUsuario.setString(1, name);
                crearUsuario.setString(2, surname);
                crearUsuario.setString(3, cargo.toString());
                crearUsuario.setString(4, email);
                crearUsuario.setString(5, null);
                crearUsuario.setString(6, null);
                crearUsuario.setString(7, null);
                crearUsuario.setString(8, null);
                crearUsuario.setString(9, null);
                crearUsuario.setString(10, null);

                int filasAfectadas = crearUsuario.executeUpdate();
                Log.i(TAG, "Usuario Creado");
                //enviar un código de verificación al email
                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
            } catch (SQLException ex) {
                Log.e("CONSULTA", "Imposible realizar consulta '"+ consulta +"' ... FAIL");
                ex.printStackTrace();
            }
        });
    }

    /**
     * Elimina la cuenta del usuario y su informacion en Firebase TODO (quitar de convocatorias y resto de informacion)
     * @param credential credencial para reautentificar el usuario
     */
    public void deleteUser(AuthCredential credential){
        Objects.requireNonNull(mAuth.getCurrentUser()).reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    mAuth.getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            executor.execute(()-> {
                                String consulta = "DELETE FROM Usuario WHERE Email = ?";
                                try {
                                    // Preparamos la actualización del registro

                                    PreparedStatement delUsuario = connection.prepareStatement(consulta);
                                    delUsuario.setString(1, mAuth.getCurrentUser().getEmail());

                                    int filasAfectadas = delUsuario.executeUpdate();
                                } catch (SQLException ex) {
                                    Log.e("CONSULTA", "Imposible realizar consulta '"+ consulta +"' ... FAIL");
                                    ex.printStackTrace();
                                }
                            });

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
     * Actualiza la información del usuario comunero en Firestore
     * @param nombres nombres
     * @param apellidos apellidos
     * @param nameMadre nombre de la madre
     * @param surnameMadre apellido de la madre
     * @param namePadre nombre del padre
     * @param surnamePadre apellido del padre
     * @param fechaNacimiento fecha de nacimiento
     * @param prefesion profesion
     */
    public void updateInfo(String nombres, String apellidos, String nameMadre, String surnameMadre, String namePadre, String surnamePadre, String fechaNacimiento, String prefesion){
        executor.execute(()-> {
            String consulta = "UPDATE Usuario SET Nombre = ?, SET Apellidos = ?, SET nombrePadre = ?, SET apellidosPadre = ?, SET nombreMadre = ?, SET apellidosMadre = ?, SET fechaNacimiento = ?, SET Profesion = ? WHERE Email = ?";
            try {
                // Preparamos la actualización del registro
                PreparedStatement actUsuario = connection.prepareStatement(consulta);
                actUsuario.setString(1, nombres);
                actUsuario.setString(2, apellidos);
                actUsuario.setString(3, namePadre);
                actUsuario.setString(4, surnamePadre);
                actUsuario.setString(5, nameMadre);
                actUsuario.setString(6, surnameMadre);
                actUsuario.setString(7, fechaNacimiento);
                actUsuario.setString(8, prefesion);
                actUsuario.setString(9, mAuth.getCurrentUser().getEmail());

                int filasAfectadas = actUsuario.executeUpdate();
            } catch (SQLException ex) {
                Log.e("CONSULTA", "Imposible realizar consulta '"+ consulta +"' ... FAIL");
                ex.printStackTrace();
            }
        });
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

        // se desconecta porq en la otra activity se va a volver a conectar
        sqlConnection.desconectar();
        executor.shutdown();

        mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                        //lo mismo, sale que puede ser NULL porque puede estar registrado con telefono, pero eso no está implementado.
                        Log.w(TAG, "Email is not verified");
                        Toast.makeText(context, "El correo no está verificado", Toast.LENGTH_SHORT).show();
                        logOut();
                        return;
                    }
                    Log.d(TAG, "LogInWithEmailAndPassword: success");


                    executor.execute(()-> {
                        String consulta = "SELECT * FROM Usuario WHERE Email like ?";
                        try {
                            // Preparamos la actualización del registro
                            PreparedStatement getUsuario = connection.prepareStatement(consulta);
                            getUsuario.setString(1, mAuth.getCurrentUser().getEmail());

                            ResultSet rs = getUsuario.executeQuery(consulta);
                            if(rs.next()) {
                                agregarPreferencias(rs);
                                Log.d(TAG, "onSuccess: added Preferences");
                                context.startActivity(new Intent(context, MainActivity.class));
                            } else {
                                Log.i("RESULTADO", "No hay resultados en la consulta");
                            }

                        } catch (SQLException ex) {
                            Log.e("CONSULTA", "Imposible realizar consulta '"+ consulta +"' ... FAIL");
                            ex.printStackTrace();
                        }
                    });
                } else {
                    Log.e(TAG, "signInWithEmail: failure", task.getException());
                    Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Some minor methods
      **/

    public void agregarPreferencias(@NonNull ResultSet rs) throws SQLException {

        SharedPreferences.Editor prefsEditor = prefs.edit();

        prefsEditor.putString("name", rs.getString("Nombre"));
        prefsEditor.putString("surname", rs.getString("Apellidos"));
        prefsEditor.putString("cargo", rs.getString("Cargo"));
        prefsEditor.putString("email", Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
        prefsEditor.putInt("carpeta", rs.getInt("Carpeta_idCarpeta"));
        prefsEditor.putBoolean("completeInfo", isCompleteInfo());

        /* TODO para lueguito (se tiene que colocar las tablas muchos a muchos en las preferencias) :d
        prefsEditor.putStringSet("inscripciones", user.getInscripciones().keySet());
        prefsEditor.putStringSet("grupos", user.getGrupos().keySet());

        if (user.isCompleteInfo()){
            dv.collection("info_comunero").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
        }*/
        prefsEditor.apply();
    }

    /**
     * Cierra sesión
     */
    public void logOut(){
        sqlConnection.desconectar();
        executor.shutdown();
        prefs.edit().clear().apply();
        mAuth.signOut();
        context.startActivity(new Intent(context, AuthActivity.class));
        Log.d("LOGOUT", "LogOut: success");
    }

    public void replaceFragment(Fragment fragment){
        context.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    public void replaceFragment(@NonNull Fragment fragment, Bundle bundle){
        fragment.setArguments(bundle);
        context.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @NonNull
    @Override
    public String toString() {
        return "nombre: " + this.nombre + ", apellidos: " + this.apellidos + ", cargo: " + this.cargo;
    }
}