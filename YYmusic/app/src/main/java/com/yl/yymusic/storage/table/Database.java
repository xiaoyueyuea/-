package com.yl.yymusic.storage.table;
import android.content.Context;
import com.yl.yymusic.storage.dao.MusicDao;
import com.yl.yymusic.storage.dao.MusicListDao;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {MusicEntity.class,ListEntity.class},version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {
    private static Database INSTANCE;

    private static final String DB_NAME = "musicDatabase";

    private static final Object sLock = new Object();

    public static Database getInstance() {
        return INSTANCE;
    }

    public static void initialize(Context context) {
        if (INSTANCE == null) {
            synchronized (sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class, DB_NAME)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
    }

    public abstract MusicDao getMusicDao();

    public abstract MusicListDao getMusicListDao();
}
