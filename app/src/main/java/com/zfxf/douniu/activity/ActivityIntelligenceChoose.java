package com.zfxf.douniu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.MarketMarketAdapter;
import com.zfxf.douniu.view.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:14
 * @des    智能选股王详情页
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityIntelligenceChoose extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.iv_base_share)
    ImageView share;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tv_intelligence_choose_average_price)
    TextView average_price;
    @BindView(R.id.tv_intelligence_choose_subscribe)
    TextView tv_subscribe;
    @BindView(R.id.tv_intelligence_choose_fall)
    TextView tv_fall;
    @BindView(R.id.tv_intelligence_choose_rise)
    TextView tv_rise;
    @BindView(R.id.tv_intelligence_choose_sucess_ratio)
    TextView tv_ratio;
    @BindView(R.id.tv_intelligence_choose_data)
    TextView tv_data;
    @BindView(R.id.tv_intelligence_choose_subscribe_count)
    TextView tv_subscribe_count;
    @BindView(R.id.tv_intelligence_choose_subscribe_time)
    TextView tv_subscribe_time;
    @BindView(R.id.tv_intelligence_choose_subscribe_content)
    TextView tv_subscribe_content;
    @BindView(R.id.tv_intelligence_choose_stock_count)
    TextView tv_stock_count;

    @BindView(R.id.ll_intelligence_choose_history)
    LinearLayout history;

    @BindView(R.id.lv_intelligence_choose)
    ListView mListView;
    private MyListViewAdapter mAdapter;
    private int mCurrentPositon;

    @BindView(R.id.rv_intelligence_choose)
    RecyclerView mRecyclerView;
    private MarketMarketAdapter mMarketAdapter;
    private LinearLayoutManager mManager;
    private List<String> datas = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intelligence_choose);
        ButterKnife.bind(this);
        String type_title = getIntent().getStringExtra("type");//传过来的标题
        title.setText(type_title);
        edit.setVisibility(View.INVISIBLE);
        share.setVisibility(View.VISIBLE);
        average_price.getPaint().setFakeBoldText(true);//加粗
        tv_data.getPaint().setFakeBoldText(true);//加粗
        tv_fall.getPaint().setFakeBoldText(true);//加粗
        tv_ratio.getPaint().setFakeBoldText(true);//加粗
        tv_rise.getPaint().setFakeBoldText(true);//加粗
        tv_subscribe.getPaint().setFakeBoldText(true);//加粗
        initData();
        initListener();
    }

    private void initData() {
        if (listviewDatas.size() == 0) {
            listviewDatas.add("");
            listviewDatas.add("");
            listviewDatas.add("");
            listviewDatas.add("");
            listviewDatas.add("");
            listviewDatas.add("");
            listviewDatas.add("");
        }
        if(mAdapter == null){
            mAdapter = new MyListViewAdapter(this);
        }
        mListView.setAdapter(mAdapter);

        if(datas.size() == 0){
            datas.add("1");
            datas.add("2");
        }
        if(mMarketAdapter == null){
            mMarketAdapter = new MarketMarketAdapter(this,datas);
        }
        if(mManager == null){
            mManager = new FullyLinearLayoutManager(this);
        }
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mMarketAdapter);

    }
    private void initListener() {
        back.setOnClickListener(this);
        share.setOnClickListener(this);
        history.setOnClickListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPositon = position;
                if(datas.size()>0){
                    datas.clear();
                    for (int i = 0;i < position + 2;i++){
                        datas.add("1");
                    }
                }
                mMarketAdapter.notifyDataSetChanged();
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.iv_base_share:

                break;
            case R.id.ll_intelligence_choose_history:
                Intent intent = new Intent(this,Activityhistory.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
        }
    }

    private void finishAll() {

    }
    private List<String> listviewDatas = new ArrayList<>();

     class MyListViewAdapter extends BaseAdapter{
         private Context mContext;
         public MyListViewAdapter(Context context) {
             mContext = context;
         }

         @Override
         public int getCount() {
             return listviewDatas.size();
         }

         @Override
         public Object getItem(int position) {
             return listviewDatas.get(position);
         }

         @Override
         public long getItemId(int position) {
             return position;
         }

         @Override
         public View getView(int position, View convertView, ViewGroup parent) {
             ViewHolder holder;
             if(convertView == null){
                 holder = new ViewHolder();
                 convertView = View.inflate(mContext,R.layout.item_list_intelligence_choose,null);
                 holder.tv_name = (TextView) convertView.findViewById(R.id.tv_list_intelligence_choose_name);
                 convertView.setTag(holder);
             }else{
                 holder = (ViewHolder) convertView.getTag();
             }
             if(mCurrentPositon == position){
                 holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.colorTitle));
             }else{
                 holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.titleText));
             }
             holder.tv_name.setText("强势异动"+position);
             return convertView;
         }
         class ViewHolder{
             public TextView tv_name;
         }
     }
}
