package com.zfxf.douniu.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zfxf.douniu.activity.pay.ActivityDeposit;
import com.zfxf.douniu.activity.pay.ActivityToPay;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		CommonUtils.logMes("onPayFinish, errCode = " + "-----------onResp-------="+resp.errCode);
		CommonUtils.logMes("onPayFinish, errCode = " + "-----------resp.getType()-------="+resp.getType());
		CommonUtils.logMes("onPayFinish, errCode = " + "-----------resp.errStr-------="+resp.errStr);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if(resp.errCode == 0){
				CommonUtils.toastMessage("支付成功");
				if(SpTools.getInt(CommonUtils.getContext(),Constants.payweixin,0) == 1){
					ActivityDeposit.getInstance().showResult(2);
				}else if(SpTools.getInt(CommonUtils.getContext(),Constants.payweixin,0) == 2){
					ActivityToPay.getInstance().showResult(2);//通知支付成功
				}
			}else if(resp.errCode == -1){
//				tv.setText("支付发生错误,请从新支付");
				CommonUtils.toastMessage("支付失败");
			}else if(resp.errCode == -2){
//				tv.setText("用户取消支付,请从新支付");
				CommonUtils.toastMessage("用户取消支付");
			}
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle("提示");
//			builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
//			builder.show();
		}
		finish();
	}
}