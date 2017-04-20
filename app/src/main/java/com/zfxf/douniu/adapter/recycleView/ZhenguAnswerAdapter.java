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

public class ZhenguAnswerAdapter extends RecyclerView.Adapter<ZhenguAnswerAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<String> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public ZhenguAnswerAdapter(Context context, List<String> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_zhengu_answer, null);
        MyHolder myHolder = new MyHolder(view, mItemClickListener);
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
    public void addDatas(List<String> data) {
        mDatas.addAll(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        ImageView imageView;
        ImageView lock;
        ImageView unlock;
        TextView name;
        TextView title;
        TextView detail;
        TextView time;
        TextView count;
        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            imageView = (ImageView) itemView.findViewById(R.id.iv_bar_bar_img);
            lock = (ImageView) itemView.findViewById(R.id.iv_bar_bar_lock_yy);
            unlock = (ImageView) itemView.findViewById(R.id.iv_bar_bar_unlock_yy);
            name = (TextView) itemView.findViewById(R.id.tv_bar_bar_name);
            title = (TextView) itemView.findViewById(R.id.tv_bar_bar_title);
            detail = (TextView) itemView.findViewById(R.id.tv_bar_bar_detail);
            time = (TextView) itemView.findViewById(R.id.tv_bar_bar_time);
            count = (TextView) itemView.findViewById(R.id.tv_bar_bar_count);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        public void setRefreshData(String str) {
            if(str.equals("1")){
                name.setText("孙悟空");
                title.setText("看我72变");
                lock.setVisibility(View.GONE);
                unlock.setVisibility(View.VISIBLE);
            }else if(str.equals("2")){
                name.setText(str);
                title.setText(str);
                unlock.setVisibility(View.GONE);
                lock.setVisibility(View.VISIBLE);
            }else{
                name.setText(str);
                title.setText(str);
                unlock.setVisibility(View.GONE);
                lock.setVisibility(View.VISIBLE);
            }
        }
    }
}
