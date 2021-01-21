package com.leon.biuvideo.values;

public enum  Tables {
    FavoriteUp("favoriteUp"),

    LocalVideoFolders("localVideoFolders"),
    LocalOrders("localOrders"),

    DownloadRecordsForVideo("downloadRecordsForVideo"),
    DownloadDetailsForVideo("downloadDetailsForVideo");

    public String value;

    Tables(String value) {
        this.value = value;
    }
}
