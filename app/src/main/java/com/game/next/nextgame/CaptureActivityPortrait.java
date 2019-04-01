package com.game.next.nextgame;

import android.content.Intent;

import com.journeyapps.barcodescanner.CaptureActivity;

public class CaptureActivityPortrait extends CaptureActivity {

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CaptureActivityPortrait.this,ActivityMeusJogos.class);
        startActivity(intent);
        finish();
    }
}
