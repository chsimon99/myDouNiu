package com.zfxf.douniu.activity.myself;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:19
 * @des    我的 评估报告结果
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityMyselfRvaluateResult extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tv_rvaluate_result)
    TextView result;
    @BindView(R.id.tv_rvaluate_content)
    TextView content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_rvaluate_result);
        ButterKnife.bind(this);

        title.setText("评估报告");
        initdata();
        initListener();
    }

    private void initdata() {
        int resultNum = SpTools.getInt(this, Constants.rvaluateResult, 0);
        Log.d("------------","resultNum="+resultNum);
        if(resultNum > 36 && resultNum <73){
            result.setText(R.string.evaluate_second_result);
            content.setText(R.string.evaluate_second_content);
        }else if(resultNum > 72){
            result.setText(R.string.evaluate_third_result);
            content.setText(R.string.evaluate_third_content);
        }
        SpTools.setBoolean(this,Constants.rvaluateSuccess,true);
    }
    private void initListener() {
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
        }
    }

    private void finishAll() {

    }
}
