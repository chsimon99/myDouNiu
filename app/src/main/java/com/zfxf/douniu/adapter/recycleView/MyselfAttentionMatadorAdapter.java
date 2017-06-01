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

public class MyselfAttentionMatadorAdapter extends RecyclerView.Adapter<MyselfAttentionMatadorAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<IndexAdvisorListInfo> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public MyselfAttentionMatadorAdapter(Context context, List<IndexAdvisorListInfo> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_myself_attention_matador,null);
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

    public void addDatas(List<IndexAdvisorListInfo> data) {
        mDatas.addAll(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        private ImageView img;
        private ImageView type;
        private TextView name;
        private TextView month;
        private TextView week;
        private TextView number;//排名

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            img = (ImageView) itemView.findViewById(R.id.iv_myself_attention_matador_img);
            type = (ImageView) itemView.findViewById(R.id.iv_myself_attention_matador_type);
            name = (TextView) itemView.findViewById(R.id.tv_myself_attention_matador_name);
            month = (TextView) itemView.findViewById(R.id.tv_myself_attention_matador_month);
            week = (TextView) itemView.findViewById(R.id.tv_myself_attention_matador_week);
            number = (TextView) itemView.findViewById(R.id.tv_myself_attention_matador_number);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        public void setRefreshData(IndexAdvisorListInfo bean, int position) {
            Glide.with(mContext).load(bean.headImg)
                    .placeholder(R.drawable.home_adviosr_img)
                    .bitmapTransform(new CropCircleTransformation(mContext)).into(img);
            name.setText(bean.ud_nickname);
            month.setText(bean.mf_bysy+"%");
            week.setText(bean.mf_bzsy+"%");
            number.setText(bean.mf_zpm);
            if(bean.mf_status.equals("1")){
                type.setImageResource(R.drawable.myself_up);
            }else {
                type.setImageResource(R.drawable.myself_up_down);
            }
        }
    }
}
