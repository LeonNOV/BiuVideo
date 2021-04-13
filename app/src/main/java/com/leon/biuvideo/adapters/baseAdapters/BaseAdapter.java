package com.leon.biuvideo.adapters.baseAdapters;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.otherFragments.biliUserFragments.BiliUserFragment;
import com.leon.biuvideo.ui.resourcesFragment.article.ArticleFragment;
import com.leon.biuvideo.ui.resourcesFragment.picture.PictureFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.VideoFragment;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.values.FragmentType;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * @Author Leon
 * @Time 2020/11/16
 * @Desc 基本的RecyclerViewAdapter为RecyclerView创建适配器时最好使用该抽象类进行创建ViewHolder为同包下的BaseViewHolder
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private final List<T> beans;
    public final Context context;
    public View view;
    protected ViewGroup parent;

    private final SupportActivity supportActivity;

    public BaseAdapter(List<T> beans, Context context) {
        this.beans = beans;
        this.context = context;

        if (context instanceof MainActivity) {
            Fuck.blue("is MainActivity");
        }

        this.supportActivity = (SupportActivity) context;
    }

    /**
     * 用于RecyclerView绑定item使用
     *
     * @param viewType  itemID
     * @return  返回itemID
     */
    public abstract int getLayout(int viewType);

    /**
     * 创建ViewHolder
     *
     * @return  返回BaseViewHolder
     */
    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        this.view = LayoutInflater.from(context).inflate(getLayout(viewType), parent, false);
        return new BaseViewHolder(view, context);
    }

    /**
     * 获取数据总数
     *
     * @return  返回数据总数
     */
    @Override
    public int getItemCount() {
        return beans == null ? 0 : beans.size();
    }

    /**
     * 重写该方法，防止数据出现错乱的问题
     *
     * @param position  position
     * @return  position
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 刷新加载数据
     *
     * @param addOns    要加入的数据
     */
    public void append (List<T> addOns) {
        this.beans.addAll(addOns);
        notifyDataSetChanged();
    }

    /**
     * 刷新加载数据
     *
     * @param addOn    要加入的数据
     */
    public void append (T addOn) {
        this.beans.add(addOn);
        notifyDataSetChanged();
    }

    /**
     * 根据其对象进行删除
     *
     * @param t     对象
     */
    public void remove(T t) {
        this.beans.remove(t);
        notifyDataSetChanged();
    }

    /**
     * 清空已存在的数据
     */
    public void removeAll() {
        if (this.beans != null) {
            this.beans.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * 启动一个“公共”Fragment
     *
     * @param fragmentType  FragmentType
     * @param params 参数
     */
    protected void startPublicFragment (FragmentType fragmentType, String params) {
        // 获取栈顶的SupportFragment
        SupportFragment topFragment = (SupportFragment) supportActivity.getTopFragment();
        switch (fragmentType) {
            case BILI_USER:
                topFragment.start(new BiliUserFragment(params));
                break;
            case VIDEO:
                topFragment.start(new VideoFragment(params));
                break;
            case AUDIO:

                break;
            case ARTICLE:
                topFragment.start(new ArticleFragment(params));
                break;
            case PICTURE:
                topFragment.start(new PictureFragment(params));
                break;
            default:
                break;
        }
    }
}
