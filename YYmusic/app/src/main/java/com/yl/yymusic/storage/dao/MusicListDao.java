package com.yl.yymusic.storage.dao;

import com.yl.yymusic.storage.table.ListEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MusicListDao{

    @Insert
    void addMusicList(ListEntity listEntity);
    @Query("select * from list_entity")
    List<ListEntity> getAllMusicList();
}
