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

public class AdvisorAllGoldPondHistoryAdapter extends RecyclerView.Adapter<AdvisorAllGoldPondHistoryAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<SimulationInfo> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public AdvisorAllGoldPondHistoryAdapter(Context context, List<SimulationInfo> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_all_gold_pond_history, null);
        MyHolder myHolder = new MyHolder(view, mItemClickListener);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.setRefreshData(mDatas.get(position), position);
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
        private TextView name;
        private TextView number;
        private TextView price;
        private TextView time;
        private TextView highrise;
        private TextView reason;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            name = (TextView) itemView.findViewById(R.id.tv_gold_pond_history_item_name);
            number = (TextView) itemView.findViewById(R.id.tv_gold_pond_history_item_number);
            price = (TextView) itemView.findViewById(R.id.tv_gold_pond_history_item_price);
            time = (TextView) itemView.findViewById(R.id.tv_gold_pond_history_item_highprice);
            highrise = (TextView) itemView.findViewById(R.id.tv_gold_pond_history_item_highrise);
            reason = (TextView) itemView.findViewById(R.id.tv_gold_pond_history_item_reason);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        public void setRefreshData(SimulationInfo bean, int position) {
            name.setText(bean.mg_name);
            number.setText(bean.mg_code);
            if(bean.mg_zfz.contains("+")){
                highrise.setTextColor(mContext.getResources().getColor(R.color.colorRise));
            }else {
                highrise.setTextColor(mContext.getResources().getColor(R.color.colorFall));
            }
            price.setText(bean.mg_rxjg);
            time.setText(bean.dj_buy_date);
            highrise.setText(bean.mg_zfz);
            reason.setText("推荐理由："+bean.mg_tjly);
        }
    }
}
