package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.ProjectListResult;

import java.util.List;

/**
 * @author Admin
 *  我的钱包 获取牛币内容
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class WalletNoticeAdapter extends RecyclerView.Adapter<WalletNoticeAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<ProjectListResult> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int id);
    }

    public WalletNoticeAdapter(Context context, List<ProjectListResult> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_wallet_notice, null);
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
    public void addDatas(List<ProjectListResult> data) {
        mDatas.addAll(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        TextView content;
        TextView time;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            content = (TextView) itemView.findViewById(R.id.tv_new_notice_content);
            time = (TextView) itemView.findViewById(R.id.tv_new_notice_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        public void setRefreshData(ProjectListResult bean, int position) {
            content.setText(bean.name);
            time.setText("+"+bean.number+"牛币");
        }
    }
}
