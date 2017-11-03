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
import com.zfxf.douniu.bean.NewsNewsResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class BarShipinAdapter extends RecyclerView.Adapter<BarShipinAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<NewsNewsResult> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public BarShipinAdapter(Context context, List<NewsNewsResult> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_bar_shipin, null);
        MyHolder myHolder = new MyHolder(view, mItemClickListener);
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
    public void addDatas(List<NewsNewsResult> data) {
        mDatas.addAll(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        ImageView img;
        TextView title;
        TextView name;
        TextView count;
        JCVideoPlayerStandard videoplayer;
        LinearLayout ll_zan;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            img = (ImageView) itemView.findViewById(R.id.iv_bar_shipin_img);
            videoplayer = (JCVideoPlayerStandard) itemView.findViewById(R.id.vp_bar_shipin);

            title = (TextView) itemView.findViewById(R.id.tv_bar_shipin_title);
            name = (TextView) itemView.findViewById(R.id.tv_bar_shipin_name);
            count = (TextView) itemView.findViewById(R.id.tv_bar_shipin_count);
            ll_zan = (LinearLayout) itemView.findViewById(R.id.ll_bar_shipin_zan);
            title.getPaint().setFakeBoldText(true);//加粗
            itemView.setOnClickListener(this);
            ll_zan.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_bar_shipin_zan:
                    if(mDatas.get(getPosition()).has_dz.equals("1")){
                        CommonUtils.toastMessage("您已经点过赞了");
                        break;
                    }
                    goToDianZan(mDatas.get(getPosition()).ov_id,0,getPosition());
                    if (mListener != null) {
                        mListener.onItemClick(v, getPosition());
                    }
                break;
            }
        }

        public void setRefreshData(NewsNewsResult bean) {
//            String shipinUrl = mContext.getResources().getString(R.string.file_host_address)
//                    +mContext.getResources().getString(R.string.showpic)
//                    +bean.ov_file;
            String picUrl = mContext.getResources().getString(R.string.file_host_address)
                    +mContext.getResources().getString(R.string.showpic)
                    +bean.ov_pic;
            videoplayer.setUp(bean.ov_file, JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "",bean.dz_count+"次观看","");
            Glide.with(mContext)
                    .load(picUrl)
                    .into(videoplayer.thumbImageView);

            String imgUrl = mContext.getResources().getString(R.string.file_host_address)
                    +mContext.getResources().getString(R.string.showpic)
                    +bean.headImg;
            Glide.with(mContext).load(imgUrl)
                    .placeholder(R.drawable.home_adviosr_img)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(img);

            title.setText(bean.ov_title);
            count.setText(bean.has_dz);
            name.setText(bean.ud_nickname);
        }
        private void goToDianZan(String id, int action, final int position) {
            NewsInternetRequest.MatadorShipinsubscribeAndCannel(id, action, new NewsInternetRequest.ForResultListener() {
                @Override
                public void onResponseMessage(String counts) {
                    count.setText(counts);
                    mDatas.get(position).setHas_dz("1");
                }
            });
        }
    }
}
