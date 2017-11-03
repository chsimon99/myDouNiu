package com.zfxf.douniu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.login.ActivityLogin;
import com.zfxf.douniu.adapter.recycleView.XuanGuAdapter;
import com.zfxf.douniu.bean.XuanguResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.ExpandableTextView;
import com.zfxf.douniu.view.FullyLinearLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time 2017/5/3 13:14
 * @des 智能选股王详情页
 * 邮箱：butterfly_xu@sina.com
 */
public class ActivityIntelligenceChoose extends FragmentActivity implements View.OnClickListener {

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
//    @BindView(R.id.tv_intelligence_choose_rise)
//    TextView tv_rise;
    @BindView(R.id.tv_intelligence_choose_sucess_ratio)
    TextView tv_ratio;
    @BindView(R.id.tv_intelligence_choose_data)
    TextView tv_data;
    @BindView(R.id.tv_intelligence_choose_subscribe_count)
    TextView tv_subscribe_count;
    @BindView(R.id.tv_intelligence_choose_subscribe_time)
    TextView tv_subscribe_time;
//    @BindView(R.id.tv_intelligence_choose_subscribe_content)
//    TextView tv_subscribe_content;
    @BindView(R.id.expandable_text)
    ExpandableTextView tv_subscribe_content;
    @BindView(R.id.tv_intelligence_choose_stock_count)
    TextView tv_stock_count;
    @BindView(R.id.tv_intelligence_choose_stock_name)
    TextView tv_name;//说明，未购买前为 历史股票，购买后为推荐股票

    @BindView(R.id.ll_intelligence_choose_history)
    LinearLayout history;
    @BindView(R.id.rl_intelligence_choose_tuijian)
    RelativeLayout rl_tuijian;

//    @BindView(R.id.lv_intelligence_choose)
//    ListView mListView;
//    private MyListViewAdapter mAdapter;
//    private int mCurrentPositon;

    @BindView(R.id.rv_intelligence_choose)
    RecyclerView mRecyclerView;
    private XuanGuAdapter mXuanGuAdapter;
    private LinearLayoutManager mManager;
    private int mId;
    private int historyId;
    private String mDys;
    private String mZfUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intelligence_choose);
        ButterKnife.bind(this);
        String type_title = getIntent().getStringExtra("type");//传过来的标题
        title.setText(type_title);
        edit.setVisibility(View.INVISIBLE);
        share.setVisibility(View.INVISIBLE);
        mId = getIntent().getIntExtra("id", 0);
        average_price.getPaint().setFakeBoldText(true);//加粗
        tv_data.getPaint().setFakeBoldText(true);//加粗
        tv_fall.getPaint().setFakeBoldText(true);//加粗
        tv_ratio.getPaint().setFakeBoldText(true);//加粗
        tv_subscribe.getPaint().setFakeBoldText(true);//加粗
        initData();
        initListener();
    }

    private void initData() {
        CommonUtils.showProgressDialog(this,"加载中……");
        visitInternet();
    }

    private void visitInternet() {
        NewsInternetRequest.getXuanGuDetailInformation(mId, new NewsInternetRequest.ForResultXuanGuListener() {
            @Override
            public void onResponseMessage(XuanguResult result) {
                title.setText(result.news_info.zf_title);
                average_price.setText(result.news_info.zf_pjsy+"%");
                tv_ratio.setText(result.news_info.zf_xgcgl+"%");
                tv_fall.setText(result.news_info.zf_zsx+"%");
                tv_data.setText(result.news_info.zf_cgzq+"天");
                mDys = result.news_info.zf_dys;
                tv_subscribe_count.setText("订阅："+ mDys);
                tv_subscribe_time.setText(result.news_info.zf_date+"更新");
//                tv_subscribe_content.setText(result.news_info.zf_info,true);
                tv_stock_count.setText(result.item_info.zi_tjgps+"股");
                historyId = Integer.parseInt(result.item_info.zi_id);
                mZfUrl = result.news_info.zf_url;
                if(result.news_info.has_buy.equals("0")){
                    tv_subscribe.setText("立即购买");
                    tv_name.setText("历史股票：");
                }else{
                    tv_subscribe.setText("到期时间："+result.news_info.end_time);
                    tv_name.setText("推荐股票：");
                }
//                listviewDatas = result.news_info.item_list;
//                if (mAdapter == null) {
//                    mAdapter = new MyListViewAdapter(ActivityIntelligenceChoose.this);
//                }
//                mListView.setAdapter(mAdapter);

                if (mXuanGuAdapter == null) {
                    mXuanGuAdapter = new XuanGuAdapter(ActivityIntelligenceChoose.this, result.item_info.gupiao_list);
                }
                if (mManager == null) {
                    mManager = new FullyLinearLayoutManager(ActivityIntelligenceChoose.this);
                }
                mRecyclerView.setLayoutManager(mManager);
                mRecyclerView.setAdapter(mXuanGuAdapter);
                CommonUtils.dismissProgressDialog();
            }
        });
    }

    private void initListener() {
        back.setOnClickListener(this);
        share.setOnClickListener(this);
        history.setOnClickListener(this);
        tv_subscribe.setOnClickListener(this);
        rl_tuijian.setOnClickListener(this);
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                CommonUtils.showProgressDialog(ActivityIntelligenceChoose.this,"加载中……");
//                mCurrentPositon = position;
//                NewsInternetRequest.getXuanGuDetailList(listviewDatas.get(position).zi_id, new NewsInternetRequest.ForResultXuanGuDetailListener() {
//                    @Override
//                    public void onResponseMessage(XuanguDetail result) {
//                        tv_stock_count.setText(result.zi_tjgps+"股");
//                        historyId = Integer.parseInt(result.zi_id);
//                        mXuanGuAdapter.changeDatas(result.gupiao_list);
//
//                        mXuanGuAdapter.notifyDataSetChanged();
//                        mAdapter.notifyDataSetChanged();
//                        CommonUtils.dismissProgressDialog();
//                    }
//                });
//
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.iv_base_share:

                break;
            case R.id.rl_intelligence_choose_tuijian:
                Intent tuijian = new Intent(this, ActivityGoldHistory.class);
                tuijian.putExtra("url",mZfUrl);
                tuijian.putExtra("name","推荐理由");
                startActivity(tuijian);
                overridePendingTransition(0, 0);
                break;
            case R.id.tv_intelligence_choose_subscribe:
                if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
                    Intent intent = new Intent(this, ActivityLogin.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return;
                }
                if(tv_subscribe.getText().toString().equals("立即购买")){
                    goToBuy();
                }
                break;
            case R.id.ll_intelligence_choose_history:
                Intent intent = new Intent(this, Activityhistory.class);
                intent.putExtra("id",historyId);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
        }
    }

    private void goToBuy() {
        Intent intent = new Intent(CommonUtils.getContext(), ActivityToPay.class);
        intent.putExtra("info","选股王,"+mId+","+mId);
        intent.putExtra("from","选股王");
        intent.putExtra("count", "100");//临时传的没有意思，为保证不崩
        intent.putExtra("planId",mId);
        intent.putExtra("dycount",mDys);
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    //    private void subscribeInternet(final int type) {
//        NewsInternetRequest.XuanGusubscribeAndCannel(mId+"", 0, type, new NewsInternetRequest.ForResultListener() {
//                    @Override
//                    public void onResponseMessage(String count) {
//                        tv_subscribe_count.setText("订阅："+count);
//                        if(type == 0){
//                            tv_subscribe.setText("已订阅");
//                            CommonUtils.toastMessage("订阅成功");
//                        }else {
//                            tv_subscribe.setText("立即订阅");
//                            CommonUtils.toastMessage("取消订阅成功");
//                        }
//                        SpTools.setBoolean(ActivityIntelligenceChoose.this, Constants.subscribe,true);
//                    }
//                },getResources().getString(R.string.xuangudy));
//    }
    private void finishAll() {

    }

//    private List<XuanguItemList> listviewDatas = new ArrayList<>();
//
//    class MyListViewAdapter extends BaseAdapter {
//        private Context mContext;
//
//        public MyListViewAdapter(Context context) {
//            mContext = context;
//        }
//
//        @Override
//        public int getCount() {
//            return listviewDatas.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return listviewDatas.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder;
//            if (convertView == null) {
//                holder = new ViewHolder();
//                convertView = View.inflate(mContext, R.layout.item_list_intelligence_choose, null);
//                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_list_intelligence_choose_name);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            if (mCurrentPositon == position) {
//                holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.colorTitle));
//            } else {
//                holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.titleText));
//            }
//            holder.tv_name.setText(listviewDatas.get(position).zi_title);
//            return convertView;
//        }
//    }
//
//    class ViewHolder {
//        public TextView tv_name;
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUtils.dismissProgressDialog();
    }

    @Override
    protected void onResume() {
        if(SpTools.getBoolean(this, Constants.buy,false)){
            mXuanGuAdapter = null;
            visitInternet();
            SpTools.setBoolean(this, Constants.buy, false);
        }
        super.onResume();
    }
}
