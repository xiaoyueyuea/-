package com.yl.yymusic.storage.dao;

import com.yl.yymusic.storage.table.MusicEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MusicDao {
    @Insert
    void addMusic(MusicEntity musicEntity);

    @Query("select * from music where music_id = :music_id limit 1")
    boolean findMusic(String music_id);

    @Query("select * from music where music_id = :music_id limit 1")
    MusicEntity getEntityByMusicId(String music_id);

    @Query("select * from music limit 1")
    MusicEntity getFirstMusic();

    @Update
    void updateCollectMusic(MusicEntity musicEntities);

    @Query("select * from music where is_collect=1")
    List<MusicEntity> findCollectMusic();

    @Query("select is_collect from music where music_id = :music_id limit 1")
    int isCollect(String music_id);
}
