package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zfxf.douniu.R;

import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class MatadorPositonAdapter extends RecyclerView.Adapter<MatadorPositonAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<String> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public MatadorPositonAdapter(Context context, List<String> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_matador_position,null);
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
        TextView name;
        TextView cost;//成本
        TextView count;//持仓数量
        TextView income;//最新市值
        TextView price;//现价
        TextView profit;//盈亏
        TextView sold;//可卖数量

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            name = (TextView) itemView.findViewById(R.id.tv_item_matador_positon_name);
            cost = (TextView) itemView.findViewById(R.id.tv_item_matador_positon_cost);
            count = (TextView) itemView.findViewById(R.id.tv_item_matador_positon_count);
            income = (TextView) itemView.findViewById(R.id.tv_item_matador_positon_income);
            price = (TextView) itemView.findViewById(R.id.tv_item_matador_positon_price);
            profit = (TextView) itemView.findViewById(R.id.tv_item_matador_positon_profit);
            sold = (TextView) itemView.findViewById(R.id.tv_item_matador_positon_sold);
//            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        public void setRefreshData(int position) {

        }
    }
}
