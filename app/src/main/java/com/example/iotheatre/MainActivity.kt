package com.example.iotheatre

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {
    private val TAG = "IOTheater"
    private var btpermission = false
    private var macAddress = "98:DA:60:03:AC:23"
    var uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        val routineButton: View = findViewById(R.id.imageView)
        val dashboardButton: View = findViewById(R.id.imageView2)
        val connectButton: View = findViewById(R.id.buttonConnect)

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT > 31) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.BLUETOOTH_CONNECT), 100)
                return
            }
        }

        routineButton.setOnClickListener {
            val intent = Intent(this, RoutineActivity::class.java)
            startActivity(intent)
        }



        dashboardButton.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }




    }
    fun btScan(device:String){
        Toast.makeText(this,"Bluetooth connected succesfully to"+device, Toast.LENGTH_LONG).show()
    }

    @SuppressLint("MissingPermission")
    private val btActivityResultLauncher=registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
            result: ActivityResult ->
        if(result.resultCode==RESULT_OK){

            HC06Connection()


        }
    }


    @SuppressLint("MissingPermission")
    fun HC06Connection(){
        val bluetoothManager:BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter:BluetoothAdapter=bluetoothManager.adapter
        val HC06BluetoothModule: BluetoothDevice = bluetoothAdapter.getRemoteDevice(macAddress)
        val deviceUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // UUID universale
        var bluetoothSocket: BluetoothSocket? = null
        bluetoothSocket = HC06BluetoothModule.createRfcommSocketToServiceRecord(deviceUUID)
        bluetoothSocket?.connect()
        btScan(HC06BluetoothModule.name)
        var outputStream = bluetoothSocket?.outputStream


        if (outputStream == null) {
            Log.d(TAG, "Output stream error")
            return
        }
        try {
            var command = "a"

            command = """ $command""".trimIndent()
            outputStream.write(command.toByteArray())
            Log.d("Command- >", command)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }






      //  bluetoothSocket?.close()
    }


    private val blueToothPermissionLauncher=registerForActivityResult(
        RequestPermission()
    ){isGranted:Boolean ->
        if(isGranted){
            val bluetoothManager:BluetoothManager = getSystemService(BluetoothManager::class.java)
            val bluetoothAdapter:BluetoothAdapter=bluetoothManager.adapter
            btpermission = true
            if(bluetoothAdapter?.isEnabled==false){
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                btActivityResultLauncher.launch(enableBtIntent)
            }else{
                HC06Connection()

            }
        }else{
            btpermission = false
        }
    }

    fun scanBT(view: View){
        val bluetoothManager:BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter:BluetoothAdapter=bluetoothManager.adapter
        if (bluetoothAdapter==null){
            Toast.makeText(this,"Device doesn't support Bluetooth",Toast.LENGTH_LONG).show()

        }else{
            if(VERSION.SDK_INT>=Build.VERSION_CODES.S){
                blueToothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
            }else{
                blueToothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_ADMIN)
            }
        }

    }




}
