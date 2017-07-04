package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.AskDone;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class MyselfAskDoneAdapter extends RecyclerView.Adapter<MyselfAskDoneAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<AskDone> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int id);
    }

    public MyselfAskDoneAdapter(Context context, List<AskDone> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_myself_ask,null);
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

    public void addDatas(List<AskDone> data) {
        mDatas.addAll(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        private ImageView img;
        private TextView name;
        private TextView title;
        private TextView detail;
        private TextView time;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            img = (ImageView) itemView.findViewById(R.id.iv_myself_ask_img);
            name = (TextView) itemView.findViewById(R.id.tv_myself_ask_name);
            title = (TextView) itemView.findViewById(R.id.tv_myself_ask_title);
            detail = (TextView) itemView.findViewById(R.id.tv_myself_ask_detail);
            time = (TextView) itemView.findViewById(R.id.tv_myself_ask_time);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, Integer.parseInt(mDatas.get(getPosition()).zc_id));
            }
        }

        public void setRefreshData(AskDone bean, int position) {
            Glide.with(mContext).load(bean.url)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .placeholder(R.drawable.home_adviosr_img).into(img);
            name.setText(bean.ud_nickname);
            title.setText(bean.zc_context);
            detail.setText(bean.zp_pl);
            time.setText(bean.zc_date);
        }
    }
}
