package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.IndexAdvisorListInfo;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class MatadorAdapter extends RecyclerView.Adapter<MatadorAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<IndexAdvisorListInfo> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int id);
    }

    public MatadorAdapter(Context context, List<IndexAdvisorListInfo> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_matador,null);
        MyHolder myHolder = new MyHolder(view , mItemClickListener);
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
    public void addDatas(List<IndexAdvisorListInfo> data) {
        mDatas.addAll(data);
    }
    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        ImageView imageView;
        TextView name;
        TextView count;
        TextView income;
        TextView detail;
        TextView level_name;
        ImageView start_three;
        ImageView start_four;
        ImageView start_five;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_home_advisor_img);
            name = (TextView) itemView.findViewById(R.id.tv_home_advisor_name);
            level_name = (TextView) itemView.findViewById(R.id.tv_home_advisor_type);
            count = (TextView) itemView.findViewById(R.id.tv_home_advisor_number);
            income = (TextView) itemView.findViewById(R.id.tv_home_advisor_income);
            detail = (TextView) itemView.findViewById(R.id.tv_home_advisor_detail);
            start_three = (ImageView) itemView.findViewById(R.id.iv_home_advisor_start_three);
            start_four = (ImageView) itemView.findViewById(R.id.iv_home_advisor_start_four);
            start_five = (ImageView) itemView.findViewById(R.id.iv_home_advisor_start_five);
            this.mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, Integer.parseInt(mDatas.get(getPosition()).ud_ub_id));
            }
        }

        public void setRefreshData(IndexAdvisorListInfo bean) {
            Glide.with(mContext).load(bean.headImg)
                    .placeholder(R.drawable.home_adviosr_img)
                    .bitmapTransform(new CropCircleTransformation(mContext)).into(imageView);
            name.setText(bean.ud_nickname);
            count.setText(bean.gz_count);
            income.setText("近期收益："+bean.mf_bysy+"%");
            detail.setText(bean.ud_memo);
            level_name.setText(bean.ud_ul_name);
            if(bean.ud_ul_level.equals("4")){
                start_three.setVisibility(View.GONE);
                start_four.setVisibility(View.GONE);
                start_five.setVisibility(View.GONE);
            }else if(bean.ud_ul_level.equals("3")){
                start_four.setVisibility(View.GONE);
                start_five.setVisibility(View.GONE);
            }else if(bean.ud_ul_level.equals("2")){
                start_five.setVisibility(View.GONE);
            }else {

            }
        }
    }
}
