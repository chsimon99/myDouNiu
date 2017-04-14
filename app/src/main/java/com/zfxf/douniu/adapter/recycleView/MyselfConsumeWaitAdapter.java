package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zfxf.douniu.R;

import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class MyselfConsumeWaitAdapter extends RecyclerView.Adapter<MyselfConsumeWaitAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<String> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public MyselfConsumeWaitAdapter(Context context, List<String> datas) {
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

    public void addDatas(List<String> data) {
        mDatas.addAll(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        private ImageView img;
        private TextView orderNumber;
        private TextView time;
        private TextView content;
        private TextView price;
        private TextView state;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            img = (ImageView) itemView.findViewById(R.id.iv_myself_consume_wait_img);
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
                mListener.onItemClick(v, getPosition());
            }
        }

        public void setRefreshData(String str, int position) {
            if(position == 0){
                img.setImageResource(R.drawable.myself_simi);
            }else{
                img.setImageResource(R.drawable.myself_ziben);
            }
        }
    }
}
