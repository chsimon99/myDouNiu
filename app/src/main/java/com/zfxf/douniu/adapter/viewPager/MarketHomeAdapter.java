package com.zfxf.douniu.adapter.viewPager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.SimulationInfo;
import com.zfxf.douniu.utils.CommonUtils;

import java.util.List;

/**
 * @author IMXU
 * @time 2017/8/4 10:52
 * @des ${TODO}
 * 邮箱：butterfly_xu@sina.com
 */

public class MarketHomeAdapter extends PagerAdapter {

    private List<LinearLayout> mDatas;
    private List<SimulationInfo> mInfos;

    public MarketHomeAdapter(List<LinearLayout> datas, List<SimulationInfo> infos) {
        mDatas = datas;
        mInfos = infos;
    }
    @Override
    public int getCount() {
        return mDatas.size();
    }
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LinearLayout layout = mDatas.get(position);
        TextView tv_name = (TextView) layout.findViewById(R.id.tv_market_home_name_item1);
        TextView tv_index = (TextView) layout.findViewById(R.id.tv_market_home_index_item1);
        TextView tv_price = (TextView) layout.findViewById(R.id.tv_market_home_price_item1);
        TextView tv_ratio = (TextView) layout.findViewById(R.id.tv_market_home_ratio_item1);
        SimulationInfo info = mInfos.get(0+position);
        if(info.mg_cj.contains("+")){
            tv_index.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.colorRise));
            tv_price.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.colorRise));
            tv_ratio.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.colorRise));
        }else {
            tv_index.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.colorFall));
            tv_price.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.colorFall));
            tv_ratio.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.colorFall));
        }
        tv_name.setText(info.mg_name);
        tv_index.setText(info.Trade_price);
        tv_price.setText(info.mg_cj);
        tv_ratio.setText(info.mg_zfz);
        layout.findViewById(R.id.ll_market_market_item1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener !=null){
                    mListener.onItemClick(0+position);
                }
            }
        });

        TextView tv_name2 = (TextView) layout.findViewById(R.id.tv_market_home_name_item2);
        TextView tv_index2 = (TextView) layout.findViewById(R.id.tv_market_home_index_item2);
        TextView tv_price2 = (TextView) layout.findViewById(R.id.tv_market_home_price_item2);
        TextView tv_ratio2 = (TextView) layout.findViewById(R.id.tv_market_home_ratio_item2);
        SimulationInfo info1 = mInfos.get(2+position);
        if(info1.mg_cj.contains("+")){
            tv_index2.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.colorRise));
            tv_price2.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.colorRise));
            tv_ratio2.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.colorRise));
        }else {
            tv_index2.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.colorFall));
            tv_price2.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.colorFall));
            tv_ratio2.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.colorFall));
        }
        tv_name2.setText(info1.mg_name);
        tv_index2.setText(info1.Trade_price);
        tv_price2.setText(info1.mg_cj);
        tv_ratio2.setText(info1.mg_zfz);
        layout.findViewById(R.id.ll_market_market_item2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener !=null){
                    mListener.onItemClick(2+position);
                }
            }
        });

        TextView tv_name3 = (TextView) layout.findViewById(R.id.tv_market_home_name_item3);
        TextView tv_index3 = (TextView) layout.findViewById(R.id.tv_market_home_index_item3);
        TextView tv_price3 = (TextView) layout.findViewById(R.id.tv_market_home_price_item3);
        TextView tv_ratio3 = (TextView) layout.findViewById(R.id.tv_market_home_ratio_item3);
        SimulationInfo info3 = mInfos.get(4+position);
        if(info3.mg_cj.contains("+")){
            tv_index3.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.colorRise));
            tv_price3.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.colorRise));
            tv_ratio3.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.colorRise));
        }else {
            tv_index3.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.colorFall));
            tv_price3.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.colorFall));
            tv_ratio3.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.colorFall));
        }
        tv_name3.setText(info3.mg_name);
        tv_index3.setText(info3.Trade_price);
        tv_price3.setText(info3.mg_cj);
        tv_ratio3.setText(info3.mg_zfz);
        layout.findViewById(R.id.ll_market_market_item3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener !=null){
                    mListener.onItemClick(4+position);
                }
            }
        });
        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    public interface MyOnClickListener {
        void onItemClick(int positon);
    }
    public void setOnMyClickListener(MyOnClickListener listener) {
        mListener = listener;
    }
    private MyOnClickListener mListener;
}
