package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;

import java.util.List;
import java.util.Map;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class AdvisorHomeReferenceAdapter extends RecyclerView.Adapter<AdvisorHomeReferenceAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<Map<String, String>> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int id, Map<String, String> buy);
    }

    public AdvisorHomeReferenceAdapter(Context context, List<Map<String, String>> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_advisor_home_reference,null);
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

    public void addDatas(List<Map<String, String>> data) {
        mDatas.addAll(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        ImageView img;
        ImageView lock;
        TextView count;
        TextView title;
        TextView price;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            img = (ImageView) itemView.findViewById(R.id.iv_advisor_home_reference_img);
            lock = (ImageView) itemView.findViewById(R.id.iv_advisor_home_reference_lock);
            title = (TextView) itemView.findViewById(R.id.tv_advisor_home_reference_title);
            count = (TextView) itemView.findViewById(R.id.tv_advisor_home_reference_count);
            price = (TextView) itemView.findViewById(R.id.tv_advisor_home_reference_price);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, Integer.parseInt(mDatas.get(getPosition()).get("cc_id"))
                        ,mDatas.get(getPosition()));
            }
        }

        public void setRefreshData(Map<String, String> bean, int position) {
            Glide.with(mContext).load(bean.get("cc_fielid"))
                    .placeholder(R.drawable.public_img).into(img);
            title.setText(bean.get("cc_title"));
            count.setText(bean.get("buy_count"));
            price.setText("￥"+bean.get("cc_fee"));
            if(bean.get("has_buy").equals("0")){
                lock.setVisibility(View.VISIBLE);
            }else{
                lock.setVisibility(View.INVISIBLE);
            }
        }
    }
}
