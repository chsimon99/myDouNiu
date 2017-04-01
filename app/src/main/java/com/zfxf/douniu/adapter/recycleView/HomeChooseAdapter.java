package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;

import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class HomeChooseAdapter extends RecyclerView.Adapter<HomeChooseAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<String> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public HomeChooseAdapter(Context context, List<String> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_home_choose,null);
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
        LinearLayout mLinearLayout;
        TextView type;
        TextView scale;
        TextView income;
        TextView days;
        TextView subscriber;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.ll_home_choose);
            type = (TextView) itemView.findViewById(R.id.tv_home_choose_type);
            scale = (TextView) itemView.findViewById(R.id.tv_home_choose_scale);
            income = (TextView) itemView.findViewById(R.id.tv_home_choose_income);
            days = (TextView) itemView.findViewById(R.id.tv_home_choose_days);
            subscriber = (TextView) itemView.findViewById(R.id.tv_home_choose_subscriber);

            type.getPaint().setFakeBoldText(true);//加粗
            subscriber.getPaint().setFakeBoldText(true);//加粗
            mLinearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        public void setRefreshData(String str, int position) {
            if(position % 2 ==0){
                mLinearLayout.setBackgroundResource(R.drawable.home_choose);
            }else{
                mLinearLayout.setBackgroundResource(R.drawable.home_choose1);
            }
        }
    }
}
