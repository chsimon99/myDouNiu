package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;

import java.util.List;
import java.util.Map;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class NewExpressAdapter extends RecyclerView.Adapter<NewExpressAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<Map<String, String>> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public NewExpressAdapter(Context context, List<Map<String, String>> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_new_express, null);
        MyHolder myHolder = new MyHolder(view, mItemClickListener);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.setRefreshData(mDatas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
    public void addDatas(List<Map<String, String>> data) {
        mDatas.addAll(data);
    }
    public void addTopDatas(Map<String, String> maps) {
        mDatas.add(0,maps);
    }
    public void deleteAll() {
        mDatas.clear();
    }
    /**
     * 复用这个属性来进行多布局展示
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        TextView content;
        TextView data;
        TextView time;
        LinearLayout ll_red;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            content = (TextView) itemView.findViewById(R.id.tv_new_express_content);
            data = (TextView) itemView.findViewById(R.id.tv_new_express_data);
            time = (TextView) itemView.findViewById(R.id.tv_new_express_time);
            ll_red = (LinearLayout) itemView.findViewById(R.id.ll_new_express_red);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        public void setRefreshData(Map<String, String> bean, int position) {
//            String thisDate = bean.get("this_date");
//            if(TextUtils.isEmpty(thisDate)){
//                data.setVisibility(View.GONE);
//            }else {
//                data.setVisibility(View.VISIBLE);
//                data.setText(thisDate);
//            }
            if(!TextUtils.isEmpty(bean.get("top"))){
                daytime = dealTime(mDatas.get(1).get("dateline"))[0];
                data.setVisibility(View.VISIBLE);
                data.setText(daytime);
                time.setVisibility(View.GONE);
                content.setVisibility(View.GONE);
                ll_red.setVisibility(View.GONE);
            }else {
                if(daytime.equals(dealTime(bean.get("dateline"))[0])){//是同一天
                    data.setVisibility(View.GONE);
                }else {//不是同一天的时候
                    data.setVisibility(View.VISIBLE);
                    data.setText(dealTime(bean.get("dateline"))[0]);
                }
                daytime = dealTime(bean.get("dateline"))[0];
                time.setText(dealTime(bean.get("dateline"))[1]);
                content.setText(bean.get("desc"));
            }
//            if(TextUtils.isEmpty(daytime)){
//                daytime = dealTime(bean.get("dateline"))[0];
//                data.setVisibility(View.VISIBLE);
//                data.setText(dealTime(bean.get("dateline"))[0]);
//            }else {
//                if(daytime.equals(dealTime(bean.get("dateline"))[0])){//是同一天
//                    data.setVisibility(View.GONE);
//                }else {//不是同一天的时候
//                    data.setVisibility(View.VISIBLE);
//                    data.setText(dealTime(bean.get("dateline"))[0]);
//                }
//                daytime = dealTime(bean.get("dateline"))[0];
//            }
        }
    }

    private String daytime;
    private String[] dealTime(String time) {
        int pos = time.indexOf("日");
        return new String[]{time.substring(0, pos + 2),time.substring(pos + 2, time.length())};
    }
}
