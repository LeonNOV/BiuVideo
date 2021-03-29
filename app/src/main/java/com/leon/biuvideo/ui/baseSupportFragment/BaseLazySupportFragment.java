package com.leon.biuvideo.ui.baseSupportFragment;

/**
 * @Author Leon
 * @Time 2021/3/26
 * @Desc 懒加载Fragment，需要配合ViewPager2使用
 */
public abstract class BaseLazySupportFragment extends BaseSupportFragment {
    /**
     * 数据读取标记
     */
    private boolean isLoaded = false;

    @Override
    public void onResume() {
        super.onResume();
        if (!isLoaded) {
            onLazyLoad();
            isLoaded = true;
        }
    }

    /**
     * 加载数据
     */
    protected abstract void onLazyLoad ();
}