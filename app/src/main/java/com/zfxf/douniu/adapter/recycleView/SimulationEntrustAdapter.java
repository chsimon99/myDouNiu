package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.SimulationEntrust;

import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class SimulationEntrustAdapter extends RecyclerView.Adapter<SimulationEntrustAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<SimulationEntrust> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon,SimulationEntrust entrust);
    }

    public SimulationEntrustAdapter(Context context, List<SimulationEntrust> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_simulation_entrust,null);
        MyHolder myHolder = new MyHolder(view , mItemClickListener);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.setRefreshData(mDatas.get(position),position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void addDatas(List<SimulationEntrust> data) {
        mDatas.addAll(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        private TextView type;
        private TextView name;
        private TextView time;
        private TextView en_price;
        private TextView en_count;
        private TextView en_state;
        private TextView deal_price;
        private TextView deal_count;
        private TextView deal_state;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            type = (TextView) itemView.findViewById(R.id.tv_simulation_entrust_type);
            name = (TextView) itemView.findViewById(R.id.tv_simulation_entrust_name);
            time = (TextView) itemView.findViewById(R.id.tv_simulation_entrust_time);
            en_price = (TextView) itemView.findViewById(R.id.tv_simulation_entrust_price);
            deal_price = (TextView) itemView.findViewById(R.id.tv_simulation_entrust_price1);
            en_count = (TextView) itemView.findViewById(R.id.tv_simulation_entrust_count);
            deal_count = (TextView) itemView.findViewById(R.id.tv_simulation_entrust_count1);
            en_state = (TextView) itemView.findViewById(R.id.tv_simulation_entrust_state);
            deal_state = (TextView) itemView.findViewById(R.id.tv_simulation_entrust_state1);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition(),mDatas.get(getPosition()));
            }
        }

        public void setRefreshData(SimulationEntrust bean, int position) {
            if(bean.mj_type.equals("1")){
                type.setText("买");
                en_state.setText("买入");
                type.setBackgroundResource(R.drawable.backgroud_stock_buy);
            }else {
                type.setText("卖");
                en_state.setText("卖出");
                type.setBackgroundResource(R.drawable.backgroud_stock_sold);
            }
            name.setText(bean.mg_name);
            time.setText(bean.mj_wtsj);
            en_price.setText(bean.mj_wtj);
            deal_price.setText(bean.mj_cjj);
            en_count.setText(bean.mj_wtl);
            deal_count.setText(bean.mj_cjl);
            if(bean.mj_result.equals("2")){
                deal_state.setText("部分交易");
            }else {
                deal_state.setText("等待交易");
            }
        }
    }
}
