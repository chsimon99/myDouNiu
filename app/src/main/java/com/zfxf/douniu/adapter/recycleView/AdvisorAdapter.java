package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.AnswerChiefListInfo;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class AdvisorAdapter extends RecyclerView.Adapter<AdvisorAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<AnswerChiefListInfo> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon,AnswerChiefListInfo bean);
    }

    public AdvisorAdapter(Context context, List<AnswerChiefListInfo> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_advisor, null);
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
    public void addDatas(List<AnswerChiefListInfo> data) {
        mDatas.addAll(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        ImageView imageView;
        TextView name;
        TextView count;
        TextView price;
        TextView type;
        TextView gold;
        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            imageView = (ImageView) itemView.findViewById(R.id.iv_advisor_img);
            name = (TextView) itemView.findViewById(R.id.tv_advisor_name);
            price = (TextView) itemView.findViewById(R.id.tv_advisor_price);
            count = (TextView) itemView.findViewById(R.id.tv_advisor_count);
            type = (TextView) itemView.findViewById(R.id.tv_advisor_type);
            gold = (TextView) itemView.findViewById(R.id.tv_advisor_gold);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition(),mDatas.get(getPosition()));
            }
        }

        public void setRefreshData(AnswerChiefListInfo bean) {
            String picUrl = mContext.getResources().getString(R.string.file_host_address)
                    +mContext.getResources().getString(R.string.showpic)
                    +bean.url;
            Glide.with(mContext).load(picUrl)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .placeholder(R.drawable.home_adviosr_img).into(imageView);
            name.setText(bean.ud_nickname);
            price.setText(bean.df_fee+"/次");
            count.setText("已回答"+bean.df_count+"人次");
            if(bean.df_sfsx.equals("金牌")){
                type.setVisibility(View.GONE);
                gold.setVisibility(View.VISIBLE);
            }else {
                type.setVisibility(View.VISIBLE);
                gold.setVisibility(View.GONE);
            }
        }
    }
}
