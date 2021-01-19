package com.leon.biuvideo.adapters.baseAdapters;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.HashMap;
import java.util.Map;

/**
 * 基本的ViewHolder
 * 该类提供了一些常用的绑定数据方法，可自行根据需要添加
 * <br/>
 * <strong>注意：设置点击事件时请注意该类中两个不同参数的setOnClickListener()方法，具体使用可看方法的注释</strong>
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
    private final Map<Integer, View> views;
    private final Context context;

    public BaseViewHolder(@NonNull View itemView, Context context) {
        super(itemView);

        this.views = new HashMap<>();
        this.context = context;
    }

    /**
     * 通过ID找到对应的控件
     *
     * @param id    控件ID
     * @param <T>   控件
     * @return  返回控件
     */
    public <T extends View> T findById(int id) {
        View view = views.get(id);

        if (view == null) {
            view = itemView.findViewById(id);
            views.put(id, view);
        }

        return (T) view;
    }

    /**
     * 用于设置控件的text
     *
     * @param id    控件ID
     * @param text  设置的文本
     * @return  返回this
     */
    public BaseViewHolder setText(int id, String text) {
        TextView view = findById(id);
        view.setText(text);

        return this;
    }

    /**
     * 用于设置控件的image(网络图片)
     *
     * @param id    控件ID
     * @param url   图片url
     * @param imagePixelSize    指定图片的像素大小
     * @return  返回this
     */
    public BaseViewHolder setImage(int id, String url, ImagePixelSize imagePixelSize) {
        ImageView imageView = findById(id);
        Glide.with(context).load(url + imagePixelSize.value).into(imageView);

        return this;
    }

    /**
     * 用于设置控件的image(本地资源)
     *
     * @param id    控件ID
     * @return  返回this
     */
    public BaseViewHolder setImage(int id, int resId) {
        ImageView imageView = findById(id);
        imageView.setImageResource(resId);

        return this;
    }

    /**
     * 用于设置控件的监听事件
     *
     * @param id    控件ID
     * @param onClickListener   监听事件
     * @return  返回this
     */
    public BaseViewHolder setOnClickListener (int id, View.OnClickListener onClickListener) {
        View view = findById(id);
        view.setOnClickListener(onClickListener);

        return this;
    }

    /**
     * 该方法只需要覆盖onClickListener即可,不需要指定要监听的控件
     * 点击的监听适用于整个item(不需要指定哪一个控件)
     * <br/>
     * <strong>
     * 注意：如果控件的属性中含有android:focusable="true", 需要将其关闭,否侧点击事件将不起作用
     * <strong/>
     *
     * @param onClickListener   监听事件
     * @return  返回this
     */
    public BaseViewHolder setOnClickListener (View.OnClickListener onClickListener) {

        //如果焦点设置为true,则需要将其设置为自动
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            itemView.setFocusable(false);
        } else {
            itemView.setFocusable(View.FOCUSABLE_AUTO);
        }

        itemView.setOnClickListener(onClickListener);

        return this;
    }

    /**
     * 设置控件是否要隐藏/gone
     *
     * @param id    控件ID
     * @param visibility    View.VISIBLE | View.INVISIBLE | View.GONE
     * @return 返回this
     */
    public BaseViewHolder setVisibility (int id, int visibility) {
        View view = findById(id);
        view.setVisibility(visibility);

        return this;
    }

    /**
     * 获取控件实体
     *
     * @return  结果需要强制转换为对应的控件类型
     */
    public View getItemView() {
        return itemView;
    }
}
