package com.leon.biuvideo.adapters.UserFragmentAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.articleBeans.Article;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 文章列表适配器
 */
public class UserArticleAdapter extends RecyclerView.Adapter<UserArticleAdapter.ViewHolder> {
    private List<Article> articles;
    private Context context;

    public UserArticleAdapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_article_list_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = articles.get(position);

        //设置封面
        Glide.with(context).load(article.coverUrl).into(holder.article_imageView_cover);

        //设置文章标题
        holder.article_textView_title.setText(article.title);

        //设置文章类型
        holder.article_textView_category.setText(article.category);

        //设置创建时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        holder.article_textView_ctime.setText(sdf.format(new Date(article.ctime * 1000)));
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView article_cardView;
        ImageView article_imageView_cover;
        TextView
                article_textView_title,
                article_textView_category,
                article_textView_ctime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            article_cardView = itemView.findViewById(R.id.article_cardView);
            article_cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClickListener(getAdapterPosition());
                    }
                }
            });

            article_imageView_cover = itemView.findViewById(R.id.article_imageView_cover);
            article_textView_title = itemView.findViewById(R.id.article_textView_title);
            article_textView_category = itemView.findViewById(R.id.article_textView_category);
            article_textView_ctime = itemView.findViewById(R.id.article_textView_ctime);
        }
    }

    //加载数据使用
    public void refresh(List<Article> addOns) {
        int position = articles.size();

        articles.addAll(position, addOns);
        notifyDataSetChanged();
    }
}
