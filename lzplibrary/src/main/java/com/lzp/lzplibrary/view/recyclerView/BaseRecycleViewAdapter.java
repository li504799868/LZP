package com.lzp.lzplibrary.view.recyclerView;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luomin on 2016/5/10.
 */
public abstract class BaseRecycleViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {

    private static final int TYPE_VIEW = 100000;
    private ArrayList<View> mHeaderViews = new ArrayList<>(); //头视图
    private ArrayList<View> mFooterViews = new ArrayList<>();   //尾视图
    private View loadMoreFooter;
    private ArrayList<Integer> mHeaderViewTypes = new ArrayList<>();
    private ArrayList<Integer> mFooterViewTypes = new ArrayList<>();

    protected Context mContext;

    protected List<T> mDatas = new ArrayList<>();

    protected LayoutInflater mInflater;

    protected OnItemClickListener<T> mOnItemClickListener;

    private OnItemLongClickListener<T> mOnItemLongClickListener;

    /**
     * 设置点击事件监听器
     *
     * @param l 监听器对象
     */
    public void setOnItemClickListener(OnItemClickListener l) {
        this.mOnItemClickListener = l;
    }

    /**
     * 构造器
     *
     * @param context
     * @param mDatas
     */
    protected BaseRecycleViewAdapter(Context context, List<T> mDatas) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
    }

    /**
     * 返回指定的item的layoutId
     */
    protected abstract int getLayoutId();


    /**
     * 可以添加多个头视图
     *
     * @param headerView
     */
    public void addHeaderView(View headerView) {
        mHeaderViews.add(headerView);
    }

    /**
     * 移除所有headers
     */
    public void removeAllheaders() {
        mHeaderViews.clear();
    }

    /**
     * 可以添加多个尾视图
     *
     * @param footerView 尾视图
     */
    public void addFooterView(View footerView) {
        mFooterViews.add(footerView);
    }

    /**
     * 获取加载更多的footer
     */
    public View getLoadFooterView() {
        return loadMoreFooter;
    }

    /**
     * 加载更多的尾视图
     *
     * @param footerView 尾视图
     */
    public void addLoadFooterView(View footerView) {
        loadMoreFooter = footerView;
        if (loadMoreFooter != null) {//这里在添加加载更多footer时移除之前的footer
            removeLoadFooterView();
        }
        loadMoreFooter.setTag("loadFooter");
        mFooterViews.add(loadMoreFooter);

    }

    /**
     * 移除加载更多footer
     */
    public void removeLoadFooterView() {
        mFooterViews.remove(loadMoreFooter);
    }

    public void removeHeaderView(View headerView) {
        for (int i = 0; i < mHeaderViews.size(); i++) {
            if (mHeaderViews.get(i).equals(headerView)) {
                mHeaderViews.remove(i);
            }
        }

    }


    public void removeFooterView(View headerView) {
        for (int i = 0; i < mFooterViews.size(); i++) {
            if (mFooterViews.get(i).equals(headerView)) {
                mFooterViews.remove(i);
            }
        }
    }


    /**
     * Grid  header footer占据位图
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == position * TYPE_VIEW
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    /**
     * StaggeredGrid  header footer占据位图
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(RecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && (holder.getLayoutPosition() < mHeaderViews.size() || holder.getLayoutPosition() >= getItemCount() - mFooterViews.size())) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);//占据全部空间
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (mHeaderViews.size() > 0 && position < mHeaderViews.size()) {
            //用position作为HeaderView 的   ViewType标记
            //记录每个ViewType标记
            mHeaderViewTypes.add(position * TYPE_VIEW);
            return position * TYPE_VIEW;
        }

        if (mFooterViews.size() > 0 && position > getListCount() - 1 + mHeaderViews.size()) {
            //用position作为FooterView 的   ViewType标记
            //记录每个ViewType标记
            mFooterViewTypes.add(position * TYPE_VIEW);
            return position * TYPE_VIEW;
        }

        if (mHeaderViews.size() > 0) {
            return getListViewType(position - mHeaderViews.size());
        }


        return getListViewType(position);
    }

    /**
     * Item页布局类型个数，默认为1
     */
    public int getListViewType(int position) {
        return 1;
    }

    private int getListCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        return 0;
    }

    private void onBindBaseViewHolder(RecyclerViewHolder holder, int i) {
        convert(holder, mDatas.get(i), i);
    }

    /**
     * 设置每个页面显示的内容
     *
     * @param holder itemHolder
     * @param item   每一Item显示的数据
     */
    public abstract void convert(RecyclerViewHolder holder, T item, int position);


    /**
     * 创建ViewHolder
     *
     * @param parent   RecycleView对象
     * @param viewType viee类型
     * @return Holder对象
     */
    protected RecyclerViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext).inflate(getLayoutId(), parent,
                false);
        RecyclerViewHolder holder2 = createViewHolder(mContext, itemView, parent, -1);
        if (holder2 != null) {
            setListener(parent, holder2, viewType);
            return holder2;
        } else {
            RecyclerViewHolder recyclerViewHolder = RecyclerViewHolder.get(mContext, null, parent, getLayoutId());
            setListener(parent, recyclerViewHolder, viewType);
            return recyclerViewHolder;
        }


    }

    /**
     * 如果希望有新的viewholder
     *
     * @param mContext
     * @param itemView
     * @param parent
     * @return
     */
    public RecyclerViewHolder createViewHolder(Context mContext, View itemView, ViewGroup parent, int position) {
        return null;
    }


    protected void setListener(final ViewGroup parent, final RecyclerViewHolder recyclerViewHolder, int viewType) {
        if (!isEnabled(viewType)) return;
        recyclerViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = getPosition(recyclerViewHolder);
                    if (position < 0 || mDatas.size() <= position) {
                        Log.d("setOnClickListener", "" + position);
                        return;
                    }
                    mOnItemClickListener.onItemClick(parent, v, mDatas.get(position), position);
                }
            }

        });

        recyclerViewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    int position = getPosition(recyclerViewHolder);
                    if (position < 0 || mDatas.size() <= position) {
                        return true;
                    }
                    mOnItemLongClickListener.onItemLongClick(v, position, mDatas.get(position));
                }
                return false;
            }
        });
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }

    protected int getPosition(RecyclerView.ViewHolder viewHolder) {
        int headerViewCounter = getHeaderViewsCount();
        if (headerViewCounter > 0) {
            return viewHolder.getAdapterPosition() - headerViewCounter;
        }

        return viewHolder.getAdapterPosition();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (mHeaderViewTypes.contains(viewType)) {
            return new HeaderHolderRecycler(mHeaderViews.get(viewType / TYPE_VIEW));
        }

        if (mFooterViewTypes.contains(viewType)) {
            int index = viewType / TYPE_VIEW - getListCount() - mHeaderViews.size();
            return new FooterHolderRecycler(mFooterViews.get(index));
        }

        return onCreateBaseViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        if (mFooterViews.size() > 0 && (position > getListCount() - 1 + mHeaderViews.size())) {
            return;
        }
        if (mHeaderViews.size() > 0) {
            if (position < mHeaderViews.size()) {

                return;
            }
            onBindBaseViewHolder(holder, position - mHeaderViews.size());
            return;
        }
        onBindBaseViewHolder(holder, position);

    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }


    class HeaderHolderRecycler extends RecyclerViewHolder {

        public HeaderHolderRecycler(View itemView) {
            super(itemView);
        }
    }

    class FooterHolderRecycler extends RecyclerViewHolder {

        public FooterHolderRecycler(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemCount() {
        if (mHeaderViews.size() > 0 && mFooterViews.size() > 0) {
            return getListCount() + mHeaderViews.size() + mFooterViews.size();
        }
        if (mHeaderViews.size() > 0) {
            return getListCount() + mHeaderViews.size();
        }
        if (mFooterViews.size() > 0) {
            return getListCount() + mFooterViews.size();
        }

        return getListCount();
    }

    /**
     * 获取所有数据
     *
     * @return
     */
    public List<T> getLists() {
        return mDatas;
    }

    /**
     * 根据位置插入
     *
     * @param position
     * @param t
     */
    public void addOne(int position, T t) {
        mDatas.add(position, t);
        notifyItemInserted(position);
    }

    /**
     * 插入一组list
     *
     * @param list
     */
    public void setList(List<T> list) {
        int size = mDatas.size();
        mDatas.addAll(list);
        notifyItemRangeInserted(size, list.size());
    }

    /**
     * 插入一组list
     *
     * @param list
     */
    public void setData(List<T> list) {
        mDatas = list;

    }

    /**
     * 单独移除某一个
     *
     * @param position
     */
    public void removeOne(int position) {
        if (!mDatas.isEmpty()) {
            mDatas.remove(position);
            notifyItemRemoved(position);
        }

    }

    /**
     * 全部移除
     */
    public void removeAll() {
        if (!mDatas.isEmpty()) {
            mDatas.clear();
            this.notifyDataSetChanged();
        }
    }

    public int getHeaderViewsCount() {
        return mHeaderViews.size();
    }

    public int getFooterViewsCount() {
        return mFooterViews.size();
    }

    /**
     * 判断当前Item是否是header
     *
     * @param position
     * @return
     */
    public boolean isHeader(int position) {
        return getHeaderViewsCount() > 0 && position < mHeaderViews.size();
    }

    /**
     * 判断当前Item是否是footer
     *
     * @param position
     * @return
     */
    public boolean isFooter(int position) {
        int lastPosition = getItemCount() - mFooterViews.size();
        return getFooterViewsCount() > 0 && position >= lastPosition;
    }
}
