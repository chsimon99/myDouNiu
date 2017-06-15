package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.login.ActivityLogin;
import com.zfxf.douniu.bean.SimulationSearchInfo;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;

import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class MarketResearchAdapter extends RecyclerView.Adapter<MarketResearchAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private MyAddStockClickListener mAddStockClickListener = null;
    private List<SimulationSearchInfo> mDatas;
    private boolean isSimulation;

    public interface MyItemClickListener {
        void onItemClick(View v, int position,SimulationSearchInfo info);
    }
    public interface MyAddStockClickListener {
        void onItemClick(View v, int position,SimulationSearchInfo info);
    }

    public MarketResearchAdapter(Context context, List<SimulationSearchInfo> datas, boolean simulation) {
        mContext = context;
        mDatas = datas;
        isSimulation = simulation;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }
    public void setOnAddStockClickListener(MyAddStockClickListener listener) {
        this.mAddStockClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_market_research,null);
        MyHolder myHolder = new MyHolder(view , mItemClickListener,mAddStockClickListener);
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
    public void addDatas(List<SimulationSearchInfo> data) {
        mDatas.addAll(data);
    }
    public void deleteDatas() {
        mDatas.clear();
    }

    public void changeItemImage(int position){
        mDatas.get(position).setAddSuccess(false);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        private MyAddStockClickListener mClickListener;
        TextView name;
        TextView type;
        TextView code;
        ImageView mView;

        public MyHolder(View itemView, MyItemClickListener listener,MyAddStockClickListener clickListener) {
            super(itemView);
            this.mListener = listener;
            this.mClickListener = clickListener;
            name = (TextView) itemView.findViewById(R.id.tv_item_market_research_name);
            type = (TextView) itemView.findViewById(R.id.tv_item_market_research_type);
            code = (TextView) itemView.findViewById(R.id.tv_item_market_research_code);
            mView = (ImageView) itemView.findViewById(R.id.iv_item_market_research_add);
            itemView.setOnClickListener(this);
            mView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            boolean isNext = false;
            switch (v.getId()){
                case R.id.iv_item_market_research_add:
                    if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
                        Intent intent = new Intent(mContext, ActivityLogin.class);
                        mContext.startActivity(intent);
                        return;
                    }
                    isNext = true;
                    if(mClickListener !=null){
                        mClickListener.onItemClick(v,getPosition(),mDatas.get(getPosition()));
                        mView.setImageResource(R.drawable.icon_add_ok);
                        mDatas.get(getPosition()).setAddSuccess(true);
                    }
                    break;
            }
            if(!isNext){
                if (mListener != null) {
                    mListener.onItemClick(v, getPosition(),mDatas.get(getPosition()));
                }
            }
        }

        public void setRefreshData(SimulationSearchInfo bean) {
            if(isSimulation){
                mView.setVisibility(View.GONE);
            }else{
                mView.setVisibility(View.VISIBLE);
            }
            if(bean.isAddSuccess()){
                mView.setImageResource(R.drawable.icon_add_ok);
            }else {
                mView.setImageResource(R.drawable.icon_add_stock);
            }
            if(bean.zixuan_status.equals("1")){
                bean.setAddSuccess(true);
                mView.setImageResource(R.drawable.icon_add_ok);
            }else {
                bean.setAddSuccess(false);
                mView.setImageResource(R.drawable.icon_add_stock);
            }
            type.setText(bean.mb_name);
            name.setText(bean.mg_name);
            code.setText(bean.mg_code);
        }
    }
}
