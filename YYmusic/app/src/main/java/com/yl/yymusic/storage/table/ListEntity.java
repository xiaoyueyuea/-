package com.yl.yymusic.storage.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "list_entity")
public class ListEntity {
    @PrimaryKey(autoGenerate = true)
    private int list_id;
    @ColumnInfo(name = "list_name")
    private String listName;
    @ColumnInfo(name = "count_music")
    private int musicCount;

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public int getMusicCount() {
        return musicCount;
    }

    public void setMusicCount(int musicCount) {
        this.musicCount = musicCount;
    }

    public int getList_id() {
        return list_id;
    }

    public void setList_id(int list_id) {
        this.list_id = list_id;
    }

    public ListEntity(String listName, int musicCount) {
        this.listName = listName;
        this.musicCount = musicCount;
    }
}
