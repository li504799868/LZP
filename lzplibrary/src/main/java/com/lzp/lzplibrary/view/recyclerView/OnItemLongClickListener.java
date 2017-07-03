package com.lzp.lzplibrary.view.recyclerView;

import android.view.View;

/**
 * Created by luomin on 16/8/4.
 */
public interface OnItemLongClickListener<T> {
    void onItemLongClick(View view, int position, T data);
}
