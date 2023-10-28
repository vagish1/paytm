package app.hashinclude.paytm;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.MethodChannel;

public class PaytmTransactionCallback implements PaytmPaymentTransactionCallback {

    final MethodChannel.Result result;

    public PaytmTransactionCallback(MethodChannel.Result result) {
        this.result = result;
    }


    @Override
    public void onTransactionResponse(@Nullable Bundle bundle) {
        Map<String,Object> paymentResponse = new HashMap<String,Object>();
        if( bundle!=null){

            paymentResponse.put("errorOccurred",false);
            for(String key : bundle.keySet()){
                paymentResponse.put(key,paymentResponse.get(key));
            }


        }else{
            paymentResponse.put("errorOccurred",true);
            String msg ="Unexpected error";
            if(bundle.get("nativeSdkForMerchantMessage")!=null){
                msg = bundle.get("nativeSdkForMerchantMessage").toString();
            }
            paymentResponse.put("messageFromPaytm",msg);

        }

        result.success(paymentResponse);
    }

    @Override
    public void networkNotAvailable() {
        Map<String,Object> paymentResponse = new HashMap<String,Object>();

        paymentResponse.put("errorOccurred",true);
        paymentResponse.put("errorType","NETWORK_UNAVAILABLE");
        paymentResponse.put("message","While paying the directed amount the phone internet was not available");
        result.error("errorOccurred","NetworkUnavailable",paymentResponse);
    }

    @Override
    public void onErrorProceed(String s) {
        Map<String,Object> paymentResponse = new HashMap<String,Object>();

        paymentResponse.put("errorOccurred",true);
        paymentResponse.put("errorType","STARTING_PAYMENT_FAILED");
        paymentResponse.put("message",s);
        result.error("UnKnownError","Can't continue Further",paymentResponse);

    }

    @Override
    public void clientAuthenticationFailed(String s) {
        Map<String,Object> paymentResponse = new HashMap<String,Object>();

        paymentResponse.put("errorOccurred",true);
        paymentResponse.put("errorType","CLIENT_AUTHENTICATION_ERROR");
        paymentResponse.put("message",s);
        result.error("CLIENT_ERROR","Can't continue Further",paymentResponse);

    }

    @Override
    public void someUIErrorOccurred(String s) {
        Map<String,Object> paymentResponse = new HashMap<String,Object>();

        paymentResponse.put("errorOccurred",true);
        paymentResponse.put("errorType","UI_ERROR_OCCURRED");
        paymentResponse.put("message",s);
        result.error("UI_ERROR","Can't continue Further",paymentResponse);
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Map<String,Object> paymentResponse = new HashMap<String,Object>();

        paymentResponse.put("errorOccurred",true);
        paymentResponse.put("errorType","ERROR_WHILE_LOADING_WEBPAGE");
        paymentResponse.put("message",s);
        paymentResponse.put("failingURL",s1);

        result.error("LOADING_ERROR","Can't continue Further",paymentResponse);
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Map<String,Object> paymentResponse = new HashMap<String,Object>();

        paymentResponse.put("errorOccurred",true);
        paymentResponse.put("errorType","BACK_PRESSED_IN_PAYMENT");
        paymentResponse.put("message","User pressed back button in between the payment");


        result.error("USER_ERROR","Can't continue Further",paymentResponse);
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Map<String,Object> paymentResponse = new HashMap<String,Object>();

        paymentResponse.put("errorOccurred",true);
        paymentResponse.put("errorType","PAYMENT_ERROR");
        paymentResponse.put("message",s);
        for(String key : bundle.keySet()){
            paymentResponse.put(key,paymentResponse.get(key));
        }



        result.error("USER_ERROR","Can't continue Further",paymentResponse);
    }
}
