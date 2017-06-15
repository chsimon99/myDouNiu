package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.utils.CommonUtils;

import java.util.List;
import java.util.Map;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class AdvisorHomeDirectAdapter extends RecyclerView.Adapter<AdvisorHomeDirectAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<Map<String, String>> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int id,int status);
    }

    public AdvisorHomeDirectAdapter(Context context, List<Map<String, String>> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_advisor_home_direct, null);
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

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        ImageView img;
        ImageView type;
        TextView time;
        TextView count;
        TextView content;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            img = (ImageView) itemView.findViewById(R.id.iv_advisor_home_item_img);
            type = (ImageView) itemView.findViewById(R.id.iv_advisor_home_item_type);
            time = (TextView) itemView.findViewById(R.id.tv_advisor_home_item_time);
            count = (TextView) itemView.findViewById(R.id.tv_advisor_home_item_count);
            content = (TextView) itemView.findViewById(R.id.tv_advisor_home_item_content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, Integer.parseInt(mDatas.get(getPosition()).get("zt_id")),
                        Integer.parseInt(mDatas.get(getPosition()).get("status")));
            }
        }

        public void setRefreshData(Map<String, String> bean, int position) {
            if(bean.get("status").equals("1")){//正在直播
                time.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.colorTitle));
                count.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.colorShort));
                type.setImageResource(R.drawable.icon_eyes);
                img.setImageResource(R.drawable.icon_play);
                time.setText("语言文字直播中");
            }else {
                time.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.titleTextGray));
                count.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.titleTextGray));
                type.setImageResource(R.drawable.icon_eye);
                time.setText(bean.get("zt_start"));
                img.setImageResource(R.drawable.bar_time);
            }
            count.setText(bean.get("zt_clicks"));
            content.setText(bean.get("zt_name"));
        }
    }
}
