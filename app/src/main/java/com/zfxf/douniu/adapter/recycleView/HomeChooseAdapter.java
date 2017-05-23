package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.IndexStockListInfo;

import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class HomeChooseAdapter extends RecyclerView.Adapter<HomeChooseAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<IndexStockListInfo> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int id);
    }

    public HomeChooseAdapter(Context context, List<IndexStockListInfo> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_home_choose,null);
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

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        ImageView img;
        TextView type;
        TextView scale;
        TextView income;
        TextView days;
        TextView subscriber;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            img = (ImageView) itemView.findViewById(R.id.iv_home_choose_img);
            type = (TextView) itemView.findViewById(R.id.tv_home_choose_type);
            scale = (TextView) itemView.findViewById(R.id.tv_home_choose_scale);
            income = (TextView) itemView.findViewById(R.id.tv_home_choose_income);
            days = (TextView) itemView.findViewById(R.id.tv_home_choose_days);
            subscriber = (TextView) itemView.findViewById(R.id.tv_home_choose_subscriber);

            type.getPaint().setFakeBoldText(true);//加粗
            subscriber.getPaint().setFakeBoldText(true);//加粗
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, Integer.parseInt(mDatas.get(getPosition()).zf_id));
            }
        }

        public void setRefreshData(IndexStockListInfo bean, int position) {
            Glide.with(mContext).load(bean.zf_fieldid)
                    .placeholder(R.drawable.home_choose).into(img);
            type.setText(bean.zf_title);
            income.setText(bean.zf_pjsy+"%");
            scale.setText(bean.zf_xgcgl+"%");
            days.setText(bean.zf_cgzq+"天");
            subscriber.setText(bean.zf_dys+"人订阅");
        }
    }
}
