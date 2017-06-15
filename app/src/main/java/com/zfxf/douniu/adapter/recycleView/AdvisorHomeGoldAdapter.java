package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.StockChiInfo;
import com.zfxf.douniu.utils.CommonUtils;

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
        void onItemClick(View v, int id);
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
        private TextView count;//订阅人数
        private TextView item1;//展示股票
        private TextView item2;
        private TextView item3;
        private TextView range1;//涨跌幅
        private TextView range2;
        private TextView range3;
        private TextView type1;//趋势
        private TextView type2;//趋势幅度
        private TextView quantity;//股池数量
        private TextView time;//周期

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            title = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_title);
            count = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_count);
            item1 = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_item1);
            item2 = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_item2);
            item3 = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_item3);
            range1 = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_range1);
            range2 = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_range2);
            range3 = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_range3);
            type1 = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_type1);
            type2 = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_type2);
            quantity = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_quantity);
            time = (TextView) itemView.findViewById(R.id.tv_advisor_home_gold_time);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, Integer.parseInt(mDatas.get(getPosition()).ud_ub_id));
            }
        }

        public void setRefreshData(StockChiInfo bean, int position) {
            title.setText(bean.ud_nickname+"的金股池");
            count.setText(bean.dy_count);
            item1.setText(bean.gu_piao.get(0).mg_name+"("+bean.gu_piao.get(0).mg_code+")");
            item2.setText(bean.gu_piao.get(1).mg_name+"("+bean.gu_piao.get(1).mg_code+")");
            item3.setText(bean.gu_piao.get(2).mg_name+"("+bean.gu_piao.get(2).mg_code+")");
            if(bean.gu_piao.get(0).mg_zfz.contains("+")){
                range1.setTextColor(mContext.getResources().getColor(R.color.colorRise));
            }else {
                range1.setTextColor(mContext.getResources().getColor(R.color.colorFall));
            }
            range1.setText(bean.gu_piao.get(0).mg_zfz);
            if(bean.gu_piao.get(1).mg_zfz.contains("+")){
                range1.setTextColor(mContext.getResources().getColor(R.color.colorRise));
            }else {
                range1.setTextColor(mContext.getResources().getColor(R.color.colorFall));
            }
            range1.setText(bean.gu_piao.get(1).mg_zfz);
            if(bean.gu_piao.get(2).mg_zfz.contains("+")){
                range1.setTextColor(mContext.getResources().getColor(R.color.colorRise));
            }else {
                range1.setTextColor(mContext.getResources().getColor(R.color.colorFall));
            }
            range1.setText(bean.gu_piao.get(2).mg_zfz);
            if(bean.is_up.equals("1")){
                type1.setText("涨");
                type1.setBackgroundResource(R.drawable.backgroud_circle_rise);
                type2.setTextColor(CommonUtils.getResource().getColor(R.color.colorRise));
            }else {
                type1.setText("跌");
                type1.setBackgroundResource(R.drawable.backgroud_circle_fall);
                type2.setTextColor(CommonUtils.getResource().getColor(R.color.colorFall));
            }
            type2.setText(bean.status);
            quantity.setText("股池数量："+bean.jgc_count);
            time.setText("发布周期："+bean.jgcfa_date);
        }
    }
}
