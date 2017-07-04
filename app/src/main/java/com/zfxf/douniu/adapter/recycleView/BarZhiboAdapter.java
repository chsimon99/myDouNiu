package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.MatadorPKList;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class BarZhiboAdapter extends RecyclerView.Adapter<BarZhiboAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<MatadorPKList> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public BarZhiboAdapter(Context context, List<MatadorPKList> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_bar_zhibo, null);
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
    public void addDatas(List<MatadorPKList> data) {
        mDatas.addAll(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        ImageView leftImg;
        ImageView rightImg;
        TextView leftname;
        TextView leftincome;
        TextView leftcount;
        TextView rightname;
        TextView rightincome;
        TextView rightcount;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            leftImg = (ImageView) itemView.findViewById(R.id.iv_bar_zhibo_left_img);
            rightImg = (ImageView) itemView.findViewById(R.id.iv_bar_zhibo_right_img);
            leftname = (TextView) itemView.findViewById(R.id.tv_bar_zhibo_left_name);
            leftincome = (TextView) itemView.findViewById(R.id.tv_bar_zhibo_left_income);
            leftcount = (TextView) itemView.findViewById(R.id.tv_bar_zhibo_left_count);
            rightname = (TextView) itemView.findViewById(R.id.tv_bar_zhibo_right_name);
            rightincome = (TextView) itemView.findViewById(R.id.tv_bar_zhibo_right_income);
            rightcount = (TextView) itemView.findViewById(R.id.tv_bar_zhibo_right_count);
            leftname.getPaint().setFakeBoldText(true);//加粗
            rightname.getPaint().setFakeBoldText(true);//加粗
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        public void setRefreshData(MatadorPKList bean) {
            Glide.with(mContext).load(bean.op_fileid_1)
                    .placeholder(R.drawable.home_adviosr_img)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(leftImg);
            Glide.with(mContext).load(bean.op_fileid_2)
                    .placeholder(R.drawable.home_adviosr_img)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(rightImg);
            leftname.setText(bean.op_name_1);
            rightname.setText(bean.op_name_2);
            leftcount.setText(bean.op_rq_1);
            rightcount.setText(bean.op_rq_2);
            leftincome.setText(bean.op_sy_1+"%");
            rightincome.setText(bean.op_sy_2+"%");
        }
    }
}
