package com.muiska;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.muiska.clases.Adapters.GroupAdapter;
import com.muiska.clases.Adapters.RecyclerViewClickListener;
import com.muiska.clases.Group;
import com.muiska.clases.User;

import java.util.ArrayList;

public class GroupSolicitudesFragment extends Fragment implements RecyclerViewClickListener {
    private ArrayList<Group> peticion;
    private GroupAdapter adapter;
    private String group;
    private User usuario;
    private FragmentActivity context;


    public GroupSolicitudesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_solicitudes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = requireActivity();
        usuario = ((MainActivity) context).getUsuario();

        if (getArguments() != null){
            group = getArguments().getString("grupo");
            Toast.makeText(context, group, Toast.LENGTH_SHORT).show();
        }

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewGroupSolicitudes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        peticion = new ArrayList<>();
        adapter = new GroupAdapter(peticion, this);
        recyclerView.setAdapter(adapter);

        /*
        root.child("requests-groups").child(group).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged") // solo hace que no se muestre un warning en en adapter.notifyDataSetChanged()
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    GenericTypeIndicator<HashMap<String, Object>> typeIndicator = new GenericTypeIndicator<HashMap<String, Object>>() {};
                    HashMap<String, Object> hashmap = dataSnapshot.getValue(typeIndicator);
                    assert hashmap != null : " hashmap es null en GroupSolicitudesFragment";
                    if (Objects.equals(hashmap.get("accepted"), null)) // TODO si se quiere filtrar por lo que ya están o por los que se rechazaron, se cambia entre true y false
                        peticion.add(hashmap);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

         */
    }

    @Override
    public void onItemCliked(int position) {
        Toast.makeText(context, "Esta petición no tiene archivos relacionados", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongCliked(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_goto, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        Button aceptar = dialogView.findViewById(R.id.confirmar);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                Bundle bundle = new Bundle();
                // bundle.putString("email", peticion.get(position).get("email").toString());
                usuario.replaceFragment(new SeeUserInfoFragment(), bundle);
            }
        });

        Button cancelar = dialogView.findViewById(R.id.cancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }
}