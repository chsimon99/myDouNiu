package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.StockChiInfo;

import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class AdvisorHomeGoldAdapter extends RecyclerView.Adapter<AdvisorHomeGoldAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<StockChiInfo> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int id,int jgc);
    }

    public AdvisorHomeGoldAdapter(Context context, List<StockChiInfo> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_advisor_home_gold,null);
        MyHolder myHolder = new MyHolder(view , mItemClickListener);
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

    public void addDatas(List<StockChiInfo> data) {
        mDatas.addAll(data);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        private TextView title;//标题
        private TextView type;//类型
        private TextView count;//订阅人数
        private TextView item1;//展示股票
        private TextView item2;
        private TextView item3;
        private TextView range1;//涨跌幅
        private TextView range2;
        private TextView range3;
        private TextView timeIn1;//买入时间
        private TextView timeIn2;
        private TextView timeIn3;
        private TextView timeOut1;//卖出时间
        private TextView timeOut2;
        private TextView timeOut3;
        private TextView quantity;//股池数量

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            title = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_title);
            type = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_type);
            count = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_count);
            item1 = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_item1);
            item2 = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_item2);
            item3 = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_item3);
            range1 = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_range1);
            range2 = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_range2);
            range3 = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_range3);
            timeIn1 = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_time_in1);
            timeIn2 = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_time_in2);
            timeIn3 = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_time_in3);
            timeOut1 = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_time_out1);
            timeOut2 = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_time_out2);
            timeOut3 = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_time_out3);
            quantity = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_quantity);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, Integer.parseInt(mDatas.get(getPosition()).ud_ub_id)
                        ,Integer.parseInt(mDatas.get(getPosition()).djf_id));
            }
        }

        public void setRefreshData(StockChiInfo bean, int position) {
            title.setText(bean.ud_nickname+"的金股池");
            if(bean.gu_piao.size() == 1){
                item2.setVisibility(View.GONE);
                range2.setVisibility(View.GONE);
                timeIn2.setVisibility(View.GONE);
                timeOut2.setVisibility(View.GONE);
                item3.setVisibility(View.GONE);
                range3.setVisibility(View.GONE);
                timeIn3.setVisibility(View.GONE);
                timeOut3.setVisibility(View.GONE);
                item1.setText(bean.gu_piao.get(0).mg_name+"("+bean.gu_piao.get(0).mg_code+")");
                if(bean.gu_piao.get(0).mg_zfz.contains("+")){
                    range1.setTextColor(mContext.getResources().getColor(R.color.colorRise));
                }else {
                    range1.setTextColor(mContext.getResources().getColor(R.color.colorFall));
                }
                range1.setText(bean.gu_piao.get(0).mg_zfz);
                timeIn1.setText(bean.gu_piao.get(0).dj_buy_date);
                timeOut1.setText(bean.gu_piao.get(0).dj_sell_date);
            }else if(bean.gu_piao.size() == 2){
                item3.setVisibility(View.GONE);
                range3.setVisibility(View.GONE);
                timeIn3.setVisibility(View.GONE);
                timeOut3.setVisibility(View.GONE);
                item1.setText(bean.gu_piao.get(0).mg_name+"("+bean.gu_piao.get(0).mg_code+")");
                item2.setText(bean.gu_piao.get(1).mg_name+"("+bean.gu_piao.get(1).mg_code+")");
                if(bean.gu_piao.get(0).mg_zfz.contains("+")){
                    range1.setTextColor(mContext.getResources().getColor(R.color.colorRise));
                }else {
                    range1.setTextColor(mContext.getResources().getColor(R.color.colorFall));
                }
                range1.setText(bean.gu_piao.get(0).mg_zfz);
                timeIn1.setText(bean.gu_piao.get(0).dj_buy_date);
                timeOut1.setText(bean.gu_piao.get(0).dj_sell_date);
                if(bean.gu_piao.get(1).mg_zfz.contains("+")){
                    range2.setTextColor(mContext.getResources().getColor(R.color.colorRise));
                }else {
                    range2.setTextColor(mContext.getResources().getColor(R.color.colorFall));
                }
                range2.setText(bean.gu_piao.get(1).mg_zfz);
                timeIn2.setText(bean.gu_piao.get(1).dj_buy_date);
                timeOut2.setText(bean.gu_piao.get(1).dj_sell_date);
            }else {
                item1.setText(bean.gu_piao.get(0).mg_name+"("+bean.gu_piao.get(0).mg_code+")");
                item2.setText(bean.gu_piao.get(1).mg_name+"("+bean.gu_piao.get(1).mg_code+")");
                item3.setText(bean.gu_piao.get(2).mg_name+"("+bean.gu_piao.get(2).mg_code+")");
                if(bean.gu_piao.get(0).mg_zfz.contains("+")){
                    range1.setTextColor(mContext.getResources().getColor(R.color.colorRise));
                }else {
                    range1.setTextColor(mContext.getResources().getColor(R.color.colorFall));
                }
                range1.setText(bean.gu_piao.get(0).mg_zfz);
                timeIn1.setText(bean.gu_piao.get(0).dj_buy_date);
                timeOut1.setText(bean.gu_piao.get(0).dj_sell_date);
                if(bean.gu_piao.get(1).mg_zfz.contains("+")){
                    range2.setTextColor(mContext.getResources().getColor(R.color.colorRise));
                }else {
                    range2.setTextColor(mContext.getResources().getColor(R.color.colorFall));
                }
                range2.setText(bean.gu_piao.get(1).mg_zfz);
                timeIn2.setText(bean.gu_piao.get(1).dj_buy_date);
                timeOut2.setText(bean.gu_piao.get(1).dj_sell_date);
                if(bean.gu_piao.get(2).mg_zfz.contains("+")){
                    range3.setTextColor(mContext.getResources().getColor(R.color.colorRise));
                }else {
                    range3.setTextColor(mContext.getResources().getColor(R.color.colorFall));
                }
                range3.setText(bean.gu_piao.get(2).mg_zfz);
                timeIn3.setText(bean.gu_piao.get(2).dj_buy_date);
                timeOut3.setText(bean.gu_piao.get(2).dj_sell_date);
            }
            if(bean.djf_type.equals("0")){
                type.setText("短");
            }else {
                type.setText("中");
            }
            count.setText(bean.dy_count);
            quantity.setText("股池数量："+bean.jgc_count);
        }
    }
}
