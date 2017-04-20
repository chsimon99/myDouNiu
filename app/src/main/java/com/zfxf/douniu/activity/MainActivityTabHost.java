package com.zfxf.douniu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.base.BaseApplication;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.fragment.FragmentAdvisor;
import com.zfxf.douniu.view.fragment.FragmentHome;
import com.zfxf.douniu.view.fragment.FragmentMarket;
import com.zfxf.douniu.view.fragment.FragmentMyself;
import com.zfxf.douniu.view.fragment.FragmentNews;

/**
 * 
 * @author zqy
 * 
 */
public class MainActivityTabHost extends FragmentActivity {

	private FragmentTabHost mTabHost;
	private LayoutInflater mLayoutInflater;

	private Class mFragmentArray[] = { FragmentHome.class, FragmentNews.class,FragmentAdvisor.class, FragmentMarket.class, FragmentMyself.class };

	private int mImageArray[] = { R.drawable.tab_home_btn,R.drawable.tab_news_btn, R.drawable.tab_advisor_btn,
			R.drawable.tab_market_btn, R.drawable.tab_myself_btn};

	private String mTextArray[] = CommonUtils.getResource().getStringArray(R.array.tab_titles);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabhost);
		initView();
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		mLayoutInflater = LayoutInflater.from(this);

		// 找到TabHost
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		mTabHost.getTabWidget().setDividerDrawable(null);//去掉中间间隔线
		// 得到fragment的个数
		int count = mFragmentArray.length;
		for (int i = 0; i < count; i++) {
			// 给每个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(mTextArray[i]).setIndicator(getTabItemView(i));
			// 将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, mFragmentArray[i], null);
			// 设置Tab按钮的背景
			mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
			mTabHost.getTabWidget().getChildAt(i).getLayoutParams().height = CommonUtils.dip2px(this,56);//设置高度
		}
	}

	/**
	 * 给每个Tab按钮设置图标和文字
	 */
	private View getTabItemView(int index) {
		View view = mLayoutInflater.inflate(R.layout.tab_item_view, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageArray[index]);
		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText(mTextArray[index]);
		return view;
	}

	private long exitTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			if((System.currentTimeMillis()-exitTime) > 2000){
				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				BaseApplication.getThreadPool().shutdownNow();//关闭线程池
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
