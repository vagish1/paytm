import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'paytm_method_channel.dart';

abstract class PaytmPlatform extends PlatformInterface {
  /// Constructs a PaytmPlatform.
  PaytmPlatform() : super(token: _token);

  static final Object _token = Object();

  static PaytmPlatform _instance = MethodChannelPaytm();

  /// The default instance of [PaytmPlatform] to use.
  ///
  /// Defaults to [MethodChannelPaytm].
  static PaytmPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [PaytmPlatform] when
  /// they register themselves.
  static set instance(PaytmPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
