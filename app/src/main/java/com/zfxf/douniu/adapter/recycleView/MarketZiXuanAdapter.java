package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.SimulationInfo;

import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class MarketZiXuanAdapter extends RecyclerView.Adapter<MarketZiXuanAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<SimulationInfo> mDatas;
    private StringBuilder mStringBuilder;
    private boolean isDeleteing = false;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public MarketZiXuanAdapter(Context context, List<SimulationInfo> datas) {
        mContext = context;
        mDatas = datas;
        mStringBuilder = new StringBuilder();
        mStringBuilder.delete(0,mStringBuilder.length());
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_market_zixuan,null);
        MyHolder myHolder = new MyHolder(view , mItemClickListener);
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
    public void addDatas(List<SimulationInfo> data) {
        mDatas.addAll(data);
    }
    public String getDeleteCode(){
        return mStringBuilder.toString();
    }
    public void setState(boolean bool){
        isDeleteing = bool;
    }
    public void selectAll(boolean type){
        mStringBuilder.delete(0,mStringBuilder.length());
        for(int i = 0;i<mDatas.size();i++){
            mDatas.get(i).setSelect(type);
            mStringBuilder.append(mDatas.get(i).mg_code+",");
        }
        if(type){
            count = mDatas.size();
        }else {
            count = 0;
        }
        notifyDataSetChanged();
    }
    private int count = 0;
    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        TextView name;
        TextView ratio;//最新市值
        TextView price;//现价
        ImageView mImageView;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            name = (TextView) itemView.findViewById(R.id.tv_item_market_market_name);
            price = (TextView) itemView.findViewById(R.id.tv_item_market_market_price);
            ratio = (TextView) itemView.findViewById(R.id.tv_item_market_market_ratio);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_item_market_market);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                if(isDeleteing){//删除的时候不能显示点击事件
                    return;
                }
                if(mDatas.get(getPosition()).isSelect){
                    int index = mStringBuilder.indexOf(mDatas.get(getPosition()).mg_code + ",");
                    mStringBuilder.delete(index,index+7);
                    count--;
                }else {
                    mStringBuilder.append(mDatas.get(getPosition()).mg_code + ",");
                    count++;
                }
                mDatas.get(getPosition()).setSelect(!mDatas.get(getPosition()).isSelect);
                mListener.onItemClick(v, count);
                notifyDataSetChanged();
            }
        }

        public void setRefreshData(SimulationInfo bean) {
            name.setText(bean.mg_name+"\n"+bean.mg_code);
            price.setText(bean.mg_xj);
            ratio.setText(bean.mg_zfz);
            if(bean.mg_zfz.contains("+")){
                ratio.setTextColor(mContext.getResources().getColor(R.color.colorRise));
            }else if(bean.mg_zfz.contains("停牌")){
                ratio.setTextColor(mContext.getResources().getColor(R.color.colorGray));
            }else {
                ratio.setTextColor(mContext.getResources().getColor(R.color.colorFall));
            }
            if(bean.isSelect){
                mImageView.setImageResource(R.drawable.my_select);
            }else {
                mImageView.setImageResource(R.drawable.my_noselect);
            }
        }
    }
}
