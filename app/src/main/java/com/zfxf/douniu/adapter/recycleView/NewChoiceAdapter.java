package com.zfxf.douniu.adapter.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class NewChoiceAdapter extends RecyclerView.Adapter<NewChoiceAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<Map<String, String>> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int id);
    }

    public NewChoiceAdapter(Context context, List<Map<String, String>> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_new_choice, null);
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
        TextView content;
        TextView name;
        TextView time;
        ImageView mImageView;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            content = (TextView) itemView.findViewById(R.id.tv_new_choice_content);
            name = (TextView) itemView.findViewById(R.id.tv_new_choice_name);
            time = (TextView) itemView.findViewById(R.id.tv_new_choice_time);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_new_choice_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, Integer.parseInt(mDatas.get(getPosition()).get("cc_id")));
            }
        }

        public void setRefreshData(Map<String, String> bean, int position) {
            name.setText(bean.get("cc_from"));
            time.setText(bean.get("cc_datetime"));
            content.setText(bean.get("cc_title"));

            String str = "http://app.douniu8.com/index.php/cms/zixunlist/checkfileexists/fileid/"+bean.get("cc_fielid");

            OkHttpUtils.get().url(str).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                }
                @Override
                public void onResponse(String response, int id) {
                    Glide.with(mContext).load(response)
                            .placeholder(R.drawable.public_img)
                            .into(mImageView);
                }
            });
        }
    }

//    public static String inputStream2String(InputStream is) throws IOException {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        int i = -1;
//        while ((i = is.read()) != -1) {
//            baos.write(i);
//        }
//        return baos.toString();
//    }
}
