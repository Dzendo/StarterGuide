package com.dinadurykina.starter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<View>(R.id.button)
        button.setOnClickListener {
            val options = GmsBarcodeScannerOptions.Builder()
                // чтобы обнаружить только  QR-коды
                .setBarcodeFormats(
                    Barcode.FORMAT_QR_CODE
                )
                // чтобы обнаружить только ацтекский код
                //  Barcode.FORMAT_AZTEC
                //Чтобы включить функцию автоматического масштабирования
                .enableAutoZoom() // available on 16.1.0 and higher
                .build()

            // Получите экземпляр GmsBarcodeScanner
            //val scanner = GmsBarcodeScanning.getClient(this)
            // Or with a configured options
            val scanner = GmsBarcodeScanning.getClient(this, options)

            var taskCompleted = "NONE"
            // Запросите сканирование кода
            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    val barcodeAll = barcode.rawValue
                    taskCompleted = "Task completed successfully ${barcode.rawValue}"

                    if (barcodeAll != null) {
                        if (barcodeAll.startsWith("com.dinadurykina.mediagid")) {
                            val intent = Intent("com.dinadurykina.mediagid.MediaGuide")
                            intent.putExtra("FragmentName", barcodeAll)
                            startActivity(intent)
                        }
                    }
                }
                .addOnCanceledListener {
                    taskCompleted = "Task canceled "
                    Toast.makeText(
                        this,
                        getString(R.string.error_scanner_cancelled),
                        Toast.LENGTH_LONG
                    ).show()
                }
                .addOnFailureListener { e: Exception ->
                    taskCompleted = "Task failed with an exception \n $e"
                    Toast.makeText(this, getErrorMessage(e), Toast.LENGTH_LONG).show()
                }
           // val intent = Intent("com.dinadurykina.mediagid.MediaGuide")
           // intent.putExtra("FragmentName", "com.dinadurykina.mediagid.nav_p_4")
           // startActivity(intent)
        }
    }

    private fun getSuccessfulMessage(barcode: Barcode): String {
        val barcodeValue =
            String.format(
                Locale.US,
                "Display Value: %s\nRaw Value: %s\nFormat: %s\nValue Type: %s",
                barcode.displayValue,
                barcode.rawValue,
                barcode.format,
                barcode.valueType
            )
        return getString(R.string.barcode_result, barcodeValue)
        //       return  barcodeValue
    }

    private fun getErrorMessage(e: Exception): String? {
        return if (e is MlKitException) {
            when (e.errorCode) {
                MlKitException.CODE_SCANNER_CAMERA_PERMISSION_NOT_GRANTED ->
                    getString(R.string.error_camera_permission_not_granted)

                MlKitException.CODE_SCANNER_APP_NAME_UNAVAILABLE ->
                    getString(R.string.error_app_name_unavailable)

                else -> getString(R.string.error_default_message, e)
            }
        } else {
            e.message
        }
    }
}