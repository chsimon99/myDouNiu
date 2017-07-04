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

public class HomeResearchZhiboAdapter extends RecyclerView.Adapter<HomeResearchZhiboAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<ResearchInfo> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int id,int sx_id);
    }

    public HomeResearchZhiboAdapter(Context context, List<ResearchInfo> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_home_research_zhibo,null);
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
        private TextView info;
        private TextView name;
        private TextView count;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            info = (TextView) itemView.findViewById(R.id.tv_item_home_research_zhibo_info);
            name = (TextView) itemView.findViewById(R.id.tv_item_home_research_zhibo_name);
            count = (TextView) itemView.findViewById(R.id.tv_item_home_research_zhibo_count);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, Integer.parseInt(mDatas.get(getPosition()).zt_id)
                        ,Integer.parseInt(mDatas.get(getPosition()).zt_ub_id));
            }
        }

        public void setRefreshData(ResearchInfo bean, int position) {
            name.setText(bean.zt_name);
            info.setText("创建者："+bean.ud_nickname);
            count.setText(bean.zt_clicks+"人正在看");
        }
    }

}
