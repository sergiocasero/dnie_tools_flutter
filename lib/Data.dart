class Data {
  String address;
  String birth;
  String city;
  String docNumber;
  String expeditionCountry;
  String expiry;
  String name;
  String nationality;
  String nif;
  String sex;
  String state;
  String surname;

  Data(
      {this.address,
      this.birth,
      this.city,
      this.docNumber,
      this.expeditionCountry,
      this.expiry,
      this.name,
      this.nationality,
      this.nif,
      this.sex,
      this.state,
      this.surname});

  factory Data.fromJson(Map<String, dynamic> json) {
    return Data(
      address: json['address'],
      birth: json['birth'],
      city: json['city'],
      docNumber: json['docNumber'],
      expeditionCountry: json['expeditionCountry'],
      expiry: json['expiry'],
      name: json['name'],
      nationality: json['nationality'],
      nif: json['nif'],
      sex: json['sex'],
      state: json['state'],
      surname: json['surname'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['address'] = this.address;
    data['birth'] = this.birth;
    data['city'] = this.city;
    data['docNumber'] = this.docNumber;
    data['expeditionCountry'] = this.expeditionCountry;
    data['expiry'] = this.expiry;
    data['name'] = this.name;
    data['nationality'] = this.nationality;
    data['nif'] = this.nif;
    data['sex'] = this.sex;
    data['state'] = this.state;
    data['surname'] = this.surname;
    return data;
  }

  @override
  String toString() {
    return 'Data{address: $address, birth: $birth, city: $city, docNumber: $docNumber, expeditionCountry: $expeditionCountry, expiry: $expiry, name: $name, nationality: $nationality, nif: $nif, sex: $sex, state: $state, surname: $surname}';
  }
}
