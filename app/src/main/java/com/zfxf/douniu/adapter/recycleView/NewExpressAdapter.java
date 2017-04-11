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

public class NewExpressAdapter extends RecyclerView.Adapter<NewExpressAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<String> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public NewExpressAdapter(Context context, List<String> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_new_express, null);
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
    public void addDatas(String data) {
        mDatas.add(data);
    }

    /**
     * 复用这个属性来进行多布局展示
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        TextView content;
        TextView data;
        TextView time;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            content = (TextView) itemView.findViewById(R.id.tv_new_express_content);
            data = (TextView) itemView.findViewById(R.id.tv_new_express_data);
            time = (TextView) itemView.findViewById(R.id.tv_new_express_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        public void setRefreshData(String bean, int position) {
            if(position == 0){
                data.setVisibility(View.VISIBLE);
            }else{
                data.setVisibility(View.GONE);
            }
        }
    }
}
