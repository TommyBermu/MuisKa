package com.muiska;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.muiska.clases.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    public User usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        Window window = getWindow();
        // quita el StatusBar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // pone el Navigation Bar de color azul oscuro
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.dark_blue));
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usuario = new User(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);

        if (usuario.getCargo().toString().equals(User.Cargo.ADMIN.toString())){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.admin_nav_menu);
        } else if (usuario.getCargo().toString().equals(User.Cargo.COMUNERO.toString())){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.nav_menu);
        } else if (usuario.getCargo().toString().equals(User.Cargo.PREFERENTE.toString())){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.pref_nav_menu);
        } else if (usuario.getCargo().toString().equals(User.Cargo.LIBRERO.toString())){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.lib_nav_menu);
        } else if (usuario.getCargo().toString().equals(User.Cargo.GESTOR_REDES.toString())){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.com_nav_menu);
        } else if (usuario.getCargo().toString().equals(User.Cargo.GESTOR_GRUPO.toString())){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.gges_nav_menu);
        } else if (usuario.getCargo().toString().equals(User.Cargo.CREAODR_GRUPOS.toString())){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.gcre_nav_menu);
        }

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_nav,R.string.close_nav);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment());
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.home_bottom_menu){
                replaceFragment(new HomeFragment());
            } else if (id == R.id.groups_bottom_menu){
                replaceFragment(new GroupsFragment());

            } else if (id == R.id.library_bottom_menu){
                replaceFragment(new LibraryFragment());

            } else if (id == R.id.profile_bottom_menu){
                replaceFragment(new ProfileComuFragment());

            }
            return true;
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.folder_change_menu && canAccess()) {
            replaceFragment(new ChangeFolderFragment());
        } else if (id == R.id.convs_requests_menu) {
            replaceFragment(new RequestConvFragment());
        } else if (id == R.id.folders_requests_menu) {
            replaceFragment(new RequestFolderChangeFragment());
        } else if (id == R.id.groups_requests_menu) {
            replaceFragment(new RequestGroupFragment());
        } else if (id == R.id.create_publication_menu) {
            replaceFragment(new CreatePublishFragment());
        } else if (id == R.id.create_group_menu) {
            replaceFragment(new CreateGroupFragment());
        } else if (id == R.id.add_book_menu) {
            replaceFragment(new CreateBookFragment());
        } else if (id == R.id.logout_menu) {
            usuario.logOut();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE);
        if (!(fUser != null && fUser.isEmailVerified() && prefs.getString("email", null) != null)){
            Intent auth = new Intent(this, AuthActivity.class);
            startActivity(auth);
        }
    }

    private boolean canAccess(){
        if (usuario.isCompleteInfo())
            return true;
        Toast.makeText(this, "Debe completar la información primero", Toast.LENGTH_SHORT).show();
        replaceFragment(new ProfileComuFragment());
        return false;
    }

    private void replaceFragment(Fragment fragment){
        usuario.replaceFragment(fragment);
    }

    public User getUsuario(){
        return usuario;
    }
}