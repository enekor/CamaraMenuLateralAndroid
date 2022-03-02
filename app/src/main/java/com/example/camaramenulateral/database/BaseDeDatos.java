package com.example.camaramenulateral.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.camaramenulateral.dao.FotoDao;
import com.example.camaramenulateral.dao.TagDao;
import com.example.camaramenulateral.model.Foto;
import com.example.camaramenulateral.model.Tag;
import org.jetbrains.annotations.NotNull;

@Database(entities = {Foto.class, Tag.class},version=2)
public abstract class BaseDeDatos extends RoomDatabase {

    private static final String DATABASENAME = "baseDatos";

    public abstract FotoDao fotoDao();
    public abstract TagDao tagDao();

    static final Migration MIGRATION_1_2 = new Migration(1,2){

        @Override
        public void migrate(@NonNull @NotNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Foto ADD gps_location_altitud real");
            database.execSQL("ALTER TABLE Foto ADD gps_location_longitud real");
        }
    };

    private static volatile BaseDeDatos instance;

    public synchronized static BaseDeDatos getInstance(final Context contexto){
        if(instance==null){
            instance = Room.databaseBuilder(contexto.getApplicationContext(),
                    BaseDeDatos.class,DATABASENAME).allowMainThreadQueries().addMigrations(MIGRATION_1_2).build();
        }
        return instance;
    }
}
