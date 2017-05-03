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

public class SimulationEntrustAdapter extends RecyclerView.Adapter<SimulationEntrustAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<String> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public SimulationEntrustAdapter(Context context, List<String> datas) {
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

    public void addDatas(List<String> data) {
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
                mListener.onItemClick(v, getPosition());
            }
        }

        public void setRefreshData(String str, int position) {

        }
    }
}
