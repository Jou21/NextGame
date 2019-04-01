package com.game.next.nextgame;

import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.asksira.loopingviewpager.LoopingViewPager;
import com.game.next.nextgame.adapters.MyViewPagerAdapter;
import com.game.next.nextgame.entidades.Jogo;
import com.rd.PageIndicatorView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class JogoDoUsuarioActivity extends AppCompatActivity {

    private RatingBar ratingBarUser,ratingBarJogo;
    private CircleImageView imgUserJogoDoUsuario;
    private TextView txtNomeUserJogoDoUsuario,txtTituloJogoDoUsuario,descricaoDoJogoDoUsuario;
    private Button btnAlugarJogoDoUsuario,btnComprarJogoDoUsuario;

    public static Jogo modelJodoDoUsuario;
    private String imgOtherUserURL;
    private String nomeOtherUser;
    private String precoAluguel;
    private String precoVenda;

    private LoopingViewPager viewPager;
    private MyViewPagerAdapter adapter;
    private PageIndicatorView indicatorView;

    private LayerDrawable starsUser, starsJogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo_do_usuario);

        viewPager = (LoopingViewPager) findViewById(R.id.viewpager);
        indicatorView = (PageIndicatorView) findViewById(R.id.indicator);

        adapter = new MyViewPagerAdapter(this, createDummyItems(), true);

        ratingBarUser = (RatingBar) findViewById(R.id.rating_bar_user_jogo_do_usuario);
        ratingBarJogo = (RatingBar) findViewById(R.id.rating_bar_jogo_do_usuario);
        imgUserJogoDoUsuario = (CircleImageView) findViewById(R.id.img_user_jogo_do_usuario);
        txtNomeUserJogoDoUsuario = (TextView) findViewById(R.id.txt_nome_user_jogo_do_usuario);
        txtTituloJogoDoUsuario = (TextView) findViewById(R.id.txt_titulo_jogo_do_usuario);
        descricaoDoJogoDoUsuario = (TextView) findViewById(R.id.descricao_do_jogo_do_usuario);
        btnAlugarJogoDoUsuario = (Button) findViewById(R.id.btn_alugar_jogo_do_usuario);
        btnComprarJogoDoUsuario = (Button) findViewById(R.id.btn_comprar_jogo_do_usuario);

        starsUser = (LayerDrawable) ratingBarUser.getProgressDrawable();
        starsUser.getDrawable(2).setColorFilter(getResources().getColor(R.color.amarelo),PorterDuff.Mode.SRC_ATOP); // for filled stars
        starsUser.getDrawable(1).setColorFilter(getResources().getColor(R.color.gold),PorterDuff.Mode.SRC_ATOP); // for half filled stars
        starsUser.getDrawable(0).setColorFilter(getResources().getColor(R.color.cinza),PorterDuff.Mode.SRC_ATOP); // for empty stars

        ratingBarUser.setMax(5);
        ratingBarUser.setRating(5);
        ratingBarUser.setIsIndicator(true);

        starsJogo = (LayerDrawable) ratingBarJogo.getProgressDrawable();
        starsJogo.getDrawable(2).setColorFilter(getResources().getColor(R.color.amarelo),PorterDuff.Mode.SRC_ATOP); // for filled stars
        starsJogo.getDrawable(1).setColorFilter(getResources().getColor(R.color.gold),PorterDuff.Mode.SRC_ATOP); // for half filled stars
        starsJogo.getDrawable(0).setColorFilter(getResources().getColor(R.color.cinza),PorterDuff.Mode.SRC_ATOP); // for empty stars

        ratingBarJogo.setMax(5);
        ratingBarJogo.setIsIndicator(true);


        if (getIntent().hasExtra("JOGOUSER")) {
            modelJodoDoUsuario = (Jogo) getIntent().getSerializableExtra("JOGOUSER");

            if(modelJodoDoUsuario.getNome().equals("") || modelJodoDoUsuario.getNome().isEmpty()){

            }else{
                txtTituloJogoDoUsuario.setText(modelJodoDoUsuario.getNome());
            }

            if(modelJodoDoUsuario.getDescricao().equals("") || modelJodoDoUsuario.getDescricao().isEmpty()){

            }else{
                descricaoDoJogoDoUsuario.setText(modelJodoDoUsuario.getDescricao());
            }

            if(modelJodoDoUsuario.getRating().equals("") || modelJodoDoUsuario.getRating().isEmpty()){
                ratingBarJogo.setRating(4);
            }else{
                ratingBarJogo.setRating(Float.parseFloat(modelJodoDoUsuario.getRating()));
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

        viewPager.setAdapter(adapter);

        //Custom bind indicator
        indicatorView.setCount(viewPager.getIndicatorCount());
        viewPager.setIndicatorPageChangeListener(new LoopingViewPager.IndicatorPageChangeListener() {
            @Override
            public void onIndicatorProgress(int selectingPosition, float progress) {
                indicatorView.setProgress(selectingPosition, progress);
            }

            @Override
            public void onIndicatorPageChange(int newIndicatorPosition) {
//                indicatorView.setSelection(newIndicatorPosition);
            }
        });
    }






    @Override
    protected void onResume() {
        super.onResume();
        viewPager.resumeAutoScroll();
    }

    @Override
    protected void onPause() {
        viewPager.pauseAutoScroll();
        super.onPause();
    }

    private ArrayList<Integer> createDummyItems () {
        ArrayList<Integer> items = new ArrayList<>();
        items.add(0, 1);
        items.add(1, 2);
        items.add(2, 3);
        items.add(3, 4);
        items.add(4, 0);
        //items.add(5, 0);
        //items.add(6, 0);
        return items;
    }


}
