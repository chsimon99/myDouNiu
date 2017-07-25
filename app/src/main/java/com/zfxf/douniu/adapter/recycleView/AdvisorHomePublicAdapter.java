package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.internet.NewsInternetRequest;

import java.util.List;
import java.util.Map;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class AdvisorHomePublicAdapter extends RecyclerView.Adapter<AdvisorHomePublicAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private MySubscribeClickListener mSubscribeClickListener = null;
    private List<Map<String, String>> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int id,int isYd);
    }
    public interface MySubscribeClickListener {
        void onItemClick(View v, int id, String type);
    }

    public AdvisorHomePublicAdapter(Context context, List<Map<String, String>> datas) {
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
        View view = View.inflate(mContext, R.layout.item_advisor_home_public,null);
        MyHolder myHolder = new MyHolder(view , mItemClickListener,mSubscribeClickListener);
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
        private MySubscribeClickListener mSubListener;
        private ImageView img;
        private TextView title;
        private TextView time;
        private TextView myCount;
        private TextView subscribe;
        private LinearLayout ll;

        public MyHolder(View itemView, MyItemClickListener listener,MySubscribeClickListener subListener) {
            super(itemView);
            this.mListener = listener;
            mSubListener = subListener;
            img = (ImageView) itemView.findViewById(R.id.iv_advisor_home_public_img);
            title = (TextView) itemView.findViewById(R.id.tv_advisor_home_public_title);
            time = (TextView) itemView.findViewById(R.id.tv_advisor_home_public_day);
            myCount = (TextView) itemView.findViewById(R.id.tv_advisor_home_public_count);
            subscribe = (TextView) itemView.findViewById(R.id.tv_advisor_home_public_subscribe);
            ll = (LinearLayout) itemView.findViewById(R.id.ll_advisor_home_public_subscribe);

            subscribe.getPaint().setFakeBoldText(true);//加粗
            ll.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            boolean isNext = false;
            switch (v.getId()) {
                case R.id.ll_advisor_home_public_subscribe:
                    isNext = true;
                    if (subscribe.getText().toString().equals("预约")) {
                        subscribe.setText("已预约");
                        subscribe.setBackgroundResource(R.drawable.backgroud_button_gary_color);
                    } else {
                        subscribe.setText("预约");
                        subscribe.setBackgroundResource(R.drawable.backgroud_button_app_color);
                    }
                    if(mSubListener !=null){
                        mSubListener.onItemClick(v,Integer.parseInt(mDatas.get(getPosition()).get("cc_id"))
                                ,subscribe.getText().toString());
                        if (subscribe.getText().toString().equals("已预约")) {
                            subscribeInternet(0);
                        } else {
                            subscribeInternet(1);
                        }
                    }
                    break;
            }
            if(!isNext){
                if (mListener != null) {
                    mListener.onItemClick(v, Integer.parseInt(mDatas.get(getPosition()).get("cc_id")),
                            Integer.parseInt(mDatas.get(getPosition()).get("has_dy")));
                }
            }
        }
        private void subscribeInternet(int type) {
            NewsInternetRequest.subscribeAndCannel(mDatas.get(getPosition()).get("cc_id"), 0, type, new NewsInternetRequest.ForResultListener() {
                @Override
                public void onResponseMessage(String count) {
                    myCount.setText(count);
                }
            }, mContext.getResources().getString(R.string.userdy));
        }
        public void setRefreshData(Map<String, String> bean, int position) {
            String picUrl = mContext.getResources().getString(R.string.file_host_address)
                    +mContext.getResources().getString(R.string.showpic)
                    +bean.get("cc_fielid");
            Glide.with(mContext).load(picUrl)
                    .placeholder(R.drawable.public_img).into(img);
            title.setText(bean.get("cc_title"));
            myCount.setText(bean.get("dy_count"));
            time.setText(bean.get("cc_datetime"));
            if(bean.get("has_dy").equals("0")){
                subscribe.setText("预约");
                subscribe.setBackgroundResource(R.drawable.backgroud_button_app_color);
            }else{
                subscribe.setText("已预约");
                subscribe.setBackgroundResource(R.drawable.backgroud_button_gary_color);
            }
        }
    }
}
