package com.example.iotheatre

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class RoutineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine)
        val bluetoothSocket = BluetoothSocketSingleton.bluetoothSocket
        val bluetoothAdapter = BluetoothAdapterSingleton.bluetoothAdapter
        // Inizializza i componenti UI e gestisci le azioni dell'utente qui
    }
}
