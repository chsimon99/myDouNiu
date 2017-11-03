package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.MessageInfo;

import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<MessageInfo> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int id,MessageInfo bean);
    }

    public MessageAdapter(Context context, List<MessageInfo> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_message, null);
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
    public void addDatas(List<MessageInfo> data) {
        mDatas.addAll(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        TextView content;
        TextView time;
        TextView title;
        ImageView img;
        View po;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            content = (TextView) itemView.findViewById(R.id.tv_item_message_content);
            time = (TextView) itemView.findViewById(R.id.tv_item_message_time);
            title = (TextView) itemView.findViewById(R.id.tv_item_message_title);
            img = (ImageView) itemView.findViewById(R.id.iv_item_message_pic);
            po = itemView.findViewById(R.id.v_item_message);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition(),mDatas.get(getPosition()));
            }
        }

        public void setRefreshData(MessageInfo bean, int position) {
            po.setVisibility(View.GONE);
            content.setText(bean.sm_context);
            time.setText(bean.sm_date);
            title.setText(bean.sm_title);
            if(bean.sm_ub_id.equals("0")){
                img.setImageResource(R.drawable.message_alarm);
            }else {
                img.setImageResource(R.drawable.message_system);
            }
        }
    }
}
