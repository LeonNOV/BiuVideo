package com.leon.biuvideo.adapters.baseAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.utils.Fuck;

import java.util.List;

/**
 * 基本的RecyclerViewAdapter
 * 为RecyclerView创建适配器时最好使用该抽象类进行创建
 * ViewHolder为同包下的BaseViewHolder
 *
 * @param <T>   实体类型
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private final List<T> beans;
    private final Context context;
    protected ViewGroup parent;

    public BaseAdapter(List<T> beans, Context context) {
        this.beans = beans;
        this.context = context;
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
        View view = LayoutInflater.from(context).inflate(getLayout(viewType), parent, false);
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
     * 根据索引进行删除
     *
     * @param index 索引值
     */
    public void remove(int index) {
        this.beans.remove(index);
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
     * 刷新方法,使用时，必须对其进行重写
     */
    public void refresh(String str) {

    }
}
