package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.AnswerListInfo;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class ZhenguAnswerAdapter extends RecyclerView.Adapter<ZhenguAnswerAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<AnswerListInfo> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon,AnswerListInfo bean);
    }

    public ZhenguAnswerAdapter(Context context, List<AnswerListInfo> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_zhengu_answer, null);
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
    public void addDatas(List<AnswerListInfo> data) {
        mDatas.addAll(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        ImageView imageView;
        ImageView lock;
        ImageView unlock;
        TextView name;
        TextView title;
        TextView detail;
        TextView time;
        TextView count;
        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            imageView = (ImageView) itemView.findViewById(R.id.iv_bar_bar_img);
            lock = (ImageView) itemView.findViewById(R.id.iv_bar_bar_lock_yy);
            unlock = (ImageView) itemView.findViewById(R.id.iv_bar_bar_unlock_yy);
            name = (TextView) itemView.findViewById(R.id.tv_bar_bar_name);
            title = (TextView) itemView.findViewById(R.id.tv_bar_bar_title);
            detail = (TextView) itemView.findViewById(R.id.tv_bar_bar_detail);
            time = (TextView) itemView.findViewById(R.id.tv_bar_bar_time);
            count = (TextView) itemView.findViewById(R.id.tv_bar_bar_count);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition(),mDatas.get(getPosition()));
            }
        }

        public void setRefreshData(AnswerListInfo bean) {
            String picUrl = mContext.getResources().getString(R.string.file_host_address)
                    +mContext.getResources().getString(R.string.showpic)
                    +bean.url;
            Glide.with(mContext).load(picUrl)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .placeholder(R.drawable.home_adviosr_img).into(imageView);
            name.setText(bean.ud_nickname);
            title.setText(bean.zc_context);
            detail.setText(bean.zc_pl);
            time.setText(bean.zc_date);
            count.setText(bean.zc_count);
            if(bean.zc_sfjf.equals("0")){
                lock.setVisibility(View.VISIBLE);
                unlock.setVisibility(View.GONE);
            }else {
                lock.setVisibility(View.GONE);
                unlock.setVisibility(View.VISIBLE);
            }
        }
    }
}
