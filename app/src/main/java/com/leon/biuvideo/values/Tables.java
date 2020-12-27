package com.leon.biuvideo.values;

public enum  Tables {
    FavoriteUp("favorite_up"),
    MusicPlayList("musicPlayList"),
    VideoPlayList("videoPlayList"),
    Article("article"),

    DownloadRecordsForVideo("downloadRecordsForVideo"),
    DownloadDetailsForVideo("downloadDetailsForVideo");

    public String value;

    Tables(String value) {
        this.value = value;
    }
}
