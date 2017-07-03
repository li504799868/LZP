package com.lzp.lzplibrary.view.recyclerView;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lizhipeng 2017.06.28
 * <p>
 * recycleradapter 的item点击回调监听
 */
public interface OnItemClickListener<T> {
    void onItemClick(ViewGroup parent, View view, T t, int position);
}