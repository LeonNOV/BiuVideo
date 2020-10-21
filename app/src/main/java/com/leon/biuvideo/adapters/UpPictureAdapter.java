package com.leon.biuvideo.adapters;

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

import java.util.List;

public class UpPictureAdapter extends RecyclerView.Adapter<UpPictureAdapter.ViewHolder> {
    private List<UpPicture> upPictures;
    private Context context;

    public UpPictureAdapter(List<UpPicture> upPictures, Context context) {
        this.upPictures = upPictures;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.up_picture_list_view_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpPictureAdapter.ViewHolder holder, int position) {
        UpPicture upPicture = upPictures.get(position);

        //设置相簿封面
        Glide.with(context).load(upPicture.pictureUrls.get(0)).into(holder.up_picture_imageView_cover);

        //设置相簿count
        //总数大于2则进行显示
        if (upPicture.pictureUrls.size() > 1) {
            holder.up_picture_textView_count.setText(upPicture.pictureUrls.size() + "P");
        } else {
            holder.up_picture_textView_count.setVisibility(View.INVISIBLE);
        }

        //设置标题
        holder.up_picture_textView_desc.setText(upPicture.description);

        //设置查看次数
        holder.up_picture_textView_view.setText(upPicture.view + "次观看");

        //设置喜欢数
        holder.up_picture_textView_like.setText(upPicture.like + "次喜欢");
    }

    @Override
    public int getItemCount() {
        return upPictures.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
}
