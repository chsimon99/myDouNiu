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

public class AdvisorHomeCapitalAdapter extends RecyclerView.Adapter<AdvisorHomeCapitalAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<String> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public AdvisorHomeCapitalAdapter(Context context, List<String> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_advisor_home_capital,null);
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

    public void addDatas(List<String> data) {
        mDatas.addAll(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        private TextView title;
        private TextView detail;
        private TextView count;
        private TextView join;
        private LinearLayout ll;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            title = (TextView) itemView.findViewById(R.id.tv_advisor_home_capital_title);
            detail = (TextView) itemView.findViewById(R.id.tv_advisor_home_capital_detail);
            count = (TextView) itemView.findViewById(R.id.tv_advisor_home_capital_count);
            join = (TextView) itemView.findViewById(R.id.tv_advisor_home_capital_join);
            ll = (LinearLayout) itemView.findViewById(R.id.ll_advisor_home_capital_join);

            ll.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
                join.setText("已加入");
                join.setBackgroundResource(R.drawable.backgroud_button_gary_color);
            }
        }

        public void setRefreshData(String str, int position) {

        }
    }
}
