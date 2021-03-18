package com.leon.biuvideo.adapters.userAdapters;

import com.leon.biuvideo.beans.orderBeans.LocalOrder;

public interface OnMusicListListener {
    /**
     * 刷新歌曲favorite
     *
     * @param localOrder   localOrder
     */
    void onRefreshFavoriteIcon(LocalOrder localOrder);

    /**
     * 切换歌曲
     *
     * @param sid   歌曲sid
     */
    void onSwitchMusic(String sid);
}
