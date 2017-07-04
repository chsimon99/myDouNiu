package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.LivingContent;
import com.zfxf.douniu.bean.LivingContentDetail;
import com.zfxf.douniu.bean.LivingContentDetailType;

import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class LiveLivingAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<LivingContentDetail> mDatas;

    private int living_text = 1;
    private int living_sound = 2;


    public interface MyItemClickListener {
        void onItemClick(View v, int positon , LivingContentDetailType type ,ImageView view);
    }

    public LiveLivingAdapter(Context context, LivingContent datas) {
        mContext = context;
        mDatas = datas.context_list;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MySoundHolder mySoundHolder = null;
        MyTextHolder myTextHolder = null;
        if(viewType == living_text){
            View view = View.inflate(mContext, R.layout.item_live_living_text, null);
            myTextHolder = new MyTextHolder(view, mItemClickListener);
            return myTextHolder;
        }else{
            View view = View.inflate(mContext, R.layout.item_live_living_sound, null);
            mySoundHolder = new MySoundHolder(view,mItemClickListener);
            return mySoundHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        holder.setRefreshData(mDatas.get(position), position);
        if(holder instanceof  MyTextHolder){
            ((MyTextHolder) holder).setRefreshData(mDatas.get(position),position);
        }else if(holder instanceof MySoundHolder){
            ((MySoundHolder) holder).setRefreshData(mDatas.get(position),position);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
    public void addDatas(LivingContent data) {
        mDatas.addAll(data.context_list);
    }
    public void refreshDatas(LivingContent data) {
        mDatas.addAll(0,data.context_list);
    }

    @Override
    public int getItemViewType(int position) {
        if(mDatas.get(position).zc_context.type.equals("0")){
            return living_text;
        }else{
            return living_sound;
        }
    }

    class MyTextHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        TextView content;
        TextView time;

        public MyTextHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            time = (TextView) itemView.findViewById(R.id.tv_live_living_text_time);
            content = (TextView) itemView.findViewById(R.id.tv_live_living_text_content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition(),mDatas.get(getPosition()).zc_context,null);
            }
        }

        public void setRefreshData(LivingContentDetail bean, int position) {
            time.setText(bean.zc_time);
            content.setText(bean.zc_context.text);
        }
    }
    class MySoundHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        TextView sound_time;
        TextView time;
        FrameLayout mLayout;
        ImageView mView;
        ViewGroup.LayoutParams mParams;
        private int mMinItemWidth; //最小的item宽度
        private int mMaxItemWidth; //最大的item宽度

        public MySoundHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetrics);
            mMaxItemWidth = (int) (outMetrics.widthPixels * 0.7f);
            mMinItemWidth = (int) (outMetrics.widthPixels * 0.15f);

            sound_time = (TextView) itemView.findViewById(R.id.tv_live_living_sound_soundtime);
            time = (TextView) itemView.findViewById(R.id.tv_live_living_sound_time);
            mLayout = (FrameLayout) itemView.findViewById(R.id.fl_live_living_sound);
            mView = (ImageView) itemView.findViewById(R.id.iv_live_living_sound);
            mParams =  mLayout.getLayoutParams();

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition(),mDatas.get(getPosition()).zc_context,mView);
            }
        }

        public void setRefreshData(LivingContentDetail bean, int position) {
            time.setText(bean.zc_time);
            sound_time.setText(bean.zc_context.length);
            String length = bean.zc_context.length;
            if(length.contains("’")){//超过一分钟的就一样显示
                mParams.width = (int) (mMaxItemWidth *0.9);
            }else{//低于一分钟的按比例显示
                int i = length.indexOf("”");
                String str = length.substring(0, i);
                int addLength = (int) ((mMaxItemWidth*0.8 - mMinItemWidth) * Integer.parseInt(str) / 60);
                mParams.width = mMinItemWidth +addLength;
            }
            if(bean.zc_context.isShow()){
                startAnimation(mView);
            }else {
                stopAnimation(mView);
            }
        }
        private AnimationDrawable voiceAnimation = null;
        public void stopAnimation(ImageView view){
            if(voiceAnimation !=null){
                voiceAnimation.stop();
            }
            view.setImageResource(R.drawable.icon_sound);
        }
        public void startAnimation(ImageView view){
            view.setImageResource(R.drawable.voiceanimation);
            voiceAnimation = (AnimationDrawable) view.getDrawable();
            voiceAnimation.start();
        }
    }
}
