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

public class MarketPlateAdapter extends RecyclerView.Adapter<MarketPlateAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<SimulationInfo> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, SimulationInfo bean);
    }

    public MarketPlateAdapter(Context context, List<SimulationInfo> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_market_plate,null);
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
        TextView name;
        TextView ratio;//涨幅
        TextView other;//领涨股票

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            name = (TextView) itemView.findViewById(R.id.tv_item_market_plate_name);
            ratio = (TextView) itemView.findViewById(R.id.tv_item_market_plate_ratio);
            other = (TextView) itemView.findViewById(R.id.tv_item_market_plate_stock);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, mDatas.get(getPosition()));
            }
        }

        public void setRefreshData(SimulationInfo bean) {
            other.setText(bean.mg_name+"\n"+bean.mg_code);
            name.setText(bean.mg_xj);
            ratio.setText(bean.mg_zfz);
            if(bean.mg_zfz.contains("-")){
                ratio.setTextColor(mContext.getResources().getColor(R.color.colorFall));
            }else {
                ratio.setTextColor(mContext.getResources().getColor(R.color.colorRise));
            }
        }
    }
}
