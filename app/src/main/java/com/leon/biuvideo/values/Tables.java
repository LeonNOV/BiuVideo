package com.leon.biuvideo.values;

public enum  Tables {
    FavoriteUp("favoriteUp"),

    LocalVideoFolders("localVideoFolders"),
    LocalOrders("localOrders"),

    // 已下载资源文件夹
    DownloadRecordsForVideo("downloadRecordsForVideo"),

    // 已下载资源具体信息
    DownloadDetailsForVideo("downloadDetailsForVideo");

    public String value;

    Tables(String value) {
        this.value = value;
    }
}
