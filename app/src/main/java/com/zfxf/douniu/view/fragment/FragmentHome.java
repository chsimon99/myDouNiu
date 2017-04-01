package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityAdvisorHome;
import com.zfxf.douniu.activity.ActivityBar;
import com.zfxf.douniu.activity.ActivityXiangMu;
import com.zfxf.douniu.activity.ActivityZhengu;
import com.zfxf.douniu.adapter.recycleView.HomeAdvisorAdapter;
import com.zfxf.douniu.adapter.recycleView.HomeChooseAdapter;
import com.zfxf.douniu.adapter.recycleView.HomeZhiboAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.utils.MyLunBo;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.InnerView;
import com.zfxf.douniu.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentHome extends BaseFragment implements View.OnClickListener{
    private View view;
    @BindView(R.id.inwerview)
    InnerView mViewPage;
    @BindView(R.id.item_home_pic_ll)
    LinearLayout mContainer;
    @BindView(R.id.tv_home_gold)
    TextView gold;
    @BindView(R.id.tv_home_choose_stock)
    TextView choose;
    @BindView(R.id.tv_home_zhibo)
    TextView zhibo;

    @BindView(R.id.iv_home_jinpai_more)
    ImageView jinpai_more;
    @BindView(R.id.ll_home_zhibo)
    LinearLayout ll_zhibo;
    @BindView(R.id.ll_home_gkk)
    LinearLayout ll_gkk;
    @BindView(R.id.ll_home_smk)
    LinearLayout ll_smk;
    @BindView(R.id.ll_home_bar)
    LinearLayout ll_bar;
    @BindView(R.id.ll_home_toutiao)
    LinearLayout ll_toutiao;
    @BindView(R.id.ll_home_zhengu)
    LinearLayout ll_zhengu;
    @BindView(R.id.ll_home_moni)
    LinearLayout ll_moni;
    @BindView(R.id.ll_home_xiangmu)
    LinearLayout ll_xiangmu;

    @BindView(R.id.rv_home_advisor)
    RecyclerView mAdvisorRecyclerView;//金牌首席recycleview
    private LinearLayoutManager mAdvisorManager;
    private HomeAdvisorAdapter mAdvisorAdapter;

    @BindView(R.id.rv_home_choose)
    RecyclerView mChooseRecyclerView;//智能选股recycleview
    private LinearLayoutManager mChooseManager;
    private HomeChooseAdapter mChooseAdapter;

    @BindView(R.id.rv_home_zhibo)
    RecyclerView mZhiboRecyclerView;//热门直播recycleview
    private LinearLayoutManager mZhiboManager;
    private HomeZhiboAdapter mZhiboAdapter;

    private List<Integer> mDatas = new ArrayList<Integer>();
    private MyLunBo mMyLunBO;
    private List<String> advisorDatas = new ArrayList<String>();
    private List<String> chooseDatas = new ArrayList<String>();
    private List<String> zhiboDatas = new ArrayList<String>();

    @Override
    public View initView(LayoutInflater inflater) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home, null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if(parent !=null){
            parent.removeView(view);
        }
        ButterKnife.bind(this,view);
        gold.getPaint().setFakeBoldText(true);//加粗
        choose.getPaint().setFakeBoldText(true);//加粗
        zhibo.getPaint().setFakeBoldText(true);//加粗
        return view;
    }

    @Override
    public void init() {
        super.init();
    }
    @Override
    public void initdata() {
        if (mDatas.size() == 0) {
            mDatas.add(R.drawable.home_banner);
            mDatas.add(R.drawable.home_banner);
            mDatas.add(R.drawable.home_banner);
            mDatas.add(R.drawable.home_banner);
        }
        mViewPage.setAdapter(new myPicAdapter());

        if (advisorDatas.size() == 0) {
            advisorDatas.add("");
            advisorDatas.add("");
        }
        if(mAdvisorManager == null){
            mAdvisorManager = new FullyLinearLayoutManager(getActivity());
        }
        if(mAdvisorAdapter == null){
            mAdvisorAdapter = new HomeAdvisorAdapter(getActivity(), advisorDatas);
        }

        if(chooseDatas.size() == 0){
            chooseDatas.add("");
            chooseDatas.add("");
            chooseDatas.add("");
            chooseDatas.add("");
            chooseDatas.add("");
        }
        if(mChooseManager == null){
            mChooseManager = new FullyLinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        }
        if(mChooseAdapter == null){
            mChooseAdapter = new HomeChooseAdapter(getActivity(), chooseDatas);
        }

        if(zhiboDatas.size() == 0){
            zhiboDatas.add("");
            zhiboDatas.add("");
        }
        if(mZhiboManager == null){
            mZhiboManager = new FullyLinearLayoutManager(getActivity());
        }
        if(mZhiboAdapter == null){
            mZhiboAdapter = new HomeZhiboAdapter(getActivity(), zhiboDatas);
        }

        mAdvisorRecyclerView.setLayoutManager(mAdvisorManager);
        mAdvisorRecyclerView.setAdapter(mAdvisorAdapter);
        mAdvisorRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(),LinearLayoutManager.HORIZONTAL));

        mChooseRecyclerView.setLayoutManager(mChooseManager);
        mChooseRecyclerView.setAdapter(mChooseAdapter);

        mZhiboRecyclerView.setLayoutManager(mZhiboManager);
        mZhiboRecyclerView.setAdapter(mZhiboAdapter);
        super.initdata();
    }

    @Override
    public void initListener() {
        super.initListener();
        jinpai_more.setOnClickListener(this);
        ll_zhibo.setOnClickListener(this);
        ll_gkk.setOnClickListener(this);
        ll_smk.setOnClickListener(this);
        ll_bar.setOnClickListener(this);
        ll_toutiao.setOnClickListener(this);
        ll_zhengu.setOnClickListener(this);
        ll_moni.setOnClickListener(this);
        ll_xiangmu.setOnClickListener(this);
        mAdvisorAdapter.setOnItemClickListener(new HomeAdvisorAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View v, int positon) {
                Intent intent = new Intent(getActivity(), ActivityAdvisorHome.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(0,0);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.iv_home_jinpai_more:
                break;
            case R.id.ll_home_zhibo:
                break;
            case R.id.ll_home_gkk:
                break;
            case R.id.ll_home_smk:
                break;
            case R.id.ll_home_bar:
                intent = new Intent(getActivity(), ActivityBar.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(0,0);
                break;
            case R.id.ll_home_toutiao:
                break;
            case R.id.ll_home_zhengu:
                intent = new Intent(getActivity(), ActivityZhengu.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(0,0);
                break;
            case R.id.ll_home_moni:
                break;
            case R.id.ll_home_xiangmu:
                intent = new Intent(getActivity(), ActivityXiangMu.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(0,0);
                break;
        }
    }

    class myPicAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if(mDatas !=null){
                return  Integer.MAX_VALUE;
            }
            return 0;
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            int pos = position % mDatas.size();
            ImageView iv = new ImageView(getActivity());
            iv.setImageResource(mDatas.get(pos));
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(iv);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick(position % mDatas.size());
                }
            });
            return iv;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    public void itemClick(int pos){
        //        Toast.makeText(getActivity(),"您点击的是第 "+ (++pos) +" 个Item",Toast.LENGTH_SHORT).show();
    }

    private boolean isOnPause = false;
    @Override
    public void onPause() {
        isOnPause = true;
        if(mMyLunBO !=null) mMyLunBO.stopLunBO();
        super.onPause();
    }
    @Override
    public void onResume() {
        if(mMyLunBO == null){
            mMyLunBO = new MyLunBo(mContainer, mViewPage, mDatas);
            mMyLunBO.startLunBO();
        }
        if(isOnPause){//防止轮播图暂定不动
            if(mMyLunBO !=null) mMyLunBO.restartLunBO();
            isOnPause = false;
        }
        super.onResume();
    }
}
