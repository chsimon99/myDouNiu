package com.zfxf.douniu.activity.simulation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.simulation.ActivitySimulation;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.MyViewPagerSimulation;
import com.zfxf.douniu.view.fragment.FragmentSimulationStockBuy;
import com.zfxf.douniu.view.fragment.FragmentSimulationStockEntrust;
import com.zfxf.douniu.view.fragment.FragmentSimulationStockQuery;
import com.zfxf.douniu.view.fragment.FragmentSimulationStockSold;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:30
 * @des    模拟炒股页
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivitySimulationStock extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.iv_base_refresh)
    ImageView refresh;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tv_simulation_stock_buy)
    TextView tv_buy;
    @BindView(R.id.tv_simulation_stock_sold)
    TextView tv_sold;
    @BindView(R.id.tv_simulation_stock_entrust)
    TextView tv_entrust;
    @BindView(R.id.tv_simulation_stock_query)
    TextView tv_query;

    @BindView(R.id.v_simulation_stock_view)
    View v_v;
    @BindView(R.id.rl_simulation_stock_view)
    RelativeLayout rl_view;

    @BindView(R.id.vp_simulation_stock)
    MyViewPagerSimulation mViewPager;

    private List<Fragment> list_fragment = new ArrayList<>();
    private myPageAdapter mAdapter;
    private List<String> list_title = new ArrayList<>();
    private Fragment mFragmentSimulationStockBuy;
    private Fragment mFragmentSimulationStockSold;
    private Fragment mFragmentSimulationStockEntrust;
    private Fragment mFragmentSimulationStockQuery;

    private int screenWidth;
    private RelativeLayout.LayoutParams mLp;
    private RelativeLayout.LayoutParams mRlp;
    private int lastPositon = 0;
    private int newPositon = 9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation_stock);
        ButterKnife.bind(this);
        initTabLineWidth();
        title.setText("模拟炒股");
        int type = getIntent().getIntExtra("type", 0);
        edit.setVisibility(View.INVISIBLE);
        initData();
        initListener();
    }

    private void initData() {
        if(list_title.size() == 0){
            String[] titleStrings = CommonUtils.getResource().getStringArray(R.array.activity_simulation_titles);
            for(int i = 0; i<titleStrings.length;i++){
                list_title.add(titleStrings[i]);
            }
        }
        tv_buy.setText(list_title.get(0));
        tv_sold.setText(list_title.get(1));
        tv_entrust.setText(list_title.get(2));
        tv_query.setText(list_title.get(3));

        if(mFragmentSimulationStockBuy == null){
            mFragmentSimulationStockBuy = new FragmentSimulationStockBuy();
        }
        if(mFragmentSimulationStockSold == null){
            mFragmentSimulationStockSold = new FragmentSimulationStockSold();
        }
        if(mFragmentSimulationStockEntrust == null){
            mFragmentSimulationStockEntrust = new FragmentSimulationStockEntrust();
        }
        if(mFragmentSimulationStockQuery == null){
            mFragmentSimulationStockQuery = new FragmentSimulationStockQuery();
        }

        if(list_fragment.size() == 0){
            list_fragment.add(mFragmentSimulationStockBuy);
            list_fragment.add(mFragmentSimulationStockSold);
            list_fragment.add(mFragmentSimulationStockEntrust);
            list_fragment.add(mFragmentSimulationStockQuery);
        }

        if (mAdapter == null) {
            mAdapter = new myPageAdapter(getSupportFragmentManager(),list_fragment);
            mViewPager.setAdapter(mAdapter);
        }

        int index = ActivitySimulation.getIndex();
        if(index > 0){
            newPositon = index-2;
            select(newPositon);
            mRlp.width = (newPositon+1) * screenWidth / 4;
            mLp.leftMargin = newPositon * (screenWidth / 4);
            rl_view.setLayoutParams(mRlp);
            v_v.setLayoutParams(mLp);
            ActivitySimulation.setIndex(0);
            newPositon = 9;
            return;
        }
        select(lastPositon);
    }
    private void initListener() {
        back.setOnClickListener(this);
        refresh.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(new MyViewPagerSimulation.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (lastPositon == 0 && position == 0){// 0->1
                    getLeftMargin(positionOffset,0);
                }else if(lastPositon == 1 && position == 0){ // 1->0
                    getLeftMargin(positionOffset,1);
                }else if(lastPositon == 1 && position == 1){ // 1->2
                    getLeftMargin(positionOffset,0);
                }else if(lastPositon == 2 && position == 1){ // 2->1
                    getLeftMargin(positionOffset,1);
                }else if(lastPositon == 2 && position == 2){
                    getLeftMargin(positionOffset,0);
                }else if(lastPositon == 3 && position == 3){
                    getLeftMargin(positionOffset,0);
                }else if(lastPositon == 3 && position == 2){
                    getLeftMargin(positionOffset,1);
                }
                rl_view.setLayoutParams(mRlp);
                v_v.setLayoutParams(mLp);
            }

            private void getLeftMargin(float positionOffset,int offset) {
                int margin = (int) (-(offset-positionOffset) * (screenWidth * 1.0 / 4) + lastPositon * (screenWidth / 4));
                mLp.leftMargin = margin;
                mRlp.width = margin+screenWidth / 4;
            }

            @Override
            public void onPageSelected(int position) {
                lastPositon = position;
                select(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tv_buy.setOnClickListener(this);
        tv_sold.setOnClickListener(this);
        tv_entrust.setOnClickListener(this);
        tv_query.setOnClickListener(this);

    }
    public interface OnRefreshListener {
        void refreshData();
    }
    public static void setOnRefreshListener(OnRefreshListener listener){
       mListener = listener;
    }
    private static OnRefreshListener mListener;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.iv_base_refresh:
                if(mListener != null){
                    mListener.refreshData();
                }
                break;
            case R.id.tv_simulation_stock_buy:
                select(0);

                break;
            case R.id.tv_simulation_stock_sold:
                select(1);
                break;
            case R.id.tv_simulation_stock_entrust:
                select(2);
                break;
            case R.id.tv_simulation_stock_query:
                select(3);
                break;
        }
    }

    private void finishAll() {
        mListener = null;
    }
    private void select(int i) {
        reset();
        switch (i){
            case 0:
                tv_buy.setTextColor(getResources().getColor(R.color.colorTitle));
                refresh.setVisibility(View.VISIBLE);
                break;
            case 1:
                tv_sold.setTextColor(getResources().getColor(R.color.colorTitle));
                refresh.setVisibility(View.VISIBLE);
                break;
            case 2:
                tv_entrust.setTextColor(getResources().getColor(R.color.colorTitle));
                refresh.setVisibility(View.VISIBLE);
                break;
            case 3:
                tv_query.setTextColor(getResources().getColor(R.color.colorTitle));
                refresh.setVisibility(View.INVISIBLE);
                break;
        }
        mViewPager.setCurrentItem(i);
    }

    private void reset() {
        tv_buy.setTextColor(getResources().getColor(R.color.titleText));
        tv_sold.setTextColor(getResources().getColor(R.color.titleText));
        tv_entrust.setTextColor(getResources().getColor(R.color.titleText));
        tv_query.setTextColor(getResources().getColor(R.color.titleText));
    }

    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        mLp = (RelativeLayout.LayoutParams) v_v.getLayoutParams();
        mLp.width = screenWidth / 4;
        mRlp = (RelativeLayout.LayoutParams) rl_view.getLayoutParams();
        mRlp.width = screenWidth / 4;
        rl_view.setLayoutParams(mRlp);
        rl_view.setPadding(CommonUtils.px2dip(this,60),0,CommonUtils.px2dip(this,60),0);
        v_v.setLayoutParams(mLp);
    }

    class myPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> list_fragment = new ArrayList<Fragment>();

        public myPageAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            list_fragment = list;
        }

        @Override
        public Fragment getItem(int position) {
            return list_fragment.get(position);
        }

        @Override
        public int getCount() {
            return list_fragment.size();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUtils.dismissProgressDialog();
    }
}
