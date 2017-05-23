package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.XuanguGupiaoDetail;

import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class XuanGuAdapter extends RecyclerView.Adapter<XuanGuAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<XuanguGupiaoDetail> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public XuanGuAdapter(Context context, List<XuanguGupiaoDetail> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_xuangu,null);
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
    public void changeDatas(List<XuanguGupiaoDetail> gupiao_list) {
        mDatas = gupiao_list;
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        TextView name;
        TextView ratio;//最新市值
        TextView price;//入选价
        TextView nowprice;//现价

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            name = (TextView) itemView.findViewById(R.id.tv_item_xuangu_name);
            price = (TextView) itemView.findViewById(R.id.tv_item_xuangu_price);
            nowprice = (TextView) itemView.findViewById(R.id.tv_item_xuangu_nowprice);
            ratio = (TextView) itemView.findViewById(R.id.tv_item_xuangu_ratio);
//            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        public void setRefreshData(XuanguGupiaoDetail bean) {
            name.setText(bean.zg_mg_name+"\n"+bean.zg_mg_code);
            price.setText("/"+bean.zg_rxj);
            nowprice.setText(bean.zg_rxj);
            ratio.setText(bean.zg_zgzf+"%");
        }
    }
}
