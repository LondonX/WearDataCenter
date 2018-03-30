# Wear Data Center
Make data transmission easier
## Usage
* Install app on your phone and wearable
* Test it to see if data can transfer between your devices
* Import `weardata` module copy `DataConst.kt` into your project to use Broadcast action and data
## Modules
### mobile
Receive data from wear app and send it by `Broadcast`, see broadcast data in module **weardata**
### wear
Receive data from user and send it by `MessageClient`, see messafe data in module **weardata**
### weardata
Contains all const used in other modules, if you want to handle messages in your app, you need to **import this module** or **copy `DataConst.kt`** into your project