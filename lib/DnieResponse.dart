import 'Data.dart';

class DnieResponse {
  Data data;
  String type;
  String status;
  String uri;

  DnieResponse({this.data, this.type, this.status, this.uri});

  factory DnieResponse.fromJson(Map<String, dynamic> json) {
    return DnieResponse(
      data: json['data'] != null ? Data.fromJson(json['data']) : null,
      type: json['type'],
      status: json['status'],
      uri: json['uri'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['type'] = this.type;
    data['status'] = this.status;
    data['uri'] = this.uri;
    if (this.data != null) {
      data['data'] = this.data.toJson();
    }
    return data;
  }

  @override
  String toString() {
    return 'DnieResponse{data: $data, type: $type, status: $status, uri: $uri}';
  }
}
