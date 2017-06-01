package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.MyContentList;

import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class MyselfSubscribePublicAdapter extends RecyclerView.Adapter<MyselfSubscribePublicAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<MyContentList> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public MyselfSubscribePublicAdapter(Context context, List<MyContentList> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_myself_subscribe_public,null);
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

    public void addDatas(List<MyContentList> data) {
        mDatas.addAll(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        private ImageView img;
        private TextView title;
        private TextView from;
        private TextView day;
        private TextView count;
        private TextView mtupe;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            img = (ImageView) itemView.findViewById(R.id.iv_myself_subscribe_public_img);
            title = (TextView) itemView.findViewById(R.id.tv_myself_subscribe_public_title);
            from = (TextView) itemView.findViewById(R.id.tv_myself_subscribe_public_from);
            day = (TextView) itemView.findViewById(R.id.tv_myself_subscribe_public_day);
            count = (TextView) itemView.findViewById(R.id.tv_myself_subscribe_public_count);
            mtupe = (TextView) itemView.findViewById(R.id.tv_myself_subscribe_public_type);
            mtupe.getPaint().setFakeBoldText(true);//加粗
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, Integer.parseInt(mDatas.get(getPosition()).cc_id));
            }
        }

        public void setRefreshData(MyContentList bean, int position) {
            Glide.with(mContext).load(bean.cc_fielid)
                    .placeholder(R.drawable.public_img).into(img);
            title.setText(bean.cc_title);
            from.setText(bean.ud_nickname);
            day.setText(bean.cc_datetime);
            count.setText(bean.dy_count);
        }
    }
}
