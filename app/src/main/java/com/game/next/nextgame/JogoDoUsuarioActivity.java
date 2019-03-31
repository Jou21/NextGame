package com.game.next.nextgame;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.game.next.nextgame.entidades.Jogo;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class JogoDoUsuarioActivity extends AppCompatActivity {

    private RatingBar ratingBarUser,ratingBarJogo;
    private ImageView imgUserJogoDoUsuario,imgDoJogoDoUsuario;
    private TextView txtNomeUserJogoDoUsuario,txtTituloJogoDoUsuario,descricaoDoJogoDoUsuario;
    private Button btnAlugarJogoDoUsuario,btnComprarJogoDoUsuario;

    private Jogo model;
    private String imgOtherUserURL;
    private String nomeOtherUser;
    private String precoAluguel;
    private String precoVenda;

    private LayerDrawable starsUser, starsJogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo_do_usuario);

        ratingBarUser = (RatingBar) findViewById(R.id.rating_bar_user_jogo_do_usuario);
        ratingBarJogo = (RatingBar) findViewById(R.id.rating_bar_jogo_do_usuario);
        imgUserJogoDoUsuario = (ImageView) findViewById(R.id.img_user_jogo_do_usuario);
        imgDoJogoDoUsuario = (ImageView) findViewById(R.id.img_do_jogo_do_usuario);
        txtNomeUserJogoDoUsuario = (TextView) findViewById(R.id.txt_nome_user_jogo_do_usuario);
        txtTituloJogoDoUsuario = (TextView) findViewById(R.id.txt_titulo_jogo_do_usuario);
        descricaoDoJogoDoUsuario = (TextView) findViewById(R.id.descricao_do_jogo_do_usuario);
        btnAlugarJogoDoUsuario = (Button) findViewById(R.id.btn_alugar_jogo_do_usuario);
        btnComprarJogoDoUsuario = (Button) findViewById(R.id.btn_comprar_jogo_do_usuario);

        LayerDrawable starsUser = (LayerDrawable) ratingBarUser.getProgressDrawable();
        starsUser.getDrawable(2).setColorFilter(getResources().getColor(R.color.amarelo),PorterDuff.Mode.SRC_ATOP); // for filled stars
        starsUser.getDrawable(1).setColorFilter(getResources().getColor(R.color.gold),PorterDuff.Mode.SRC_ATOP); // for half filled stars
        starsUser.getDrawable(0).setColorFilter(getResources().getColor(R.color.cinza),PorterDuff.Mode.SRC_ATOP); // for empty stars

        ratingBarUser.setMax(5);
        ratingBarUser.setRating(5);
        ratingBarUser.setIsIndicator(true);

        LayerDrawable starsJogo = (LayerDrawable) ratingBarUser.getProgressDrawable();
        starsJogo.getDrawable(2).setColorFilter(getResources().getColor(R.color.amarelo),PorterDuff.Mode.SRC_ATOP); // for filled stars
        starsJogo.getDrawable(1).setColorFilter(getResources().getColor(R.color.gold),PorterDuff.Mode.SRC_ATOP); // for half filled stars
        starsJogo.getDrawable(0).setColorFilter(getResources().getColor(R.color.cinza),PorterDuff.Mode.SRC_ATOP); // for empty stars

        ratingBarJogo.setMax(5);
        ratingBarJogo.setIsIndicator(true);


        if (getIntent().hasExtra("JOGOUSER")) {
            model = (Jogo) getIntent().getSerializableExtra("JOGOUSER");

            //if(model.getUrlImgComplementar1().equals("") || model.getUrlImgComplementar1().isEmpty()){
                Picasso.get().load(model.getUrlImgJogo()).into(imgDoJogoDoUsuario, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                /*
            }else{
                Picasso.get().load(model.getUrlImgComplementar1()).into(imgDoJogoDoUsuario, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
            */
            if(model.getNome().equals("") || model.getNome().isEmpty()){

            }else{
                txtTituloJogoDoUsuario.setText(model.getNome());
            }

            if(model.getDescricao().equals("") || model.getDescricao().isEmpty()){

            }else{
                descricaoDoJogoDoUsuario.setText(model.getDescricao());
            }

            if(model.getRating().equals("") || model.getRating().isEmpty()){
                ratingBarJogo.setRating(4);
            }else{
                ratingBarJogo.setRating(Float.parseFloat(model.getRating()));
            }

            //=============================================================================================================================================

            if (getIntent().hasExtra("IMAGEMUSER")) {
                imgOtherUserURL = getIntent().getStringExtra("IMAGEMUSER");

                if (!imgOtherUserURL.equals("default")){

                    Picasso.get().load(imgOtherUserURL).into(imgUserJogoDoUsuario, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });

                } else {
                    imgUserJogoDoUsuario.setImageResource(R.mipmap.ic_launcher);
                }

            } else {
                Toast.makeText(JogoDoUsuarioActivity.this,"Activity cannot find  extras " + "JOGO",Toast.LENGTH_SHORT).show();
                Log.d("EXTRASJOGO","Activity cannot find  extras " + "IMAGEMUSER");
            }

            //=============================================================================================================================================

            if (getIntent().hasExtra("NOMEUSER")) {
                nomeOtherUser = getIntent().getStringExtra("NOMEUSER");

                txtNomeUserJogoDoUsuario.setText(nomeOtherUser);

            } else {
                Toast.makeText(JogoDoUsuarioActivity.this,"Activity cannot find  extras " + "JOGO",Toast.LENGTH_SHORT).show();
                Log.d("EXTRASJOGO","Activity cannot find  extras " + "NOMEUSER");
            }

            //=============================================================================================================================================

            if (getIntent().hasExtra("PRECOALUGUEL")) {
                precoAluguel = getIntent().getStringExtra("PRECOALUGUEL");

                btnAlugarJogoDoUsuario.setText("R$" + precoAluguel + ",00" + "\nALUGAR");
            } else {
                Toast.makeText(JogoDoUsuarioActivity.this,"Activity cannot find  extras " + "JOGO",Toast.LENGTH_SHORT).show();
                Log.d("EXTRASJOGO","Activity cannot find  extras " + "PRECOALUGUEL");
            }

            //=============================================================================================================================================

            if (getIntent().hasExtra("PRECOVENDA")) {
                precoVenda = getIntent().getStringExtra("PRECOVENDA");

                btnComprarJogoDoUsuario.setText("R$" + precoVenda + ",00" + "\nCOMPRAR");
            } else {
                Toast.makeText(JogoDoUsuarioActivity.this,"Activity cannot find  extras " + "JOGO",Toast.LENGTH_SHORT).show();
                Log.d("EXTRASJOGO","Activity cannot find  extras " + "PRECOVENDA");
            }

            //=============================================================================================================================================

        } else {
            Toast.makeText(JogoDoUsuarioActivity.this,"Activity cannot find  extras " + "JOGO",Toast.LENGTH_SHORT).show();
            Log.d("EXTRASJOGO","Activity cannot find  extras " + "JOGOUSER");
        }
    }
}
