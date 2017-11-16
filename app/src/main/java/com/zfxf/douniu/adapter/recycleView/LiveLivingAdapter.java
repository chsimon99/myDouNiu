package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.LivingContent;
import com.zfxf.douniu.bean.LivingContentDetail;
import com.zfxf.douniu.bean.LivingContentDetailType;
import com.zfxf.douniu.bean.LivingContentLinks;
import com.zfxf.douniu.utils.CommonUtils;

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
    private int living_picture = 3;


    public interface MyItemClickListener {
        //LivingContentDetailType 直播的内容
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
        MyPictureHolder myPictureHolder = null;
        if(viewType == living_text){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_live_living_text, parent,false);
            myTextHolder = new MyTextHolder(view, mItemClickListener);
            return myTextHolder;
        }else if(viewType == living_sound){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_live_living_sound, parent,false);
            mySoundHolder = new MySoundHolder(view,mItemClickListener);
            return mySoundHolder;
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_live_living_picture, parent,false);
            myPictureHolder = new MyPictureHolder(view,mItemClickListener);
            return myPictureHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        holder.setRefreshData(mDatas.get(position), position);
        if(holder instanceof  MyTextHolder){
            ((MyTextHolder) holder).setRefreshData(mDatas.get(position),position);
        }else if(holder instanceof MySoundHolder){
            ((MySoundHolder) holder).setRefreshData(mDatas.get(position),position);
        }else if(holder instanceof MyPictureHolder){
            ((MyPictureHolder) holder).setRefreshData(mDatas.get(position),position);
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
        }else if(mDatas.get(position).zc_context.type.equals("2")){
            return living_picture;
        }else {
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
            if(bean.zc_context.links.size()>0){
                content.setText(getClickableSpan(bean.zc_context.links,bean.zc_context.text));
                //设置超链接可点击
                content.setMovementMethod(LinkMovementMethod.getInstance());
            }else {
                content.setText(bean.zc_context.text);
            }
            time.setText(bean.zc_time);

        }
        private SpannableString getClickableSpan(List<LivingContentLinks> links,String text){
            SpannableString spannableString = new SpannableString(text);
            for(int i = 0;i<links.size();i++){
                //设置下划线文字
                spannableString.setSpan(new UnderlineSpan(), Integer.parseInt(links.get(i).start), Integer.parseInt(links.get(i).end), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //设置文字的单击事件
                final int finalI = i;
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        if (mListener != null) {
                            mListener.onItemClick(widget, finalI,mDatas.get(getPosition()).zc_context,null);
                        }
                    }
                }, Integer.parseInt(links.get(i).start), Integer.parseInt(links.get(i).end), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //设置文字的前景色
                spannableString.setSpan(new ForegroundColorSpan(CommonUtils.getContext().getResources().
                        getColor(R.color.blueWeb)), Integer.parseInt(links.get(i).start),
                        Integer.parseInt(links.get(i).end), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            return spannableString;
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
    class MyPictureHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        TextView time;
        ImageView img;

        public MyPictureHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            time = (TextView) itemView.findViewById(R.id.tv_live_living_pic_time);
            img = (ImageView) itemView.findViewById(R.id.iv_live_living_pic_img);
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
            String picUrl = mContext.getResources().getString(R.string.file_host_address)
                    +mContext.getResources().getString(R.string.showpic)
                    +bean.zc_context.url;
            Glide.with(mContext).load(picUrl).into(img);
        }
    }
}
