package com.leon.biuvideo.values;

public enum OrderFollowType {
    ALL(0),         //所有
    WANT_SEE(1),    //想看
    LOOK_IN(2),     //在看
    SEEN(3);        //看过

    public int value;

    OrderFollowType(int value) {
        this.value = value;
    }
}
