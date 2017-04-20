package com.zfxf.douniu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityResearch extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.et_research)
    EditText research;

    @BindView(R.id.tv_research_cannel)
    TextView cannel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research);
        ButterKnife.bind(this);
        initData();
        initListener();
    }

    private void initData() {


    }
    private void initListener() {
        cannel.setOnClickListener(this);
        research.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch(actionId){
                    case EditorInfo.IME_ACTION_SEARCH:
                        CommonUtils.toastMessage("开始搜索");
                        research.setText("");
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_research_cannel:
                research.setText("");
                break;
        }
    }

    private void finishAll() {

    }

}
