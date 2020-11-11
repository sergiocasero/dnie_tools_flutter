# DNIe tools

Flutter DNIe tools

# Disclaimer

This library is under heavy development!! For sure I will improve it on next versions so be carefull to use it!!

## Getting Started

This project is a
[plug-in package](https://flutter.dev/developing-packages/),
to get information from the DNIe 3.0 (spanish official document)

Right is still WIP and only works on iOS

### What can this libray do?

- Get information from the DNIe 3.0:

    - nif
    - birth
    - expiry
    - docNumber
    - expeditionCountry
    - sex
    - nationality
    - name
    - surname
    - address
    - city
    - state
    - DNIe image
    - Sign image

### Reqs
- Android 19+
- NFC on your smartphone

### Install

WIP

### How to use it

That's simple, whew  you want to get the DNIe data, do:

1. Listen for changes:
```dart
DnieTools.stream.listen((dnieResponse) {
  // Event is the response
}
```

2. Read DNIe
```dart
await DnieTools.read("DNIe_CAN");
```


### Event data detail
An `event` is a `DnieResponse`, it has three attrs:
1. status. With these values: 
    - `INIT`: Initialize received
    - `READY`: Ready, now you can tap your DNIe to the phone
    - `IN_PROGRESS:` Reading data
    - `INFO`: Here you can find the DNIe data in the `data` attribute
    - `IMAGE`: Here you can find the image file path in the `data` attribute, take a look also to `type`
    - `ERROR`: An error has ocurred, the error message comes from `data`

2. type. Only applies on:
    - `IN_PROGRESS`: Type could be `INFO` (getting info), `PICTURE` (getting DNIe picture) and `SIGN` (getting image sign)
    - `IMAGE`: 
        - `PICTURE`
        - `SIGN`
3. data: Only applies on:
    - `INFO`: The DNIe info (birth, name, surname, nif, etc)
4. uri: Only applies on:
    - `IMAGE`: Here you will get the image URI, simply call `Image.file(File(uri))` to see the pic
Q&A

Why do I need to pass the DNIe CAN? -> Because It's mandatory, take a look to [dnie official website](https://www.dnielectronico.es/PortalDNIe/) for more info
Why doesn't works on iOS? Ask Tim Cook & Spanish government, we can't use NFC :( 