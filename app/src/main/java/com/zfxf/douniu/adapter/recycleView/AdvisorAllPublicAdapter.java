package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;

import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class AdvisorAllPublicAdapter extends RecyclerView.Adapter<AdvisorAllPublicAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private MySubscribeClickListener mSubscribeClickListener = null;
    private List<String> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }
    public interface MySubscribeClickListener {
        void onItemClick(View v, int positon, String type);
    }

    public AdvisorAllPublicAdapter(Context context, List<String> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }
    public void setOnSubscribeClickListener(MySubscribeClickListener listener) {
        this.mSubscribeClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_advisor_all_public, null);
        MyHolder myHolder = new MyHolder(view, mItemClickListener,mSubscribeClickListener);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.setRefreshData(mDatas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
    public void addDatas(String data) {
        mDatas.add(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        private MySubscribeClickListener mSubListener;
        ImageView img;
        TextView time;
        TextView count;
        TextView title;
        TextView from;
        TextView day;
        TextView type;
        LinearLayout mLayout;

        public MyHolder(View itemView, MyItemClickListener listener
                ,MySubscribeClickListener subListener) {
            super(itemView);
            this.mListener = listener;
            mSubListener = subListener;
            img = (ImageView) itemView.findViewById(R.id.iv_advisor_all_public_img);
            title = (TextView) itemView.findViewById(R.id.tv_advisor_all_public_title);
            from = (TextView) itemView.findViewById(R.id.tv_advisor_all_public_from);
            day = (TextView) itemView.findViewById(R.id.tv_advisor_all_public_day);
            time = (TextView) itemView.findViewById(R.id.tv_advisor_all_public_time);
            count = (TextView) itemView.findViewById(R.id.tv_advisor_all_public_count);
            type = (TextView) itemView.findViewById(R.id.tv_advisor_all_public_type);
            mLayout = (LinearLayout) itemView.findViewById(R.id.ll_advisor_all_public);
            type.getPaint().setFakeBoldText(true);//加粗

            itemView.setOnClickListener(this);
            type.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            boolean isNext = false;
            switch (v.getId()) {
                case R.id.tv_advisor_all_public_type:
                    isNext = true;
                    if (type.getText().toString().equals("预约")) {
                        type.setText("已预约");
                        type.setBackgroundResource(R.drawable.backgroud_button_gary_color);
                    } else {
                        type.setText("预约");
                        type.setBackgroundResource(R.drawable.backgroud_button_app_color);
                    }
                    if(mSubListener !=null){
                        mSubListener.onItemClick(v,getPosition(),type.getText().toString());
                    }
                    break;
            }
            if(!isNext){
                if (mListener != null) {
                    mListener.onItemClick(v, getPosition());
                }
            }

        }

        public void setRefreshData(String bean, int position) {

        }
    }
}
