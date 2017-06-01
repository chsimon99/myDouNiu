package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.XuanguDetail;

import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class MyselfSubscribeChoiceAdapter extends RecyclerView.Adapter<MyselfSubscribeChoiceAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<XuanguDetail> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int id);
    }

    public MyselfSubscribeChoiceAdapter(Context context, List<XuanguDetail> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_myself_subscribe_choice,null);
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

    public void addDatas(List<XuanguDetail> data) {
        mDatas.addAll(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        private TextView mtype;
        private TextView income;
        private TextView scale;
        private TextView days;
        private TextView subscriber;
        private ImageView mImageView;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            mImageView = (ImageView) itemView.findViewById(R.id.iv_myself_subscribe_choice_img);
            mtype = (TextView) itemView.findViewById(R.id.tv_myself_subscribe_choice_type);
            income = (TextView) itemView.findViewById(R.id.tv_myself_subscribe_choice_income);
            scale = (TextView) itemView.findViewById(R.id.tv_myself_subscribe_choice_scale);
            days = (TextView) itemView.findViewById(R.id.tv_myself_subscribe_choice_days);
            subscriber = (TextView) itemView.findViewById(R.id.tv_myself_subscribe_choice_subscriber);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, Integer.parseInt(mDatas.get(getPosition()).zf_id));
            }
        }

        public void setRefreshData(XuanguDetail bean, int position) {
            Glide.with(mContext).load(bean.zf_fieldid)
                    .placeholder(R.drawable.myself_choice_bg).into(mImageView);
            mtype.setText(bean.zf_title);
            income.setText(bean.zf_pjsy+"%");
            scale.setText(bean.zf_xgcgl+"%");
            days.setText(bean.zf_cgzq+"天");
            subscriber.setText(bean.zf_dys+"人订阅");
        }
    }
}
