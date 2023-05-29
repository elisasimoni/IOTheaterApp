package com.example.iotheatre.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.util.*

class BluetoothHelper(private val activity: AppCompatActivity) {
    private val TAG = "IOTheater"
    private var macAddress = "98:DA:60:03:AC:23"
    private var uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    fun initializeBluetooth() {
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT > 31) {
                ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.BLUETOOTH_CONNECT), 100)
                return
            }
        }

        val bluetoothManager: BluetoothManager = activity.getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter = bluetoothManager.adapter

        if (bluetoothAdapter == null) {
            Toast.makeText(activity, "Device doesn't support Bluetooth", Toast.LENGTH_LONG).show()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                blueToothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
            } else {
                blueToothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_ADMIN)
            }
        }
    }

    private val blueToothPermissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val bluetoothManager: BluetoothManager = activity.getSystemService(BluetoothManager::class.java)
            val bluetoothAdapter: BluetoothAdapter = bluetoothManager.adapter

            if (bluetoothAdapter?.isEnabled == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                btActivityResultLauncher.launch(enableBtIntent)
            } else {
                HC06Connection()
            }
        } else {
            // Bluetooth permission denied
        }
    }

    private val btActivityResultLauncher = activity.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            HC06Connection()
        }
    }

    @SuppressLint("MissingPermission")
    private fun HC06Connection() {
        val bluetoothManager: BluetoothManager = activity.getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter = bluetoothManager.adapter
        val HC06BluetoothModule: BluetoothDevice = bluetoothAdapter.getRemoteDevice(macAddress)
        val deviceUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // Universal UUID
        var bluetoothSocket: BluetoothSocket? = null
        bluetoothSocket = HC06BluetoothModule.createRfcommSocketToServiceRecord(deviceUUID)
        bluetoothSocket?.connect()
        btScan(HC06BluetoothModule.name)
        var outputStream = bluetoothSocket?.outputStream

        // Perform Bluetooth operations here

        // bluetoothSocket?.close()
    }

    private fun btScan(device: String) {
        Toast.makeText(activity, "Bluetooth connected successfully to $device", Toast.LENGTH_LONG).show()
    }
}
