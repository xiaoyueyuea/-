package com.yl.yymusic.storage.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "music")
public class MusicEntity{
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "music_id")
    private String musicId;
    @ColumnInfo(name="name_music")
    private String musicName;
    @ColumnInfo(name = "singer")
    private  String singer;
    @ColumnInfo(name = "times_play")
    private int playTimes;
    @ColumnInfo(name = "image_music")
    private byte[] musicImage;
    @ColumnInfo(name = "is_collect")
    private int isCollect;
    @ColumnInfo(name = "time_music")
    private int musicTime;
    @ColumnInfo(name = "recent_play_time")
    private  long recentTimePlay;
    @ColumnInfo(name = "url")
    private  String url;

    public MusicEntity(String musicId, String musicName, String singer, int playTimes, byte[] musicImage, int isCollect, int musicTime, long recentTimePlay, String url) {
        this.musicId = musicId;
        this.musicName = musicName;
        this.singer = singer;
        this.playTimes = playTimes;
        this.musicImage = musicImage;
        this.isCollect = isCollect;
        this.musicTime = musicTime;
        this.recentTimePlay = recentTimePlay;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMusicTime() {
        return musicTime;
    }

    public void setMusicTime(int musicTime) {
        this.musicTime = musicTime;
    }

    public long getRecentTimePlay() {
        return recentTimePlay;
    }

    public void setRecentTimePlay(long recentTimePlay) {
        this.recentTimePlay = recentTimePlay;
    }

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public int isCollect() {
        return isCollect;
    }

    public void setCollect(int collect) {
        isCollect = collect;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public int getPlayTimes() {
        return playTimes;
    }

    public void setPlayTimes(int playTimes) {
        this.playTimes = playTimes;
    }

    public byte[] getMusicImage() {
        return musicImage;
    }

    public void setMusicImage(byte[] musicImage) {
        this.musicImage = musicImage;
    }
}