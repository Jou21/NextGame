package com.game.next.nextgame;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.ViewfinderView;

import java.util.Random;

public class CustomScannerActivity extends Activity implements
        DecoratedBarcodeView.TorchListener {

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private FloatingActionButton switchFlashlightButton;
    private ViewfinderView viewfinderView;
    private Button btnSemCodigoDeBarras;

    private boolean luzLigada = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scanner);

        luzLigada = false;

        Window window = getWindow();

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        barcodeScannerView = (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scanner);
        barcodeScannerView.setTorchListener(this);

        switchFlashlightButton = (FloatingActionButton)findViewById(R.id.switch_flashlight);
        btnSemCodigoDeBarras = (Button) findViewById(R.id.btn_sem_codigo_de_barras);

        btnSemCodigoDeBarras.setText("O JOGO NÃO TEM CÓDIGO DE BARRAS \nPEGAR JOGO POR LOCALIZAÇÃO PRÓXIMA");

        btnSemCodigoDeBarras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getIntent().getAction());
                intent.putExtra("SCAN_RESULT", "0000000000000");
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        viewfinderView = (ViewfinderView) findViewById(R.id.zxing_viewfinder_view);

        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...
        if (!hasFlash()) {
            switchFlashlightButton.setVisibility(View.GONE);
        }

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

        changeMaskColor(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    /**
     * Check if the device's camera has a Flashlight.
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight(View view) {

        if (luzLigada == true) {
            barcodeScannerView.setTorchOff();
            luzLigada = false;
        } else {
            barcodeScannerView.setTorchOn();
            luzLigada = true;
        }

    }

    public void changeMaskColor(View view) {
        Random rnd = new Random();
        int color = Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        //viewfinderView.setBackgroundColor(color);
    }

    @Override
    public void onTorchOn() {
        //switchFlashlightButton.setText(R.string.turn_off_flashlight);
        switchFlashlightButton.setImageDrawable(ContextCompat.getDrawable(CustomScannerActivity.this, R.drawable.ic_flash_on_black_24dp));
        switchFlashlightButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF6F22")));
    }

    @Override
    public void onTorchOff() {
        //switchFlashlightButton.setText(R.string.turn_on_flashlight);
        switchFlashlightButton.setImageDrawable(ContextCompat.getDrawable(CustomScannerActivity.this, R.drawable.ic_flash_off_black_24dp));
        switchFlashlightButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0065DE")));
    }
}
