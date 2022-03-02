package com.example.camaramenulateral.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Tag {

   @PrimaryKey(autoGenerate = true) private long id;
   private String tag;

   public String getTag() {
      return tag;
   }

   public void setTag(String tag) {
      this.tag = tag;
   }

   public long getId() {
      return id;
   }

   public void setId(long id) {
      this.id = id;
   }

   @Override
   public String toString() {
      return tag;
   }
}
