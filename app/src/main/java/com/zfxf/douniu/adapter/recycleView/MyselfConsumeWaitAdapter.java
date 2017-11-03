package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.PayOrderInfo;

import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class MyselfConsumeWaitAdapter extends RecyclerView.Adapter<MyselfConsumeWaitAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<PayOrderInfo> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon ,PayOrderInfo info);
    }

    public MyselfConsumeWaitAdapter(Context context, List<PayOrderInfo> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_myself_consume_wait,null);
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

    public void addDatas(List<PayOrderInfo> data) {
        mDatas.addAll(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        private TextView orderNumber;
        private TextView time;
        private TextView content;
        private TextView price;
        private TextView state;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            orderNumber = (TextView) itemView.findViewById(R.id.tv_myself_consume_wait_ordernumber);
            time = (TextView) itemView.findViewById(R.id.tv_myself_consume_wait_time);
            content = (TextView) itemView.findViewById(R.id.tv_myself_consume_wait_content);
            price = (TextView) itemView.findViewById(R.id.tv_myself_consume_wait_price);
            state = (TextView) itemView.findViewById(R.id.tv_myself_consume_wait_state);
            state.getPaint().setFakeBoldText(true);//加粗

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition(),mDatas.get(getPosition()));
            }
        }

        public void setRefreshData(PayOrderInfo bean, int position) {
            orderNumber.setText("订单号:"+bean.pmo_order);
            time.setText(bean.pmo_time);
            if(bean.type.equals("0")){
                content.setText(bean.pmo_info);
            }else if(bean.type.equals("1")){
                content.setText(bean.pmo_info+"的金股池");
            }else if(bean.type.equals("2")){
                content.setText(bean.pmo_info);
            }else if(bean.type.equals("3")){
                content.setText("私密课 "+bean.pmo_info);
            }else if(bean.type.equals("4")){
                content.setText(bean.pmo_info);
            }else if(bean.type.equals("5")){
                content.setText("大参考 "+bean.pmo_info);
            }else if(bean.type.equals("6")){
                content.setText("选股王");
            }
            price.setText("￥"+bean.pmo_fee+"元");
        }
    }
}
