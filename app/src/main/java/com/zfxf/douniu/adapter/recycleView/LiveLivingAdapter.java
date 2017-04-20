package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;

import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class LiveLivingAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<String> mDatas;

    private int living_text = 1;
    private int living_sound = 2;


    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public LiveLivingAdapter(Context context, List<String> datas) {
        mContext = context;
        mDatas = datas;
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
    public void addDatas(String data) {
        mDatas.add(data);
    }

    @Override
    public int getItemViewType(int position) {
        if(position % 2 == 0){
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
                mListener.onItemClick(v, getPosition());
            }
        }

        public void setRefreshData(String bean, int position) {

        }
    }
    class MySoundHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        TextView sound_time;
        TextView time;
        FrameLayout mLayout;
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
            mParams =  mLayout.getLayoutParams();

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        public void setRefreshData(String bean, int position) {
            if(position %3 ==0){
                mParams.width = mMinItemWidth;
            }else if(position %5 ==0){
                mParams.width = (int) (mMaxItemWidth *0.8);
            }else{
                mParams.width = mMinItemWidth*2;
            }
        }
    }
}
