import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'paytm_platform_interface.dart';

/// An implementation of [PaytmPlatform] that uses method channels.
class MethodChannelPaytm extends PaytmPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('paytm');

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<Map<String, Object>?> payWithPaytm(
      {required String orderId,
      required String merchantId,
      required String amount,
      required String callBackUrl}) async {
    final paymentResponse = await methodChannel.invokeMapMethod('payNow');
    return paymentResponse?.cast<String, Object>();
  }
}
