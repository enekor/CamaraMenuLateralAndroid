package com.example.camaramenulateral.ui.slideshow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.camaramenulateral.MainActivity;
import com.example.camaramenulateral.R;
import com.example.camaramenulateral.database.BaseDeDatos;
import com.example.camaramenulateral.databinding.FragmentSlideshowBinding;
import com.example.camaramenulateral.model.Foto;
import com.example.camaramenulateral.ui.vista.Vista_imagen;

import java.util.List;
import java.util.Objects;

public class SlideshowFragment extends Fragment implements Conexion{

    private FragmentSlideshowBinding binding;
    private RecyclerView recycler;
    private Adaptador adapter;
    private LinearLayoutManager manager;
    private Spinner tagSpinner;
    private SpinnerActivity spinnerActivity;

    private List<Foto> fotos;
    private BaseDeDatos database;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recycler = root.findViewById(R.id.recycler);
        recycler.setLayoutManager(manager);
        tagSpinner = root.findViewById(R.id.tagSpinner);
        setSpinner();
        return root;
    }

    private void setSpinner() {
        ArrayAdapter adapter = new ArrayAdapter(Objects.requireNonNull(getActivity()).getApplicationContext(),
                android.R.layout.simple_spinner_item, database.tagDao().selectAll());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner.setAdapter(adapter);

        spinnerActivity = new SpinnerActivity(database.tagDao().selectAll(),this);
        tagSpinner.setOnItemSelectedListener(spinnerActivity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = BaseDeDatos.getInstance(Objects.requireNonNull(getActivity()).getApplicationContext());
        fotos = database.fotoDao().getAll();
        manager = new LinearLayoutManager(getActivity().getApplicationContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void toaster(String text){
        Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setAdapter(List<Foto> fotos){
        adapter = new Adaptador(fotos,this);
        recycler.setAdapter(adapter);
        Log.i("info","hola");
    }

    @Override
    public void onCLick(Foto foto) {
        Intent imagen = new Intent(this.getContext(), Vista_imagen.class);
        imagen.putExtra("titulo",foto.getTitulo());
        imagen.putExtra("latitud",foto.getAltitud());
        imagen.putExtra("longitud",foto.getLongitud());
        imagen.putExtra("uri",foto.getUri());
        imagen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().getApplicationContext().startActivity(imagen);
        //MainActivity.getInstance(getActivity()).startActivity(imagen);
    }
}