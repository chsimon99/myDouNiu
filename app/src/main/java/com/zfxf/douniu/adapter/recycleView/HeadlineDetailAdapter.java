package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.CommentInformationResult;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class HeadlineDetailAdapter extends RecyclerView.Adapter<HeadlineDetailAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<CommentInformationResult> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public HeadlineDetailAdapter(Context context, List<CommentInformationResult> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_headline_detail,null);
        MyHolder myHolder = new MyHolder(view , mItemClickListener);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.setRefreshData(mDatas.get(position));
    }
    public void addDatas(CommentInformationResult data) {
        mDatas.add(0,data);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        ImageView img;
        TextView name;
        TextView detail;
        TextView time;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            img = (ImageView) itemView.findViewById(R.id.iv_headline_detail_item_img);
            name = (TextView) itemView.findViewById(R.id.tv_headline_detail_item_name);
            detail = (TextView) itemView.findViewById(R.id.tv_headline_detail_item_detail);
            time = (TextView) itemView.findViewById(R.id.tv_headline_detail_item_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        public void setRefreshData(CommentInformationResult result) {
            Glide.with(mContext).load(result.ud_photo_fileid)
                    .placeholder(R.drawable.home_adviosr_img)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(img);
            name.setText(result.ud_nickname);
            detail.setText(result.ccp_info);
            time.setText(result.ccp_time);
        }
    }
}
