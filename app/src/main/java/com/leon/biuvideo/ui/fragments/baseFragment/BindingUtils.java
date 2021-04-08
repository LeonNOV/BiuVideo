package com.leon.biuvideo.ui.fragments.baseFragment;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leon.biuvideo.values.ImagePixelSize;
import com.ms.square.android.expandabletextview.ExpandableTextView;

/**
 * 用于绑定Fragment中控件的资源
 */
public class BindingUtils {
    private final View view;
    private final Context context;

    public BindingUtils(View view, Context context) {
        this.view = view;
        this.context = context;
    }

    /**
     * 给指定可设置文字的控件设置文字
     *
     * @param id    控件ID
     * @param text  文本
     * @return  返回该对象本身
     */
    public BindingUtils setText(int id, String text) {
        View view = this.view.findViewById(id);

        if (view instanceof TextView) {
            ((TextView) view).setText(text);
        } else if (view instanceof ExpandableTextView) {
            ((ExpandableTextView) view).setText(text);
        }

        return this;
    }

    /**
     * 对可以设置图片资源的控件设置图片(网络图片)
     *
     * @param id    控件ID
     * @param url   图片网络路径
     * @param imagePixelSize    图片像素大小
     * @return  返回该对象本身
     */
    public BindingUtils setImage(int id, String url, ImagePixelSize imagePixelSize) {
        ImageView imageView = this.view.findViewById(id);
        Glide.with(context).load(url + imagePixelSize.value).into(imageView);

        return this;
    }

    /**
     * 对可以设置图片资源的控件设置图片(本地图片)
     *
     * @param id    控件ID
     * @param resId  图片资源ID
     * @return  返回该对象本身
     */
    public BindingUtils setImage(int id,int resId) {
        ImageView imageView = this.view.findViewById(id);
        imageView.setImageResource(resId);

        return this;
    }

    /**
     * 设置控件是否要隐藏/gone
     *
     * @param id    控件ID
     * @param visibility    View.VISIBLE | View.INVISIBLE | View.GONE
     * @return 返回this
     */
    public BindingUtils setVisibility (int id, int visibility) {
        View view = this.view.findViewById(id);
        view.setVisibility(visibility);

        return this;
    }

    /**
     * 设置控件的点击事件
     *
     * @param id    控件ID
     * @param onClickListener   监听事件
     * @return  返回该对象本身
     */
    public BindingUtils setOnClickListener(int id, View.OnClickListener onClickListener) {
        View view = this.view.findViewById(id);
        view.setOnClickListener(onClickListener);

        return this;
    }

    /**
     * 设置控件的选中变化的监听
     * <strong>适合CompoundButton的子类</strong>
     *
     * @param id    控件ID
     * @param onCheckedChangeListener   选中变化事件
     * @return  返回该对象本身
     */
    public BindingUtils setOnCheckedChangeListener(int id, CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        CompoundButton view = this.view.findViewById(id);
        view.setOnCheckedChangeListener(onCheckedChangeListener);

        return this;
    }
}
