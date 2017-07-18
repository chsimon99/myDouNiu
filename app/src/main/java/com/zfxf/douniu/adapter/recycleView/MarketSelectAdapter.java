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

public class MarketSelectAdapter extends RecyclerView.Adapter<MarketSelectAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<SimulationInfo> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, String code,String name);
    }

    public MarketSelectAdapter(Context context, List<SimulationInfo> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_market_select,null);
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
        TextView ratio;//最新市值
        TextView price;//现价
        TextView code;//代码

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            name = (TextView) itemView.findViewById(R.id.tv_item_market_select_name);
            price = (TextView) itemView.findViewById(R.id.tv_item_market_select_price);
            code = (TextView) itemView.findViewById(R.id.tv_item_market_select_code);
            ratio = (TextView) itemView.findViewById(R.id.tv_item_market_select_ratio);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, mDatas.get(getPosition()).mg_code,mDatas.get(getPosition()).mg_name);
            }
        }

        public void setRefreshData(SimulationInfo bean) {
            name.setText(bean.mg_name);
            code.setText(bean.mg_code);
            price.setText(bean.mg_xj);
            ratio.setText(bean.mg_zfz);
            if(bean.mg_zfz.contains("+")){
                ratio.setBackgroundResource(R.drawable.backgroud_button_red_color);
            }else {
                ratio.setBackgroundResource(R.drawable.backgroud_button_green_color);
            }
        }
    }
}
