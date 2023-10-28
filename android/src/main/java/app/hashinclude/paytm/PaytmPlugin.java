package app.hashinclude.paytm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.TransactionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;

/** PaytmPlugin */
public class PaytmPlugin implements FlutterPlugin, MethodCallHandler, PluginRegistry.ActivityResultListener, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private final int PAYTM_REQUEST_CODE = 1001;


  private Activity activity;
  private  Result sendResult;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "paytm");
    channel.setMethodCallHandler(this);
  }




  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    sendResult = result;
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
      return;
    }

    if(call.method.equals("payNow")){
      Map<String,Object>  args = (Map<String, Object>) call.arguments;
      final PaytmOrder paytmOrder = new PaytmOrder(Objects.requireNonNull(args.get("orderId")).toString(), Objects.requireNonNull(args.get("mid")).toString(), Objects.requireNonNull(args.get("txnToken")).toString(), Objects.requireNonNull(args.get("amount")).toString(), Objects.requireNonNull(args.get("callBackUrl")).toString());
      final TransactionManager transactionManager = new TransactionManager(paytmOrder,new PaytmTransactionCallback(result));
      transactionManager.setAppInvokeEnabled(false);
      transactionManager.setShowPaymentUrl(Objects.requireNonNull(args.get("callBackUrl")).toString());
      transactionManager.setEmiSubventionEnabled((boolean)Objects.requireNonNull(args.get("emiAllowed")));
      transactionManager.startTransaction(activity, PAYTM_REQUEST_CODE);
      return;
    }
      result.notImplemented();

  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  @Override
  public boolean onActivityResult(int requestCode, int i1, @Nullable Intent intent) {

    if(requestCode == PAYTM_REQUEST_CODE && intent !=null){
      Map<String,Object> paymentResponse = new HashMap<String,Object>();


      if(intent.getStringExtra("response")!=null && intent.getExtras()!=null){

        paymentResponse.put("errorOccurred",false);
        for(String key : intent.getExtras().keySet()){
          paymentResponse.put(key,paymentResponse.get(key));
        }


      }else{
        paymentResponse.put("errorOccurred",true);
        paymentResponse.put("messageFromPaytm",intent.getStringExtra("nativeSdkForMerchantMessage"));

      }

      sendResult.success(paymentResponse);
      return  false;
    }

    return false;
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding activityPluginBinding) {
    activity = activityPluginBinding.getActivity();
    activityPluginBinding.addActivityResultListener(this);
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  activity = null;

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding activityPluginBinding) {
      activity  = activityPluginBinding.getActivity();

  }

  @Override
  public void onDetachedFromActivity() {
    activity = null;

  }
}
