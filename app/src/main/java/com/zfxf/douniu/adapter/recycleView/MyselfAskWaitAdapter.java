package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.AskWait;

import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class MyselfAskWaitAdapter extends RecyclerView.Adapter<MyselfAskWaitAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<AskWait> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public MyselfAskWaitAdapter(Context context, List<AskWait> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_myself_ask_wait,null);
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

    public void addDatas(List<AskWait> data) {
        mDatas.addAll(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        private TextView title;
        private TextView time;
        private TextView type;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            title = (TextView) itemView.findViewById(R.id.tv_myself_ask_title);
            time = (TextView) itemView.findViewById(R.id.tv_myself_ask_time);
            type = (TextView) itemView.findViewById(R.id.tv_myself_ask_type);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        public void setRefreshData(AskWait bean, int position) {
            time.setText(bean.zc_date);
            title.setText(bean.zc_context);
            if(bean.zc_sfgq.equals("0")){
                type.setText("待回答");
                type.setBackgroundResource(R.drawable.backgroud_button_app_color);
            }else {
                type.setText("已过期");
                type.setBackgroundResource(R.drawable.backgroud_button_gray_color);
            }
        }
    }
}
