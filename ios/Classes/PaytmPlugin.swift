import Flutter
import UIKit
import AppInvokeSDK


public class PaytmPlugin: NSObject, FlutterPlugin, AIDelegate  {
    
    private var appInvoke :AIHandler = AIHandler()
    private var paymentResult : FlutterResult?
    public func openPaymentWebVC(_ controller: UIViewController?) {
        if let vc = controller {
            DispatchQueue.main.async {[weak self] in
                UIApplication.self.shared.keyWindow?.rootViewController?.present(vc, animated: true)
                
                    }
                }
    }
    
    public func didFinish(with status: AppInvokeSDK.AIPaymentStatus, response: [String : Any]) {
        
        var responseResult =  [String:Any]()
        
        if(status == AIPaymentStatus.success){
            
            responseResult["errorOccurred"] = false
            responseResult["response"] = response
            
            paymentResult!(responseResult)
            return;
        }
        
        if(status == AIPaymentStatus.failed){
            responseResult["errorOccurred"] = true
            responseResult["errorType"] = "PAYMENT_FAILED"
            responseResult["message"] = "Payment has been failed"
            responseResult["response"] = response;
            paymentResult!(responseResult)
            return;
        }
        
        if(status == AIPaymentStatus.cancel){
            responseResult["errorOccurred"] = true
            responseResult["errorType"] = "PAYMENT_CANCELLED"
            responseResult["message"] = "Payment has been cancelled"
            responseResult["response"] = response;
          
            paymentResult!(responseResult)
            return;
        }
        
        if(status == AIPaymentStatus.pending){
            responseResult["errorOccurred"] = true
            responseResult["errorType"] = "PAYMENT_PENDING"
            responseResult["message"] = "Payment has been pending from bank"
            responseResult["response"] = response;
          
            paymentResult!(responseResult)
            return;
        }
    }
    
    
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "paytm", binaryMessenger: registrar.messenger())
    let instance = PaytmPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }
    
    public func application(_ application: UIApplication, handleOpen url: URL) -> Bool {
        var dict = [String:String]()
               
               
               let components = URLComponents(url: url, resolvingAgainstBaseURL: false)!
               if let queryItems = components.queryItems {
                   for item in queryItems {
                       dict[item.name] = item.value!
                   }
               }
               print(dict)
               
               var paramMap = [String: Any]()

               if dict["response"] != nil && dict["response"]!.count > 0{
                   paramMap["error"]=false
                   paramMap["response"]=dict["response"]

               }else{
                   paramMap["error"]=true
                   paramMap["errorMessage"]="Transaction Cancelled"
                   paramMap["status"]=dict["status"]
               }
               
               paymentResult!(paramMap)
        return true;
        
    }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
      paymentResult = result
    switch call.method {
    case "getPlatformVersion":
      result("iOS " + UIDevice.current.systemVersion)

    case "payNow":
        var arguments =  call.arguments as? NSDictionary
        
        let mId = arguments!["mid"] as! String
                   let orderId = arguments!["orderId"] as! String
                   let amount = arguments!["amount"] as! String
                   let txnToken = arguments!["txnToken"] as! String
                   let callBackUrl = arguments!["callBackUrl"] as! String
                   let isStaging = arguments!["isStaging"] as! Bool
                   
                   var environment:AIEnvironment;
                   
                   if(isStaging){
                       environment=AIEnvironment.staging
                   }else{
                       environment=AIEnvironment.production
                   }
                   
                   print(callBackUrl);
                   
                   
                   appInvoke.openPaytm(merchantId: mId, orderId: orderId, txnToken: txnToken, amount: amount, callbackUrl: callBackUrl , delegate: self, environment: environment,urlScheme: "paytm"+mId)
        result("ios")
        

    default:
      result(FlutterMethodNotImplemented)
    }
  }
}
