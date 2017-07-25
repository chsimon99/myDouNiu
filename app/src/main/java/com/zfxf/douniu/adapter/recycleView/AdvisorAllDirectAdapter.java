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

public class AdvisorAllDirectAdapter extends RecyclerView.Adapter<AdvisorAllDirectAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<Map<String, String>> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int id,String sx_id);
    }

    public AdvisorAllDirectAdapter(Context context, List<Map<String, String>> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_advisor_all_direct, null);
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
        TextView name;
        TextView count;
        TextView title;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            img = (ImageView) itemView.findViewById(R.id.iv_advisor_all_direct_item_img);
            name = (TextView) itemView.findViewById(R.id.tv_advisor_all_direct_item_name);
            title = (TextView) itemView.findViewById(R.id.tv_advisor_all_direct_item_title);
            count = (TextView) itemView.findViewById(R.id.tv_advisor_all_direct_item_count);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, Integer.parseInt(mDatas.get(getPosition()).get("zt_id")),
                        mDatas.get(getPosition()).get("ud_ub_id"));
            }
        }

        public void setRefreshData(Map<String, String> bean, int position) {
            String picUrl = mContext.getResources().getString(R.string.file_host_address)
                    +mContext.getResources().getString(R.string.showpic)
                    +bean.get("headImg");
            Glide.with(mContext).load(picUrl)
                    .placeholder(R.drawable.home_adviosr_img)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(img);
            title.setText(bean.get("zt_name"));
            name.setText(bean.get("ud_nickname"));
            count.setText(bean.get("zt_clicks"));
        }
    }
}
