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

public class HomeAdvisorAdapter extends RecyclerView.Adapter<HomeAdvisorAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<IndexAdvisorListInfo> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int id);
    }

    public HomeAdvisorAdapter(Context context, List<IndexAdvisorListInfo> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_home_advisor,null);
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
        TextView goldAdvisor;
        TextView shouAdvisor;
        TextView count;
        TextView income;
        TextView detail;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_home_advisor_img);
            name = (TextView) itemView.findViewById(R.id.tv_home_advisor_name);
            goldAdvisor = (TextView) itemView.findViewById(R.id.tv_home_advisor_midder);
            shouAdvisor = (TextView) itemView.findViewById(R.id.tv_home_advisor_short);
            count = (TextView) itemView.findViewById(R.id.tv_home_advisor_number);
            income = (TextView) itemView.findViewById(R.id.tv_home_advisor_income);
            detail = (TextView) itemView.findViewById(R.id.tv_home_advisor_detail);
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
            String picUrl = mContext.getResources().getString(R.string.file_host_address)
                    +mContext.getResources().getString(R.string.showpic)
                    +bean.headImg;
            Glide.with(mContext).load(picUrl)
                    .placeholder(R.drawable.home_adviosr_img)
                    .bitmapTransform(new CropCircleTransformation(mContext)).into(imageView);
            name.setText(bean.ud_nickname);
            if(bean.type.equals("1")){
                shouAdvisor.setVisibility(View.GONE);
                goldAdvisor.setVisibility(View.VISIBLE);
            }else{
                goldAdvisor.setVisibility(View.GONE);
                shouAdvisor.setVisibility(View.VISIBLE);
            }
            count.setText(bean.dy_count);
            income.setText("近期收益："+bean.mf_bysy+"%");
            detail.setText(bean.ud_memo);
        }
    }
}
