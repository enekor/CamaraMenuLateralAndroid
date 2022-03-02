package com.example.camaramenulateral.ui.vista;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.camaramenulateral.R;
import com.example.camaramenulateral.mapper.UriMapper;
import com.example.camaramenulateral.model.Foto;

public class Vista_imagen extends AppCompatActivity {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private TextView mParam1;
    private ImageView mParam2;
    private Button map;
    private Foto foto;
    private UriMapper mapper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_vista_imagen);

        initComponents();
        getData();
        setOnClick();
    }

    private void initComponents(){
        mapper = new UriMapper();
        mParam1 = findViewById(R.id.titulo_View);
        mParam2 = findViewById(R.id.fotoView);
        map = findViewById(R.id.mostrarPosicion);
    }

    private void getData(){
        Bundle bundle = getIntent().getExtras();
        foto = new Foto();
        foto.setTitulo(bundle.getString("titulo"));
        foto.setAltitud(bundle.getDouble("latitud"));
        foto.setLongitud(bundle.getDouble("longitud"));
        foto.setUri(bundle.getString("uri"));

        mParam1.setText(foto.getTitulo());
        mParam2.setImageURI(mapper.StringToUri(foto.getUri()));
    }

    private void setOnClick(){
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Vista_imagen.this, "latitud: "+String.valueOf(foto.getAltitud()+"longitud: "+String.valueOf(foto.getLongitud())), Toast.LENGTH_SHORT).show();
            }
        });
    }
}