package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;

import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class NewTopPolicyAdapter extends RecyclerView.Adapter<NewTopPolicyAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<Map<String, String>> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int infoId);
    }

    public NewTopPolicyAdapter(Context context, List<Map<String, String>> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_headline, null);
        MyHolder myHolder = new MyHolder(view, mItemClickListener);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.setRefreshData(mDatas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
    public void addDatas(List<Map<String, String>> data) {
        mDatas.addAll(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        ImageView img;
        TextView count;
        TextView time;
        TextView name;
        TextView title;
        TextView detail;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            img = (ImageView) itemView.findViewById(R.id.iv_headline_img);
            count = (TextView) itemView.findViewById(R.id.tv_headline_count);
            time = (TextView) itemView.findViewById(R.id.tv_headline_time);
            name = (TextView) itemView.findViewById(R.id.tv_headline_name);
            title = (TextView) itemView.findViewById(R.id.tv_headline_title);
            detail = (TextView) itemView.findViewById(R.id.tv_headline_detail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, Integer.parseInt(mDatas.get(getPosition()).get("cc_id")));
            }
        }

        public void setRefreshData(Map<String, String> bean, int position) {
            count.setText(bean.get("cc_count"));
            time.setText(bean.get("cc_datetime"));
            name.setText(bean.get("ud_nickname"));
            title.setText(bean.get("cc_title"));
            detail.setText(bean.get("cc_description"));
            String picUrl = mContext.getResources().getString(R.string.file_host_address)
                    +mContext.getResources().getString(R.string.showpic)
                    +bean.get("headImg");
            Glide.with(mContext).load(picUrl)
                    .placeholder(R.drawable.home_adviosr_img)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(img);
        }
    }
}
