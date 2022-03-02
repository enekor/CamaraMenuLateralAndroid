package com.example.camaramenulateral.ui.slideshow;

import com.example.camaramenulateral.model.Foto;

import java.util.List;

public interface Conexion {
    void setAdapter(List<Foto> fotos);
    void onCLick(Foto foto);
}
