package com.example.camaramenulateral.ui.slideshow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.camaramenulateral.R;
import com.example.camaramenulateral.mapper.UriMapper;
import com.example.camaramenulateral.model.Foto;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder> {

    private List<Foto> fotos;
    private UriMapper mapper = new UriMapper();
    private Conexion conexion;

    public Adaptador(List<Foto> fotos,Conexion conexion){
        this.fotos = fotos;
        this.conexion = conexion;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.preview,parent,false);
        return new ViewHolder(view,fotos,conexion);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titulo.setText(fotos.get(position).getTitulo());
    }

    @Override
    public int getItemCount() {
        return fotos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView titulo;
        private List<Foto> fotos;
        private Conexion conexion;
        private ConstraintLayout layout;

        public ViewHolder(@NonNull View view,List<Foto> fotos, Conexion conexion) {
            super(view);
            titulo = view.findViewById(R.id.tituloPreview);
            layout = view.findViewById(R.id.layoutPreview);
            onClick();
            this.conexion = conexion;
            this.fotos = fotos;
        }

        private void onClick() {
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int posicion = getAdapterPosition();
                    conexion.onCLick(fotos.get(posicion));
                }
            });
        }
    }
}
