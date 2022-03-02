package com.example.camaramenulateral.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.camaramenulateral.model.Foto;

import java.util.List;

@Dao
public interface FotoDao {

    @Insert
    void insert(Foto foto);

    @Query("Select * from Foto")
    List<Foto> getAll();

    @Query("Select * from Foto where tag1=:tag or tag2=:tag or tag3=:tag")
    List<Foto> selectAllByTag(String tag);
}
