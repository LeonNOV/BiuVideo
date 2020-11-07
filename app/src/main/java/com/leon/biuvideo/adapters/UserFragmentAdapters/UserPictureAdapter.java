package com.leon.biuvideo.adapters.UserFragmentAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.upMasterBean.UpPicture;
import com.leon.biuvideo.utils.WebpSizes;

import java.util.List;

/**
 * 用户界面，相簿fragment适配器
 */
public class UserPictureAdapter extends RecyclerView.Adapter<UserPictureAdapter.ViewHolder> {
    private List<UpPicture> upPictures;
    private final Context context;

    public UserPictureAdapter(List<UpPicture> upPictures, Context context) {
        this.upPictures = upPictures;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_picture_list_view_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserPictureAdapter.ViewHolder holder, int position) {
        UpPicture upPicture = upPictures.get(position);

        //设置相簿封面
        Glide.with(context).load(upPicture.pictureUrl + WebpSizes.cover).into(holder.up_picture_imageView_cover);

        //设置相簿count
        //总数大于2则进行显示
        if (upPicture.count > 1) {
            String p = upPicture.count + "P";
            holder.up_picture_textView_count.setText(p);
        } else {
            holder.up_picture_textView_count.setVisibility(View.INVISIBLE);
        }

        //设置标题
        holder.up_picture_textView_desc.setText(upPicture.description);

        //设置查看次数
        String view = upPicture.view + "次观看";
        holder.up_picture_textView_view.setText(view);

        //设置喜欢数
        String like = upPicture.like + "次喜欢";
        holder.up_picture_textView_like.setText(like);
    }

    @Override
    public int getItemCount() {
        return upPictures.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView up_picture_imageView_cover;
        TextView
                up_picture_textView_count,
                up_picture_textView_desc,
                up_picture_textView_view,
                up_picture_textView_like;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            up_picture_imageView_cover = itemView.findViewById(R.id.up_picture_imageView_cover);
            up_picture_textView_count = itemView.findViewById(R.id.up_picture_textView_count);
            up_picture_textView_desc = itemView.findViewById(R.id.up_picture_textView_desc);
            up_picture_textView_view = itemView.findViewById(R.id.up_picture_textView_view);
            up_picture_textView_like = itemView.findViewById(R.id.up_picture_textView_like);
        }
    }

    //加载数据使用
    public void refresh(List<UpPicture> addOns) {
        int position = upPictures.size();

        upPictures.addAll(position, addOns);

        notifyDataSetChanged();
    }
}