package com.lzp.lzplibrary.view.recyclerView;

import android.view.View;

/**
 * Created by lizhipeng 2017.7.03
 */
public interface OnLoadListener {
    /**
     * 开始加载下一页
     *
     * @param view 当前RecyclerView/ListView/GridView
     */
    void onLoadMore(View view);
}
