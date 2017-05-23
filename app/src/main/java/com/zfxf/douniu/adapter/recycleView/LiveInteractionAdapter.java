package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.LivingInteract;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class LiveInteractionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<LivingInteract> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public LiveInteractionAdapter(Context context, List<LivingInteract> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == TYPE_AUDIENCE){
            view = View.inflate(mContext, R.layout.item_live_interaction_audience, null);
            MyAudienceHolder myHolder = new MyAudienceHolder(view, mItemClickListener);
            return myHolder;
        }else {
            view = View.inflate(mContext, R.layout.item_live_interaction_anchor, null);
            MyAnchorHolder myHolder = new MyAnchorHolder(view, mItemClickListener);
            return myHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyAudienceHolder){
            ((MyAudienceHolder) holder).setRefreshData(mDatas.get(position));
        } else if(holder instanceof MyAnchorHolder){
            ((MyAnchorHolder) holder).setRefreshData(mDatas.get(position));
        }
    }
    @Override
    public int getItemCount() {
        return mDatas.size();
    }
    public void addDatas(List<LivingInteract> data) {
        mDatas.addAll(0,data);
    }
    public void addNewDatas(LivingInteract data) {
        mDatas.add(data);
    }

    class MyAudienceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        ImageView imageView;
        TextView name;
        TextView time;
        TextView content;
        public MyAudienceHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            imageView = (ImageView) itemView.findViewById(R.id.iv_live_interaction_audience);
            name = (TextView) itemView.findViewById(R.id.tv_live_interaction_audience_name);
            time = (TextView) itemView.findViewById(R.id.tv_live_interaction_audience_time);
            content = (TextView) itemView.findViewById(R.id.tv_live_interaction_audience_content);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        public void setRefreshData(LivingInteract bean) {
            Glide.with(mContext).load(bean.headImg)
                    .placeholder(R.drawable.home_adviosr_img)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(imageView);
            name.setText(bean.ud_nickname);
            time.setText(bean.zp_date);
            content.setText(bean.zp_pl);
        }
    }
    class MyAnchorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        ImageView imageView;
        TextView name;
        TextView time;
        TextView content;
        TextView from;
        public MyAnchorHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            imageView = (ImageView) itemView.findViewById(R.id.iv_live_interaction_anchor);
            name = (TextView) itemView.findViewById(R.id.tv_live_interaction_anchor_name);
            time = (TextView) itemView.findViewById(R.id.tv_live_interaction_anchor_time);
            content = (TextView) itemView.findViewById(R.id.tv_live_interaction_anchor_content);
            from = (TextView) itemView.findViewById(R.id.tv_live_interaction_anchor_from);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        public void setRefreshData(LivingInteract bean) {
            Glide.with(mContext).load(bean.headImg)
                    .placeholder(R.drawable.home_adviosr_img)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(imageView);
            name.setText(bean.ud_nickname);
            time.setText(bean.zp_date);
            content.setText(bean.zp_pl);
        }
    }
    public static final int TYPE_ANCHOR = 0;
    public static final int TYPE_AUDIENCE = 1;
    @Override
    public int getItemViewType(int position) {
        if(mDatas.get(position).role.equals("0")){
            return TYPE_ANCHOR;
        }else {
            return TYPE_AUDIENCE;
        }
    }
}
