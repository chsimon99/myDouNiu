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

public class MyselfSubscribeCapitalAdapter extends RecyclerView.Adapter<MyselfSubscribeCapitalAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<MyContentList> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int id);
    }

    public MyselfSubscribeCapitalAdapter(Context context, List<MyContentList> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_myself_subscribe_capital,null);
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
        private TextView from;
        private TextView title;
        private TextView count;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            img = (ImageView) itemView.findViewById(R.id.iv_myself_subscrobe_capital_img);
            from = (TextView) itemView.findViewById(R.id.tv_myself_subscrobe_capital_from);
            title = (TextView) itemView.findViewById(R.id.tv_myself_subscrobe_capital_title);
            count = (TextView) itemView.findViewById(R.id.tv_myself_subscrobe_capital_count);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, Integer.parseInt(mDatas.get(getPosition()).cc_id));
            }
        }

        public void setRefreshData(MyContentList bean, int position) {
            String picUrl = mContext.getResources().getString(R.string.file_host_address)
                    +mContext.getResources().getString(R.string.showpic)
                    +bean.cc_fielid;
            Glide.with(mContext).load(picUrl)
                    .placeholder(R.drawable.public_img).into(img);
            title.setText(bean.cc_title);
            from.setText(bean.ud_nickname);
            count.setText(bean.dy_count);
        }
    }
}
