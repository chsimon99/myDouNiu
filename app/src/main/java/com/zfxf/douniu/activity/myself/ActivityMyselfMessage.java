package com.zfxf.douniu.activity.myself;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityGoldPond;
import com.zfxf.douniu.activity.ActivityLiving;
import com.zfxf.douniu.adapter.recycleView.MessageAdapter;
import com.zfxf.douniu.bean.CourseResult;
import com.zfxf.douniu.bean.MessageInfo;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:15
 * @des    消息中心
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityMyselfMessage extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

//    @BindView(R.id.ll_myself_message_system)//系统
//    LinearLayout ll_system;
//    @BindView(R.id.ll_myself_message_advisor)
//    LinearLayout ll_advisor;
//    @BindView(R.id.ll_myself_message_alarm)//预警
//    LinearLayout ll_alarm;
//    @BindView(R.id.ll_myself_message_service)
//    LinearLayout ll_service;
//    @BindView(R.id.ll_myself_message_moni)
//    LinearLayout ll_moni;
//    @BindView(R.id.ll_myself_message_quan)
//    LinearLayout ll_quan;
//
//    @BindView(R.id.tv_myself_message_system)
//    TextView tv_system;
//    @BindView(R.id.tv_myself_message_system_time)
//    TextView tv_system_time;
//    @BindView(R.id.tv_myself_message_advisor)
//    TextView tv_advisor;
//    @BindView(R.id.tv_myself_message_advisor_time)
//    TextView tv_advisor_time;
//    @BindView(R.id.tv_myself_message_alarm)
//    TextView tv_alarm;
//    @BindView(R.id.tv_myself_message_alarm_time)
//    TextView tv_alarm_time;
//    @BindView(R.id.tv_myself_message_service)
//    TextView tv_service;
//    @BindView(R.id.tv_myself_message_service_time)
//    TextView tv_service_time;
//    @BindView(R.id.tv_myself_message_moni)
//    TextView tv_moni;
//    @BindView(R.id.tv_myself_message_moni_time)
//    TextView tv_moni_time;
//    @BindView(R.id.tv_myself_message_quan)
//    TextView tv_quan;
//    @BindView(R.id.tv_myself_message_quan_time)
//    TextView tv_quan_time;
//
//    @BindView(R.id.v_myself_message_system)
//    View v_system;
//    @BindView(R.id.v_myself_message_advisor)
//    View v_advisor;
//    @BindView(R.id.v_myself_message_alarm)
//    View v_alarm;
//    @BindView(R.id.v_myself_message_service)
//    View v_service;
//    @BindView(R.id.v_myself_message_moni)
//    View v_moni;
//    @BindView(R.id.v_myself_message_quan)
//    View v_quan;

    private int totlePage = 0;
    private int currentPage = 1;
    @BindView(R.id.rv_myself_message)
    PullLoadMoreRecyclerView mRecyclerView;
    private MessageAdapter mAdapter;
    private RecycleViewDivider mDivider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_message);
        ButterKnife.bind(this);
        title.setText("消息中心");
        edit.setVisibility(View.INVISIBLE);
        initData();
        initListener();
    }

    private void initData() {
        visitInternet();
//        v_system.setVisibility(View.INVISIBLE);
//        v_alarm.setVisibility(View.INVISIBLE);
//        v_advisor.setVisibility(View.INVISIBLE);
//        v_service.setVisibility(View.INVISIBLE);
//        v_moni.setVisibility(View.INVISIBLE);
//        v_quan.setVisibility(View.INVISIBLE);
    }

    private void visitInternet() {
        NewsInternetRequest.getMessageIndexInformation(currentPage,new NewsInternetRequest.ForResultResearchInfoListener() {
            @Override
            public void onResponseMessage(CourseResult result) {
                totlePage = Integer.parseInt(result.total);
                if (totlePage > 0 && currentPage <= totlePage){
                    if(currentPage == 1){
                        if(mAdapter == null){
                            mAdapter = new MessageAdapter(ActivityMyselfMessage.this,result.sys_message);
                        }

                        mRecyclerView.setLinearLayout();
                        mRecyclerView.setAdapter(mAdapter);
                        if(mDivider == null){
                            mDivider = new RecycleViewDivider(ActivityMyselfMessage.this, LinearLayoutManager.HORIZONTAL);
                            mRecyclerView.addItemDecoration(mDivider);
                        }
                        mRecyclerView.setFooterViewText("加载更多……");

                        mAdapter.setOnItemClickListener(new MessageAdapter.MyItemClickListener() {
                            @Override
                            public void onItemClick(View v, int id, MessageInfo bean) {//不跳详情页，直接跳内容
//                                Intent intent = new Intent(ActivityMyselfMessage.this, ActivityMessageInformation.class);
//                                intent.putExtra("id",id+"");
//                                startActivity(intent);
//                                overridePendingTransition(0,0);
                                if (bean.sm_type.equals("1")) {//直播课
                                    Intent i = new Intent(ActivityMyselfMessage.this, ActivityLiving.class);
                                    i.putExtra("id",Integer.parseInt(bean.id));
                                    i.putExtra("sx_id",bean.sx_ub_id);
                                    startActivity(i);
                                    overridePendingTransition(0,0);
                                }else if(bean.sm_type.equals("2")){//金股池
                                    Intent i = new Intent(ActivityMyselfMessage.this, ActivityGoldPond.class);
                                    i.putExtra("jgcId",Integer.parseInt(bean.id));
                                    i.putExtra("id",Integer.parseInt(bean.sx_ub_id));
                                    startActivity(i);
                                    overridePendingTransition(0,0);
                                }else if(bean.sm_type.equals("0")){//普通推送

                                }
                            }
                        });
                    }else {
                        mAdapter.addDatas(result.sys_message);
                        mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                        mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
                            @Override
                            public void run() {
                                mRecyclerView.setPullLoadMoreCompleted();
                            }
                        },1000);
                    }
                    currentPage++;
                }
            }
        });
    }

    private void initListener() {
        back.setOnClickListener(this);
//        ll_system.setOnClickListener(this);
//        ll_alarm.setOnClickListener(this);
//        ll_advisor.setOnClickListener(this);
//        ll_service.setOnClickListener(this);
//        ll_moni.setOnClickListener(this);
//        ll_quan.setOnClickListener(this);
        mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                mAdapter = null;
                visitInternet();
                mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
                    @Override
                    public void run() {
                        mRecyclerView.setPullLoadMoreCompleted();
                    }
                },1000);
            }

            @Override
            public void onLoadMore() {
                if(currentPage > totlePage){
                    Toast.makeText(CommonUtils.getContext(),"没有数据了",Toast.LENGTH_SHORT).show();
                    mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
                        @Override
                        public void run() {
                            mRecyclerView.setPullLoadMoreCompleted();
                        }
                    }, 200);
                    return;
                }
                visitInternet();
            }
        });
    }
    Intent mIntent;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                break;
//            case R.id.ll_myself_message_system:
//                mIntent = new Intent(this,ActivityMyselfMessageCategorie.class);
//                mIntent.putExtra("type","系统消息");
//                startActivity(mIntent);
//                overridePendingTransition(0,0);
//                break;
//            case R.id.ll_myself_message_alarm:
//                mIntent = new Intent(this,ActivityMyselfMessageCategorie.class);
//                mIntent.putExtra("type","预警提醒");
//                startActivity(mIntent);
//                overridePendingTransition(0,0);
//                break;
//            case R.id.ll_myself_message_advisor:
//                mIntent = new Intent(this,ActivityMyselfMessageCategorie.class);
//                mIntent.putExtra("type","首席动态");
//                startActivity(mIntent);
//                overridePendingTransition(0,0);
//                break;
//            case R.id.ll_myself_message_service:
//                mIntent = new Intent(this,ActivityMyselfMessageCategorie.class);
//                mIntent.putExtra("type","服务动态");
//                startActivity(mIntent);
//                overridePendingTransition(0,0);
//                break;
//            case R.id.ll_myself_message_moni:
//                mIntent = new Intent(this,ActivityMyselfMessageCategorie.class);
//                mIntent.putExtra("type","模拟炒股");
//                startActivity(mIntent);
//                overridePendingTransition(0,0);
//                break;
//            case R.id.ll_myself_message_quan:
//                mIntent = new Intent(this,ActivityMyselfMessageCategorie.class);
//                mIntent.putExtra("type","圈子信息");
//                startActivity(mIntent);
//                overridePendingTransition(0,0);
//                break;
        }
        finishAll();
        finish();
    }

    private void finishAll() {
        mIntent = null;
    }

}
