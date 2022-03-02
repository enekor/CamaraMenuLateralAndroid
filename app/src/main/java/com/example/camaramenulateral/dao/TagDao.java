package com.example.camaramenulateral.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.camaramenulateral.model.Tag;

import java.util.List;

@Dao
public interface TagDao {

    @Insert
    void insertTag(Tag tag);

    @Query("Select * from Tag")
    List<Tag> selectAll();

    @Query("Select * from Tag where tag=:tag")
    List<Tag> selectByTag(String tag);
}
