import 'package:flutter_test/flutter_test.dart';
import 'package:paytm/paytm.dart';
import 'package:paytm/paytm_platform_interface.dart';
import 'package:paytm/paytm_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockPaytmPlatform
    with MockPlatformInterfaceMixin
    implements PaytmPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final PaytmPlatform initialPlatform = PaytmPlatform.instance;

  test('$MethodChannelPaytm is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelPaytm>());
  });

  test('getPlatformVersion', () async {
    Paytm paytmPlugin = Paytm();
    MockPaytmPlatform fakePlatform = MockPaytmPlatform();
    PaytmPlatform.instance = fakePlatform;

    expect(await paytmPlugin.getPlatformVersion(), '42');
  });
}
