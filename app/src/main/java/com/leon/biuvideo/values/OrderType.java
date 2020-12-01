package com.leon.biuvideo.values;

public enum OrderType {
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

    OrderType(String value) {
        this.value = value;
    }
}
