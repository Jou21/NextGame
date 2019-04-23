package com.game.next.nextgame;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PerguntaXboxOuPS4Activity extends AppCompatActivity {

    private Button btnXbox, btnPS4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pergunta_xbox_ou_ps4);

        btnXbox = (Button) findViewById(R.id.btn_pergunta_xbox);
        btnPS4 = (Button) findViewById(R.id.btn_pergunta_ps4);


        btnXbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result","XBOX");
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        btnPS4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result","PS4");
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }

}
