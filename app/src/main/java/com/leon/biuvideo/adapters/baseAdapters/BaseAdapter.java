package com.leon.biuvideo.adapters.baseAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.otherFragments.biliUserFragments.BiliUserFragment;
import com.leon.biuvideo.ui.resourcesFragment.article.ArticleFragment;
import com.leon.biuvideo.ui.resourcesFragment.audio.AudioFragment;
import com.leon.biuvideo.ui.resourcesFragment.picture.PictureFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.bangumi.BangumiFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.contribution.VideoFragment;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.InternetUtils;
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

    /**
     * 使用此 Constructor 需要调用append (List<T> addOns)添加数据
     */
    public BaseAdapter(Context context) {
        this.beans = new ArrayList<>();
        this.context = context;

        this.supportActivity = (SupportActivity) context;
    }

    public BaseAdapter(List<T> beans, Context context) {
        this.beans = beans;
        this.context = context;

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
        int positionStart = this.beans.size();

        this.beans.addAll(addOns);

        notifyItemRangeInserted(positionStart, addOns.size());
    }

    /**
     * 刷新加载数据
     *
     * @param addOn    要加入的数据
     */
    public void append (T addOn) {
        this.beans.add(addOn);

        notifyItemInserted(this.beans.size() - 1);
    }

    /**
     * 根据其对象进行删除
     *
     * @param t     对象
     */
    public void remove(T t) {
        int index = this.beans.indexOf(t);

        this.beans.remove(index);
        notifyItemRemoved(index);
    }

    /**
     * 清空已存在的数据
     */
    public void removeAll() {
        if (this.beans != null) {
            int count = this.beans.size();
            this.beans.clear();

            notifyItemRangeRemoved(0, count);
        }
    }

    /**
     * 启动一个“公共”Fragment
     *
     * @param fragmentType  FragmentType
     * @param params 参数
     */
    protected void startPublicFragment (FragmentType fragmentType, String params) {
        if (!InternetUtils.checkNetwork(context)) {
            SimpleSnackBar.make(supportActivity.getWindow().getDecorView(), context.getString(R.string.networkWarn), SimpleSnackBar.LENGTH_SHORT).show();
            return;
        }

        // 获取栈顶的SupportFragment
        SupportFragment topFragment = (SupportFragment) supportActivity.getTopFragment();
        switch (fragmentType) {
            case BILI_USER:
                topFragment.start(new BiliUserFragment(params));
                break;
            case VIDEO:
                topFragment.start(new VideoFragment(params));
                break;
            case BANGUMI:
                topFragment.start(new BangumiFragment(params));
                break;
            case AUDIO:
                topFragment.start(new AudioFragment(params));
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
