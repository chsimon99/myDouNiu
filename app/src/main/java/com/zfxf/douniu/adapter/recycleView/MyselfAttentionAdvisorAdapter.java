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

public class MyselfAttentionAdvisorAdapter extends RecyclerView.Adapter<MyselfAttentionAdvisorAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<String> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public MyselfAttentionAdvisorAdapter(Context context, List<String> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_myself_attention_advisor,null);
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
        private TextView name;
        private TextView number;//关注
        private TextView midder;//中线
        private TextView mshort;//短线
        private TextView msuper;//超短线
        private TextView detail;//信息
        private TextView income;//收益

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            img = (ImageView) itemView.findViewById(R.id.iv_myself_attention_advisor_img);
            name = (TextView) itemView.findViewById(R.id.tv_myself_attention_advisor_name);
            number = (TextView) itemView.findViewById(R.id.tv_myself_attention_advisor_number);
            midder = (TextView) itemView.findViewById(R.id.tv_myself_attention_advisor_midder);
            mshort = (TextView) itemView.findViewById(R.id.tv_myself_attention_advisor_short);
            msuper = (TextView) itemView.findViewById(R.id.tv_myself_attention_advisor_super);
            detail = (TextView) itemView.findViewById(R.id.tv_myself_attention_advisor_detail);
            income = (TextView) itemView.findViewById(R.id.tv_myself_attention_advisor_income);

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
