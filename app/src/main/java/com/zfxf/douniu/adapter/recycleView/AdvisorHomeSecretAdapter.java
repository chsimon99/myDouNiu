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

public class AdvisorHomeSecretAdapter extends RecyclerView.Adapter<AdvisorHomeSecretAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<Map<String, String>> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public AdvisorHomeSecretAdapter(Context context, List<Map<String, String>> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_advisor_home_secret,null);
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
        private ImageView img;
        private TextView title;
        private TextView time;
        private TextView myCount;
        private TextView money;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            img = (ImageView) itemView.findViewById(R.id.iv_advisor_home_secret_img);
            title = (TextView) itemView.findViewById(R.id.tv_advisor_home_secret_title);
            time = (TextView) itemView.findViewById(R.id.tv_advisor_home_secret_time);
            myCount = (TextView) itemView.findViewById(R.id.tv_advisor_home_secret_count);
            money = (TextView) itemView.findViewById(R.id.tv_advisor_home_secret_money);

            money.getPaint().setFakeBoldText(true);//加粗
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, Integer.parseInt(mDatas.get(getPosition()).get("cc_id")));
            }
        }

        public void setRefreshData(Map<String, String> bean, int position) {
            String picUrl = mContext.getResources().getString(R.string.file_host_address)
                    +mContext.getResources().getString(R.string.showpic)
                    +bean.get("cc_fielid");
            Glide.with(mContext).load(picUrl)
                    .placeholder(R.drawable.public_img).into(img);
            title.setText(bean.get("cc_title"));
            myCount.setText(bean.get("buy_count"));
            time.setText(bean.get("cc_datetime"));
            money.setText("￥"+bean.get("cc_fee")+"元");
            if(bean.get("has_buy").equals("0")){
                money.setTextColor(mContext.getResources().getColor(R.color.colorTitle));
            }else{
                money.setTextColor(mContext.getResources().getColor(R.color.gray));
            }
        }
    }

}
