package com.leon.biuvideo.utils.dataBaseUtils;

public enum  Tables {
    FavoriteUp("favorite_up"),
    MusicPlayList("musicPlayList"),
    VideoPlayList("videoPlayList");

    String value;

    Tables(String value) {
        this.value = value;
    }
}