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
import com.zfxf.douniu.adapter.viewPager.PicPagerAdapter;
import com.zfxf.douniu.bean.LunBoListInfo;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.MyLunBo;
import com.zfxf.douniu.view.InnerView;

import java.util.List;
import java.util.Map;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class AdvisorAllSecretAdapter extends RecyclerView.Adapter<AdvisorAllSecretAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<Map<String, String>> mDatas;
    private List<LunBoListInfo> mLunboDatas;
    private View mHeaderView;
    private MyLunBo mMyLunBO;

    public interface MyItemClickListener {
        void onItemClick(View v, int id);
    }

    public AdvisorAllSecretAdapter(Context context, List<Map<String, String>> datas, List<LunBoListInfo> lunboDatas) {
        mContext = context;
        mDatas = datas;
        mLunboDatas = lunboDatas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER)
            return new MyHolder(mHeaderView , mItemClickListener);
        View view = View.inflate(mContext, R.layout.item_advisor_all_secret, null);
        MyHolder myHolder = new MyHolder(view, mItemClickListener);
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
        private ImageView img;
        private TextView title;
        private TextView from;
        private TextView time;
        private TextView myCount;
        private TextView money;
        LinearLayout mLayout;
        InnerView mViewPage;
        private PicPagerAdapter mPagerAdapter;
        private LinearLayout mLLayout;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;

            if(itemView == mHeaderView){
                mViewPage = (InnerView) itemView.findViewById(R.id.inwerview);
                mLLayout = (LinearLayout) itemView.findViewById(R.id.item_home_pic_ll);
                return;
            }
            img = (ImageView) itemView.findViewById(R.id.iv_advisor_all_secret_img);
            title = (TextView) itemView.findViewById(R.id.tv_advisor_all_secret_title);
            from = (TextView) itemView.findViewById(R.id.tv_advisor_all_secret_from);
            time = (TextView) itemView.findViewById(R.id.tv_advisor_all_secret_time);
            myCount = (TextView) itemView.findViewById(R.id.tv_advisor_all_secret_count);
            money = (TextView) itemView.findViewById(R.id.tv_advisor_all_secret_money);
            money.getPaint().setFakeBoldText(true);//加粗

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, Integer.parseInt(mDatas.get(getRealPosition(this)).get("cc_id")));
            }
        }

        public void setRefreshData(Map<String, String> bean, int position) {
            Glide.with(mContext).load(bean.get("cc_fielid"))
                    .placeholder(R.drawable.public_img).into(img);
            title.setText(bean.get("cc_title"));
            from.setText(bean.get("cc_auth"));
            myCount.setText(bean.get("buy_count"));
            time.setText(bean.get("cc_datetime"));
            money.setText("￥"+bean.get("cc_fee")+"元");
            if(bean.get("has_buy").equals("0")){
                money.setTextColor(mContext.getResources().getColor(R.color.colorTitle));
            }else{
                money.setTextColor(mContext.getResources().getColor(R.color.gray));
            }
        }
        public void setRefreshLunboData(List<LunBoListInfo> datas, int position) {
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
