package com.example.camaramenulateral.ui.slideshow;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.camaramenulateral.database.BaseDeDatos;
import com.example.camaramenulateral.model.Foto;
import com.example.camaramenulateral.model.Tag;
import java.util.List;

public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private List<Tag> tags;
    private List<Foto> fotos;
    private Conexion main;

    public SpinnerActivity(List<Tag> tags, Conexion main){
        this.main = main;
        this.tags = tags;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String tag = tags.get(i).getTag();
        fotos = BaseDeDatos.getInstance(this).fotoDao().selectAllByTag(tag);
        main.setAdapter(fotos);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
