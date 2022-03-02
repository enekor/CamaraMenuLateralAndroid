package com.example.camaramenulateral.mapper;

import android.net.Uri;
import androidx.room.TypeConverter;

public class UriMapper {

    @TypeConverter
    public String UriToString(Uri uri){
        return uri.toString();
    }

    @TypeConverter
    public Uri StringToUri(String string){
        return string==null?null:Uri.parse(string);
    }
}
