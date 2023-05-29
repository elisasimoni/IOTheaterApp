'use strict';

var tslib = require('tslib');
var i0 = require('@angular/core');
var core = require('@awesome-cordova-plugins/core');
require('rxjs');

function _interopNamespaceDefault(e) {
    var n = Object.create(null);
    if (e) {
        Object.keys(e).forEach(function (k) {
            if (k !== 'default') {
                var d = Object.getOwnPropertyDescriptor(e, k);
                Object.defineProperty(n, k, d.get ? d : {
                    enumerable: true,
                    get: function () { return e[k]; }
                });
            }
        });
    }
    n.default = e;
    return Object.freeze(n);
}

var i0__namespace = /*#__PURE__*/_interopNamespaceDefault(i0);

var BluetoothSerial = /** @class */ (function (_super) {
    tslib.__extends(BluetoothSerial, _super);
    function BluetoothSerial() {
        return _super !== null && _super.apply(this, arguments) || this;
    }
    BluetoothSerial.prototype.connect = function (macAddress_or_uuid) { return core.cordova(this, "connect", { "platforms": ["Android", "iOS", "Windows Phone"], "observable": true, "clearFunction": "disconnect" }, arguments); };
    BluetoothSerial.prototype.connectInsecure = function (macAddress) { return core.cordova(this, "connectInsecure", { "platforms": ["Android"], "observable": true, "clearFunction": "disconnect" }, arguments); };
    BluetoothSerial.prototype.disconnect = function () { return core.cordova(this, "disconnect", {}, arguments); };
    BluetoothSerial.prototype.write = function (data) { return core.cordova(this, "write", { "platforms": ["Android", "iOS", "Windows Phone"] }, arguments); };
    BluetoothSerial.prototype.available = function () { return core.cordova(this, "available", { "platforms": ["Android", "iOS", "Windows Phone"] }, arguments); };
    BluetoothSerial.prototype.read = function () { return core.cordova(this, "read", { "platforms": ["Android", "iOS", "Windows Phone"] }, arguments); };
    BluetoothSerial.prototype.readUntil = function (delimiter) { return core.cordova(this, "readUntil", { "platforms": ["Android", "iOS", "Windows Phone"] }, arguments); };
    BluetoothSerial.prototype.subscribe = function (delimiter) { return core.cordova(this, "subscribe", { "platforms": ["Android", "iOS", "Windows Phone"], "observable": true, "clearFunction": "unsubscribe" }, arguments); };
    BluetoothSerial.prototype.subscribeRawData = function () { return core.cordova(this, "subscribeRawData", { "platforms": ["Android", "iOS", "Windows Phone"], "observable": true, "clearFunction": "unsubscribeRawData" }, arguments); };
    BluetoothSerial.prototype.clear = function () { return core.cordova(this, "clear", { "platforms": ["Android", "iOS", "Windows Phone"] }, arguments); };
    BluetoothSerial.prototype.list = function () { return core.cordova(this, "list", { "platforms": ["Android", "iOS", "Windows Phone"] }, arguments); };
    BluetoothSerial.prototype.isEnabled = function () { return core.cordova(this, "isEnabled", { "platforms": ["Android", "iOS", "Windows Phone"] }, arguments); };
    BluetoothSerial.prototype.isConnected = function () { return core.cordova(this, "isConnected", { "platforms": ["Android", "iOS", "Windows Phone"] }, arguments); };
    BluetoothSerial.prototype.readRSSI = function () { return core.cordova(this, "readRSSI", { "platforms": ["Android", "iOS", "Windows Phone"] }, arguments); };
    BluetoothSerial.prototype.showBluetoothSettings = function () { return core.cordova(this, "showBluetoothSettings", { "platforms": ["Android", "iOS", "Windows Phone"] }, arguments); };
    BluetoothSerial.prototype.enable = function () { return core.cordova(this, "enable", { "platforms": ["Android", "iOS", "Windows Phone"] }, arguments); };
    BluetoothSerial.prototype.discoverUnpaired = function () { return core.cordova(this, "discoverUnpaired", { "platforms": ["Android", "iOS", "Windows Phone"] }, arguments); };
    BluetoothSerial.prototype.setDeviceDiscoveredListener = function () { return core.cordova(this, "setDeviceDiscoveredListener", { "platforms": ["Android", "iOS", "Windows Phone"], "observable": true, "clearFunction": "clearDeviceDiscoveredListener" }, arguments); };
    BluetoothSerial.prototype.setName = function (newName) { return core.cordova(this, "setName", { "platforms": ["Android"], "sync": true }, arguments); };
    BluetoothSerial.prototype.setDiscoverable = function (discoverableDuration) { return core.cordova(this, "setDiscoverable", { "platforms": ["Android"], "sync": true }, arguments); };
    BluetoothSerial.ɵfac = i0__namespace.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "12.2.16", ngImport: i0__namespace, type: BluetoothSerial, deps: null, target: i0__namespace.ɵɵFactoryTarget.Injectable });
    BluetoothSerial.ɵprov = i0__namespace.ɵɵngDeclareInjectable({ minVersion: "12.0.0", version: "12.2.16", ngImport: i0__namespace, type: BluetoothSerial });
    BluetoothSerial.pluginName = "BluetoothSerial";
    BluetoothSerial.repo = "https://github.com/don/BluetoothSerial";
    BluetoothSerial.plugin = "cordova-plugin-bluetooth-serial";
    BluetoothSerial.pluginRef = "bluetoothSerial";
    BluetoothSerial.platforms = ["Android", "iOS", "Windows Phone 8"];
    BluetoothSerial = tslib.__decorate([], BluetoothSerial);
    return BluetoothSerial;
}(core.AwesomeCordovaNativePlugin));
i0__namespace.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "12.2.16", ngImport: i0__namespace, type: BluetoothSerial, decorators: [{
            type: i0.Injectable
        }], propDecorators: { connect: [], connectInsecure: [], disconnect: [], write: [], available: [], read: [], readUntil: [], subscribe: [], subscribeRawData: [], clear: [], list: [], isEnabled: [], isConnected: [], readRSSI: [], showBluetoothSettings: [], enable: [], discoverUnpaired: [], setDeviceDiscoveredListener: [], setName: [], setDiscoverable: [] } });

exports.BluetoothSerial = BluetoothSerial;
