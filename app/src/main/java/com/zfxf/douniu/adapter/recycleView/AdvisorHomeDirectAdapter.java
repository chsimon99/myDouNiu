package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.TestBean;
import com.zfxf.douniu.utils.CommonUtils;

import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class AdvisorHomeDirectAdapter extends RecyclerView.Adapter<AdvisorHomeDirectAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<TestBean> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int positon);
    }

    public AdvisorHomeDirectAdapter(Context context, List<TestBean> datas) {
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
    public void addDatas(TestBean data) {
        mDatas.add(data);
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
                mListener.onItemClick(v, getPosition());
            }
        }

        public void setRefreshData(TestBean bean, int position) {

            if(bean.isFlag()){
                time.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.colorTitle));
               count.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.colorShort));
                type.setImageResource(R.drawable.icon_eyes);
                content.setText("语言文字直播中");
            }else {
                content.setText(bean.getStr());
                time.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.titleTextGray));
                count.setTextColor(CommonUtils.getContext().getResources().getColor(R.color.titleTextGray));
                type.setImageResource(R.drawable.icon_eye);
            }
        }
    }
}
