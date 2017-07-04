package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.ResearchInfo;

import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class HomeResearchStockAdapter extends RecyclerView.Adapter<HomeResearchStockAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<ResearchInfo> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int id);
    }

    public HomeResearchStockAdapter(Context context, List<ResearchInfo> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_home_research_stock,null);
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

    public void addDatas(List<ResearchInfo> data) {
        mDatas.addAll(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        private TextView type;
        private TextView name;
        private TextView code;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            type = (TextView) itemView.findViewById(R.id.tv_item_home_research_stock_type);
            name = (TextView) itemView.findViewById(R.id.tv_item_home_research_stock_name);
            code = (TextView) itemView.findViewById(R.id.tv_item_home_research_stock_code);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, Integer.parseInt(mDatas.get(getPosition()).mg_id));
            }
        }

        public void setRefreshData(ResearchInfo bean, int position) {
            type.setText(bean.mg_mb_name);
            name.setText(bean.mg_name);
            code.setText(bean.mg_code);
        }
    }

}
