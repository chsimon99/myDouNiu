package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zfxf.douniu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<String> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public HistoryAdapter(Context context, List<String> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_history,null);
        MyHolder myHolder = new MyHolder(view , mItemClickListener);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.setRefreshData(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        TextView time;
        ListView  mListView;
        private List<String> datas = new ArrayList<String>();
        private MyListViewAdapter mListViewAdapter;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            time = (TextView) itemView.findViewById(R.id.tv_history_item_time);
            mListView = (ListView) itemView.findViewById(R.id.rv_history_item);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        public void setRefreshData(int position) {
            if(position == 0){
                time.setText("2017年3月18日");
            }else if(position == 1){
                time.setText("2017年3月16日");
            }else{
                time.setText("2017年3月13日");
            }
            if (datas.size() == 0) {
                datas.add("");
                datas.add("");
                datas.add("");
                datas.add("");
                datas.add("");
                datas.add("");
            }
            if(mListViewAdapter == null){
                mListViewAdapter = new MyListViewAdapter(datas);
            }
            mListView.setAdapter(mListViewAdapter);
            setListViewHeightBasedOnChildren(mListView);
        }
    }
    class MyListViewAdapter extends BaseAdapter{
        private List<String> datas = new ArrayList<>();

        public MyListViewAdapter(List<String> datas) {
            this.datas = datas;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = View.inflate(mContext,R.layout.item_history_detail,null);
                holder.stock_name = (TextView) convertView.findViewById(R.id.tv_history_detail_item_name);
                holder.price = (TextView) convertView.findViewById(R.id.tv_history_detail_item_price);
                holder.highprice = (TextView) convertView.findViewById(R.id.tv_history_detail_item_highprice);
                holder.rise = (TextView) convertView.findViewById(R.id.tv_history_detail_item_rise);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
//            holder.stock_name.setText("");
//            holder.price.setText("");
//            holder.highprice.setText("");
//            holder.rise.setText("");
            return convertView;
        }
        class ViewHolder{
            public TextView stock_name;
            public TextView price;
            public TextView highprice;
            public TextView rise;
        }
    }
    /**
     * 动态设置ListView的高度
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
