package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.IndexLivingListInfo;

import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class HomeZhiboAdapter extends RecyclerView.Adapter<HomeZhiboAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<IndexLivingListInfo> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public HomeZhiboAdapter(Context context, List<IndexLivingListInfo> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_home_zhibo,null);
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

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        private TextView name;
        private TextView count;
        private ImageView bg;
        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            name = (TextView) itemView.findViewById(R.id.tv_home_zhibo_name);
            count = (TextView) itemView.findViewById(R.id.tv_home_zhibo_count);
            bg = (ImageView) itemView.findViewById(R.id.iv_home_zhibo_bg);

            name.getPaint().setFakeBoldText(true);//加粗
            bg.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        public void setRefreshData(IndexLivingListInfo bean, int position) {
            Glide.with(mContext).load(bean.zt_fileid)
                    .placeholder(R.drawable.home_zhibo_bg).into(bg);
            name.setText(bean.ud_nickname);
            count.setText(bean.zt_clicks+"人参与");
        }
    }
}
