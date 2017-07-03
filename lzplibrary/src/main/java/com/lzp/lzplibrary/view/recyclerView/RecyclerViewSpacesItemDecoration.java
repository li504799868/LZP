package com.lzp.lzplibrary.view.recyclerView;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 间距
 * Created by yibin on 2016-04-05.
 */
public class RecyclerViewSpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public RecyclerViewSpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = space;
        }
    }
}
