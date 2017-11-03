package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.login.ActivityLogin;
import com.zfxf.douniu.adapter.viewPager.PicPagerAdapter;
import com.zfxf.douniu.bean.LunBoListInfo;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.MyLunBo;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.InnerView;

import java.util.List;
import java.util.Map;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class AdvisorAllPublicAdapter extends RecyclerView.Adapter<AdvisorAllPublicAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private MySubscribeClickListener mSubscribeClickListener = null;
    private List<Map<String, String>> mDatas;
    private List<LunBoListInfo> mLunboDatas;
    private View mHeaderView;
    private MyLunBo mMyLunBO;

    public interface MyItemClickListener {
        void onItemClick(View v, int id,int isYd);
    }
    public interface MySubscribeClickListener {
        void onItemClick(View v, int id, String type);
    }

    public AdvisorAllPublicAdapter(Context context, List<Map<String, String>> datas, List<LunBoListInfo> lunboDatas) {
        mContext = context;
        mDatas = datas;
        mLunboDatas = lunboDatas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }
    public void setOnSubscribeClickListener(MySubscribeClickListener listener) {
        this.mSubscribeClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER)
            return new MyHolder(mHeaderView , mItemClickListener , mSubscribeClickListener);
        View view = View.inflate(mContext, R.layout.item_advisor_all_public, null);
        MyHolder myHolder = new MyHolder(view, mItemClickListener,mSubscribeClickListener);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        if(getItemViewType(position) == TYPE_HEADER) {
            holder.setRefreshLunboData(mLunboDatas, position);
            return;
        }
        int pos = getRealPosition(holder);
        holder.setRefreshData(mDatas.get(pos), pos);
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ?mDatas.size() : mDatas.size()+1;
    }
    public void addDatas(List<Map<String, String>> data) {
        mDatas.addAll(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        private MySubscribeClickListener mSubListener;
        ImageView img;
        TextView time;
        TextView myCount;
        TextView title;
        TextView from;
        TextView type;
        LinearLayout mLayout;
        InnerView mViewPage;
        private PicPagerAdapter mPagerAdapter;
        private LinearLayout mLLayout;

        public MyHolder(View itemView, MyItemClickListener listener,MySubscribeClickListener subListener) {
            super(itemView);
            this.mListener = listener;
            mSubListener = subListener;

            if(itemView == mHeaderView){
                mViewPage = (InnerView) itemView.findViewById(R.id.inwerview);
                mLLayout = (LinearLayout) itemView.findViewById(R.id.item_home_pic_ll);
                return;
            }

            img = (ImageView) itemView.findViewById(R.id.iv_advisor_all_public_img);
            title = (TextView) itemView.findViewById(R.id.tv_advisor_all_public_title);
            from = (TextView) itemView.findViewById(R.id.tv_advisor_all_public_from);
            time = (TextView) itemView.findViewById(R.id.tv_advisor_all_public_time);
            myCount = (TextView) itemView.findViewById(R.id.tv_advisor_all_public_count);
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
                    if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
                        Intent intent = new Intent(mContext, ActivityLogin.class);
                        mContext.startActivity(intent);
                        return;
                    }
                    isNext = true;
                    if(mSubListener !=null){
                        mSubListener.onItemClick(v,Integer.parseInt(mDatas.get(getRealPosition(this)).get("cc_id"))
                                ,type.getText().toString());
                        if (type.getText().toString().equals("已订阅")) {
                            subscribeInternet(1);
                        } else {
                            subscribeInternet(0);
                        }
                    }
                    break;
            }
            if(!isNext){
                if (mListener != null) {
                    mListener.onItemClick(v, Integer.parseInt(mDatas.get(getRealPosition(this)).get("cc_id")),
                            Integer.parseInt(mDatas.get(getRealPosition(this)).get("has_dy")));
                }
            }

        }

        private void subscribeInternet(int mtype) {
            NewsInternetRequest.subscribeAndCannel(mDatas.get(getRealPosition(this)).get("cc_id")
                    , 0, mtype, new NewsInternetRequest.ForResultListener() {
                        @Override
                        public void onResponseMessage(String count) {
                            if (type.getText().toString().equals("订阅")) {
                                type.setText("已订阅");
                                CommonUtils.toastMessage("订阅成功");
                                type.setBackgroundResource(R.drawable.backgroud_button_gary_color);
                            } else {
                                type.setText("订阅");
                                CommonUtils.toastMessage("取消订阅成功");
                                type.setBackgroundResource(R.drawable.backgroud_button_app_color);
                            }
                            myCount.setText(count);
                        }
                    },mContext.getResources().getString(R.string.userdy));
        }

        public void setRefreshData(Map<String, String> bean, int position) {
            String picUrl = mContext.getResources().getString(R.string.file_host_address)
                    +mContext.getResources().getString(R.string.showpic)
                    +bean.get("cc_fielid");
            Glide.with(mContext).load(picUrl)
                    .placeholder(R.drawable.public_img).into(img);
            title.setText(bean.get("cc_title"));
            from.setText(bean.get("cc_auth"));
            myCount.setText(bean.get("dy_count"));
            time.setText(bean.get("cc_datetime"));
            if(bean.get("has_dy").equals("0")){
                type.setText("订阅");
                type.setBackgroundResource(R.drawable.backgroud_button_app_color);
            }else{
                type.setText("已订阅");
                type.setBackgroundResource(R.drawable.backgroud_button_gary_color);
            }
        }

        public void setRefreshLunboData(List<LunBoListInfo> datas, int position) {
            if(mPagerAdapter ==null){
                mPagerAdapter = new PicPagerAdapter(datas, mContext, new PicPagerAdapter.MyOnClickListener() {
                    @Override
                    public void onItemClick(int positon) {
//                        CommonUtils.toastMessage("您点击的是第 " + (++positon) + " 个Item");
                    }
                });
                mViewPage.setAdapter(mPagerAdapter);
            }
            if(mMyLunBO == null){
                mMyLunBO = new MyLunBo(mLLayout, mViewPage, mLunboDatas.size());
                mMyLunBO.startLunBO();
            }
        }
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }
    public View getHeaderView() {
        return mHeaderView;
    }
    public MyLunBo getLunBo() {
        return mMyLunBO;
    }
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null) return TYPE_NORMAL;
        if(position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }
    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }
}
