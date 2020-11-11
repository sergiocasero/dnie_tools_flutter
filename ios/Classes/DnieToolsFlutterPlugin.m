#import "DnieToolsFlutterPlugin.h"
#if __has_include(<dnie_tools_flutter/dnie_tools_flutter-Swift.h>)
#import <dnie_tools_flutter/dnie_tools_flutter-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "dnie_tools_flutter-Swift.h"
#endif

@implementation DnieToolsFlutterPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftDnieToolsFlutterPlugin registerWithRegistrar:registrar];
}
@end
