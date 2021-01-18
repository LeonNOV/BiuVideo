package com.leon.biuvideo.ui.fragments.localOrderFragments;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.orderAdapters.LocalOrderVideoDetailAdapter;
import com.leon.biuvideo.adapters.orderAdapters.LocalOrderVideoFolderAdapter;
import com.leon.biuvideo.beans.orderBeans.LocalOrder;
import com.leon.biuvideo.beans.orderBeans.LocalVideoFolder;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.utils.dataBaseUtils.LocalOrdersDatabaseUtils;

import java.util.List;

public class LocalOrderVideoFragment extends BaseFragment {
    private TextView fragment_local_order_textView_folderName, fragment_local_order_textView_total, fragment_local_order_textView_ctime;
    private RecyclerView fragment_local_order_recyclerView_folderList, fragment_local_order_recyclerView_folderDetail;
    private LocalOrdersDatabaseUtils localOrdersDatabaseUtils;

    @Override
    public int setLayout() {
        return R.layout.fragment_local_order_video;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        fragment_local_order_textView_folderName = findView(R.id.fragment_local_order_textView_folderName);
        fragment_local_order_textView_total = findView(R.id.fragment_local_order_textView_total);
        fragment_local_order_textView_ctime = findView(R.id.fragment_local_order_textView_ctime);
        fragment_local_order_recyclerView_folderList = findView(R.id.fragment_local_order_recyclerView_folderList);
        fragment_local_order_recyclerView_folderDetail = findView(R.id.fragment_local_order_recyclerView_folderDetail);
    }

    @Override
    public void initValues() {
        if (localOrdersDatabaseUtils == null) {
            localOrdersDatabaseUtils = new LocalOrdersDatabaseUtils(context);
        }

        // 默认第一个显示‘默认收藏夹’中的数据
        List<LocalOrder> localOrderList = localOrdersDatabaseUtils.queryLocalOrder("默认收藏夹");
        LocalOrderVideoDetailAdapter localOrderVideoDetailAdapter = new LocalOrderVideoDetailAdapter(context, localOrderList);
        fragment_local_order_recyclerView_folderDetail.setAdapter(localOrderVideoDetailAdapter);

        List<LocalVideoFolder> localVideoFolderList = localOrdersDatabaseUtils.queryAllLocalVideoFolder();

        LocalVideoFolder firstLocalVideoFolder = localVideoFolderList.get(0);
        fragment_local_order_textView_folderName.setText(firstLocalVideoFolder.folderName);
        fragment_local_order_textView_total.setText(firstLocalVideoFolder.videoCount + "个内容");
        fragment_local_order_textView_ctime.setText(ValueFormat.generateTime(firstLocalVideoFolder.createTime, false, false, "/"));

        LocalOrderVideoFolderAdapter localOrderVideoFolderAdapter = new LocalOrderVideoFolderAdapter(context, localVideoFolderList);
        localOrderVideoFolderAdapter.setOnClickFolderListener(new LocalOrderVideoFolderAdapter.OnClickFolderListener() {
            @Override
            public void OnClick(LocalVideoFolder localVideoFolder) {
                // 如果点击的文件夹不是已选择的文件夹，则进行重置
                if (!localVideoFolder.folderName.equals(fragment_local_order_textView_folderName.getText().toString())) {
                    fragment_local_order_textView_folderName.setText(localVideoFolder.folderName);
                    fragment_local_order_textView_total.setText(localVideoFolder.videoCount + "个内容");
                    fragment_local_order_textView_ctime.setText(ValueFormat.generateTime(localVideoFolder.createTime, false, false, "/"));

                    // 查询和localVideoFolder中的folderName对应的数据
                    List<LocalOrder> nextLocalOrderList = localOrdersDatabaseUtils.queryLocalOrder(localVideoFolder.folderName);
                    localOrderVideoDetailAdapter.reset(nextLocalOrderList);
                }
            }
        });
        fragment_local_order_recyclerView_folderList.setAdapter(localOrderVideoFolderAdapter);
    }

    /**
     * 为了使数据能够正常的显示需要使用该方法来刷新数据
     */
    @Override
    public void onResume() {
        initValues();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (localOrdersDatabaseUtils != null) {
            localOrdersDatabaseUtils.close();
        }
        super.onDestroy();
    }
}
