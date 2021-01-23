package com.leon.biuvideo.values;

public enum SortType {
    DEFAULT("default"),//totalrank
    PUPDATE("pubdate"),
    SENDDATE("senddate"),
    ID("id"),
    RANKLEVEL("ranklevel"),
    CLICK("click"),
    SCORES("scores"),
    DAMKU("damku"),
    STOW("stow");//dm

    public String value;

    SortType(String value) {
        this.value = value;
    }
}
