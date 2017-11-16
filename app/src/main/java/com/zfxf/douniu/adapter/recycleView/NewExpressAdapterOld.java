package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zfxf.douniu.R;

import java.util.List;
import java.util.Map;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class NewExpressAdapterOld extends RecyclerView.Adapter<NewExpressAdapterOld.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<Map<String, String>> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public NewExpressAdapterOld(Context context, List<Map<String, String>> datas) {
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
        mDatas.get(0).put("this_date","");
        mDatas.add(0,maps);
    }
    public void deleteAll() {
        mDatas.clear();
    }
    public String getLastId(){
        return mDatas.get(mDatas.size()-1).get("cc_id");
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

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            content = (TextView) itemView.findViewById(R.id.tv_new_express_content);
            data = (TextView) itemView.findViewById(R.id.tv_new_express_data);
            time = (TextView) itemView.findViewById(R.id.tv_new_express_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        public void setRefreshData(Map<String, String> bean, int position) {
            String thisDate = bean.get("this_date");
            if(TextUtils.isEmpty(thisDate)){
                data.setVisibility(View.GONE);
            }else {
                data.setVisibility(View.VISIBLE);
                data.setText(thisDate);
            }
            time.setText(bean.get("cc_datetime"));
            content.setText(Html.fromHtml(bean.get("cc_context")));
        }
    }
}
