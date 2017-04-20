package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.viewPager.PicPagerAdapter;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.MyLunBo;
import com.zfxf.douniu.view.InnerView;

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
    private List<Integer> mLunboDatas;
    private View mHeaderView;
    private MyLunBo mMyLunBO;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }
    public interface MySubscribeClickListener {
        void onItemClick(View v, int positon, String type);
    }

    public AdvisorAllPublicAdapter(Context context, List<String> datas,List<Integer> lunboDatas) {
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
        View view = View.inflate(mContext, R.layout.item_advisor_all_public, null);
        MyHolder myHolder = new MyHolder(view, mItemClickListener,mSubscribeClickListener);
        if(mHeaderView != null && viewType == TYPE_HEADER)
            return new MyHolder(mHeaderView , mItemClickListener , mSubscribeClickListener);
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
    public void addDatas(String data) {
        mDatas.add(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        private MySubscribeClickListener mSubListener;
        private PicPagerAdapter mPagerAdapter;
        InnerView mViewPage;
        ImageView img;
        TextView time;
        TextView count;
        TextView title;
        TextView from;
        TextView day;
        TextView type;
        LinearLayout mLayout;
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

        public void setRefreshLunboData(List<Integer> datas, int position) {
            if(mPagerAdapter ==null){
                mPagerAdapter = new PicPagerAdapter(datas, mContext, new PicPagerAdapter.MyOnClickListener() {
                    @Override
                    public void onItemClick(int positon) {
                        CommonUtils.toastMessage("您点击的是第 " + (++positon) + " 个Item");
                    }
                });
                mViewPage.setAdapter(mPagerAdapter);
            }
            if(mMyLunBO == null){
                mMyLunBO = new MyLunBo(mLLayout, mViewPage, datas);
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
