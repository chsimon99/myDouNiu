package com.zfxf.douniu.adapter.viewPager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.LunBoListInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Admin
 * @time 2017/3/30 9:32
 * @des ${TODO}
 */

public class PicPagerAdapter extends PagerAdapter {
    private List<LunBoListInfo> mDatas = new ArrayList<LunBoListInfo>();
    private Context mContext;
    private MyOnClickListener mListener;

    public PicPagerAdapter(List<LunBoListInfo> datas, Context context, MyOnClickListener listener) {
        mDatas = datas;
        mContext = context;
        mListener = listener;
    }

    @Override
    public int getCount() {
        if (mDatas != null) {
            return Integer.MAX_VALUE;
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        int pos = position % mDatas.size();
        ImageView iv = new ImageView(mContext);
        String picUrl = mContext.getResources().getString(R.string.file_host_address)
                +mContext.getResources().getString(R.string.showpic)
                +mDatas.get(pos).image;
        Glide.with(mContext).load(picUrl)
                .placeholder(R.drawable.home_banner).into(iv);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        container.addView(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onItemClick(position % mDatas.size());
                }
            }
        });
        return iv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface MyOnClickListener {
        void onItemClick(int positon);
    }
    public void setOnMyClickListener(MyOnClickListener listener) {
        mListener = listener;
    }
}
