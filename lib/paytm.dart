// You have generated a new plugin project without specifying the `--platforms`
// flag. A plugin project with no platform support was generated. To add a
// platform, run `flutter create -t plugin --platforms <platforms> .` under the
// same directory. You can also find a detailed instruction on how to add
// platforms in the `pubspec.yaml` at
// https://flutter.dev/docs/development/packages-and-plugins/developing-packages#plugin-platforms.

import 'paytm_platform_interface.dart';

class Paytm {
  Future<String?> getPlatformVersion() {
    return PaytmPlatform.instance.getPlatformVersion();
  }

  Future<Map<String, Object>?> payNow(
      {required String orderId,
      required String merchantId,
      required String amount,
      required String transxToken,
      required String callBackUrl}) {
    return PaytmPlatform.instance.payWithPaytm(
      orderId: orderId,
      merchantId: merchantId,
      amount: amount,
      callBackUrl: callBackUrl,
      transxToken: transxToken,
    );
  }
}
