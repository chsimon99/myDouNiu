package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.SimulationInfo;

import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class MarketTradeAdapter extends RecyclerView.Adapter<MarketTradeAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<SimulationInfo> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, SimulationInfo bean);
    }

    public MarketTradeAdapter(Context context, List<SimulationInfo> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_market_trade,null);
        MyHolder myHolder = new MyHolder(view , mItemClickListener);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.setRefreshData(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
    public void addDatas(List<SimulationInfo> data) {
        mDatas.addAll(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        TextView name;//类别
        TextView ratio;//涨幅
        TextView other_name;//股票名
        TextView other_ratio;//股票涨幅

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            name = (TextView) itemView.findViewById(R.id.tv_item_market_trade_name);
            ratio = (TextView) itemView.findViewById(R.id.tv_item_market_trade_ratio);
            other_name = (TextView) itemView.findViewById(R.id.tv_item_market_trade_other_name);
            other_ratio = (TextView) itemView.findViewById(R.id.tv_item_market_trade_other_ratio);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, mDatas.get(getPosition()));
            }
        }

        public void setRefreshData(SimulationInfo bean) {
//            if(bean.mg_zfz.contains("-")){
//                ratio.setTextColor(mContext.getResources().getColor(R.color.colorFall));
//            }else {
//                ratio.setTextColor(mContext.getResources().getColor(R.color.colorRise));
//            }
        }
    }
}
