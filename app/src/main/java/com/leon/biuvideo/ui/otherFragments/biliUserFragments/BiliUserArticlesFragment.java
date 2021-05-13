package com.leon.biuvideo.ui.otherFragments.biliUserFragments;

import android.view.View;
import android.widget.TextView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.biliUserResourcesAdapters.BiliUserArticlesAdapter;
import com.leon.biuvideo.beans.biliUserResourcesBeans.BiliUserArticle;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.biliUserResourcesParseUtils.BiliUserArticlesParser;
import com.leon.biuvideo.utils.parseDataUtils.biliUserResourcesParseUtils.BiliUserVideoParser;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/4/9
 * @Desc B站用户专栏数据
 */
public class BiliUserArticlesFragment extends BaseLazySupportFragment implements View.OnClickListener {
    private final String mid;
    private final Map<Integer, TextView> textViewMap = new HashMap<>(3);

    private String order = BiliUserArticlesParser.ORDER_DEFAULT;
    private DataLoader<BiliUserArticle> biliUserArticleDataLoader;

    public BiliUserArticlesFragment(String mid) {
        this.mid = mid;
    }

    @Override
    protected int setLayout() {
        return R.layout.bili_user_articles_fragment;
    }

    @Override
    protected void initView() {
        TextView biliUserArticlesOrderDefault = findView(R.id.bili_user_articles_order_default);
        biliUserArticlesOrderDefault.setOnClickListener(this);
        textViewMap.put(0, biliUserArticlesOrderDefault);

        TextView biliUserArticlesOrderClick = findView(R.id.bili_user_articles_order_read);
        biliUserArticlesOrderClick.setOnClickListener(this);
        textViewMap.put(1, biliUserArticlesOrderClick);

        TextView biliUserArticlesOrderFav = findView(R.id.bili_user_articles_order_fav);
        biliUserArticlesOrderFav.setOnClickListener(this);
        textViewMap.put(2, biliUserArticlesOrderFav);

        biliUserArticleDataLoader = new DataLoader<>(context, new BiliUserArticlesParser(mid, order), R.id.bili_user_articles_data,
                new BiliUserArticlesAdapter(context), this);
    }

    @Override
    protected void onLazyLoad() {
        biliUserArticleDataLoader.insertData(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bili_user_articles_order_default:
                if (order.equals(BiliUserVideoParser.ORDER_DEFAULT)) {
                    return;
                }

                order = BiliUserVideoParser.ORDER_DEFAULT;
                BiliUserFragment.changeText(0, textViewMap, context);
                break;
            case R.id.bili_user_articles_order_read:
                if (order.equals(BiliUserArticlesParser.ORDER_READ)) {
                    return;
                }

                order = BiliUserArticlesParser.ORDER_READ;
                BiliUserFragment.changeText(1, textViewMap, context);
                break;
            case R.id.bili_user_articles_order_fav:
                if (order.equals(BiliUserArticlesParser.ORDER_FAV)) {
                    return;
                }

                order = BiliUserArticlesParser.ORDER_FAV;
                BiliUserFragment.changeText(2, textViewMap, context);
                break;
            default:
                break;
        }

        reset();
    }

    private void reset () {
        biliUserArticleDataLoader.reset(new BiliUserArticlesParser(mid, order));
    }
}
