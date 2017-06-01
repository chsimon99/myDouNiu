package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.AnswerListInfo;

import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class AdvisorHomeAskingAdapter extends RecyclerView.Adapter<AdvisorHomeAskingAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<AnswerListInfo> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon,AnswerListInfo bean);
    }

    public AdvisorHomeAskingAdapter(Context context, List<AnswerListInfo> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_advisor_home_asking,null);
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

    public void addDatas(List<AnswerListInfo> data) {
        mDatas.addAll(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        private ImageView lock;
        private ImageView unlock;
        private TextView title;
        private TextView count;
        private TextView time;
        private TextView detail;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            lock = (ImageView) itemView.findViewById(R.id.iv_advisor_home_asking_lock_yy);
            unlock = (ImageView) itemView.findViewById(R.id.iv_advisor_home_asking_unlock_yy);
            title = (TextView) itemView.findViewById(R.id.tv_advisor_home_asking_title);
            count = (TextView) itemView.findViewById(R.id.tv_advisor_home_asking_count);
            time = (TextView) itemView.findViewById(R.id.tv_advisor_home_asking_time);
            detail = (TextView) itemView.findViewById(R.id.tv_advisor_home_asking_detail);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition(),mDatas.get(getPosition()));
            }
        }

        public void setRefreshData(AnswerListInfo bean, int position) {
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
