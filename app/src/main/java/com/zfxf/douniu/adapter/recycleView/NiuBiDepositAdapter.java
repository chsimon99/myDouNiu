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

public class NiuBiDepositAdapter extends RecyclerView.Adapter<NiuBiDepositAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<ProjectListResult> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int id,String money);
    }

    public NiuBiDepositAdapter(Context context, List<ProjectListResult> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_niubi_deposit, null);
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
        TextView money;
        TextView add;
        TextView rmb;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            money = (TextView) itemView.findViewById(R.id.tv_item_niubi_deposit_money);
            add = (TextView) itemView.findViewById(R.id.tv_item_niubi_deposit_add);
            rmb = (TextView) itemView.findViewById(R.id.tv_item_niubi_deposit_rmb);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition(),mDatas.get(getPosition()).money);
            }
        }

        public void setRefreshData(ProjectListResult bean, int position) {
            money.setText(bean.niubi+"牛币");
            rmb.setText("￥"+bean.money+"元");
            add.setText("+"+bean.jiangli+"牛币");
        }
    }
}
