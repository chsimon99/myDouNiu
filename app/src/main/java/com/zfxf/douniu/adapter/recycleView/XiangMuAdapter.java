package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;

import java.util.List;
import java.util.Map;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class XiangMuAdapter extends RecyclerView.Adapter<XiangMuAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<Map<String, String>> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int id);
    }

    public XiangMuAdapter(Context context, List<Map<String, String>> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_xiangmu, null);
        MyHolder myHolder = new MyHolder(view, mItemClickListener);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.setRefreshData(mDatas.get(position));
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
        ImageView type;
        ImageView bgImg;
        Button look;
        TextView name;
        TextView time;
        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            type = (ImageView) itemView.findViewById(R.id.iv_xiangmu_item_type);
            bgImg = (ImageView) itemView.findViewById(R.id.iv_xiangmu_item_img);
            look = (Button) itemView.findViewById(R.id.bt_xiangmu_item_look);
            name = (TextView) itemView.findViewById(R.id.tv_xiangmu_item_name);
            time = (TextView) itemView.findViewById(R.id.tv_xiangmu_item_time);

            look.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, Integer.parseInt(mDatas.get(getPosition()).get("cc_id")));
            }
        }

        public void setRefreshData(Map<String, String> bean) {
            if(Integer.parseInt(bean.get("biaoshi")) == 0){
                type.setImageResource(R.drawable.xiangmu_yure);
            }else{
                type.setImageResource(R.drawable.xiangmu_ing);
            }
            name.setText(bean.get("cc_title"));
            time.setText(bean.get("cc_datetime"));
            String picUrl = mContext.getResources().getString(R.string.file_host_address)
                    +mContext.getResources().getString(R.string.showpic)
                    +bean.get("cc_fielid");
            Glide.with(mContext).load(picUrl).placeholder(R.drawable.xiangmu_img)
                    .into(bgImg);
        }
    }
}
