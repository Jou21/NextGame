package com.game.next.nextgame.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asksira.loopingviewpager.LoopingPagerAdapter;
import com.game.next.nextgame.R;
import com.game.next.nextgame.entidades.Jogo;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.game.next.nextgame.JogoDoUsuarioActivity.modelJodoDoUsuario;

public class MyViewPagerAdapter extends LoopingPagerAdapter<Integer> {

    private static final int VIEW_TYPE_NORMAL = 100;
    private static final int VIEW_TYPE_SPECIAL = 101;

    private Jogo jogo;



    public MyViewPagerAdapter(Context context, ArrayList<Integer> itemList, boolean isInfinite) {
        super(context, itemList, isInfinite);
    }

    @Override
    protected int getItemViewType(int listPosition) {
        //if (itemList.get(listPosition) == 0){
        //    return VIEW_TYPE_SPECIAL;
        //}



        return VIEW_TYPE_NORMAL;
    }

    @Override
    protected View inflateView(int viewType, ViewGroup container, int listPosition) {
        //if (viewType == VIEW_TYPE_SPECIAL){
        //    return LayoutInflater.from(context).inflate(R.layout.item_special, container, false);
        //}

        return LayoutInflater.from(context).inflate(R.layout.item_pager, container, false);
    }

    @Override
    protected void bindView(View convertView, int listPosition, int viewType) {
        //if (viewType == VIEW_TYPE_SPECIAL){
        // return;
        // }
        ImageView imagem = (ImageView) convertView.findViewById(R.id.images_of_game);
        if(imagem.equals("") || imagem.equals(null)){

        }else {

            imagem.setBackgroundColor(context.getResources().getColor(getBackgroundColor(listPosition)));



            if(listPosition == 0){
                if(modelJodoDoUsuario.getUrlImgJogo() != null) {

                    String urlImgJogo = "";
                    if(modelJodoDoUsuario.getUrlImgJogo().contains("https:")){
                        urlImgJogo = modelJodoDoUsuario.getUrlImgJogo();
                    }else{
                        urlImgJogo = "https:" + modelJodoDoUsuario.getUrlImgJogo();
                    }

                    Picasso.get().load(urlImgJogo).into(imagem, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }
            }



            if(listPosition == 1){
                if(modelJodoDoUsuario.getUrlImgComplementar1() != null) {

                    String urlImgComplementar1 = "";
                    if(modelJodoDoUsuario.getUrlImgComplementar1().contains("https:")){
                        urlImgComplementar1 = modelJodoDoUsuario.getUrlImgComplementar1();
                    }else{
                        urlImgComplementar1 = "https:" + modelJodoDoUsuario.getUrlImgComplementar1();
                    }

                    Picasso.get().load(urlImgComplementar1).into(imagem, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }
            }



            if(listPosition == 2){
                if(modelJodoDoUsuario.getUrlImgComplementar2() != null) {

                    String urlImgComplementar2 = "";
                    if(modelJodoDoUsuario.getUrlImgComplementar2().contains("https:")){
                        urlImgComplementar2 = modelJodoDoUsuario.getUrlImgComplementar2();
                    }else{
                        urlImgComplementar2 = "https:" + modelJodoDoUsuario.getUrlImgComplementar2();
                    }

                    Picasso.get().load(urlImgComplementar2).into(imagem, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }
            }



            if(listPosition == 3){
                if(modelJodoDoUsuario.getUrlImgComplementar3() != null) {

                    String urlImgComplementar3 = "";
                    if(modelJodoDoUsuario.getUrlImgComplementar3().contains("https:")){
                        urlImgComplementar3 = modelJodoDoUsuario.getUrlImgComplementar3();
                    }else{
                        urlImgComplementar3 = "https:" + modelJodoDoUsuario.getUrlImgComplementar3();
                    }

                    Picasso.get().load(urlImgComplementar3).into(imagem, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }
            }



            if(listPosition == 4){
                if(modelJodoDoUsuario.getUrlImgComplementar4() != null) {

                    String urlImgComplementar4 = "";
                    if(modelJodoDoUsuario.getUrlImgComplementar4().contains("https:")){
                        urlImgComplementar4 = modelJodoDoUsuario.getUrlImgComplementar4();
                    }else{
                        urlImgComplementar4 = "https:" + modelJodoDoUsuario.getUrlImgComplementar4();
                    }

                    Picasso.get().load(urlImgComplementar4).into(imagem, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }
            }



            if(listPosition == 5){
                if(modelJodoDoUsuario.getUrlImgComplementar5() != null) {

                    String urlImgComplementar5 = "";
                    if(modelJodoDoUsuario.getUrlImgComplementar5().contains("https:")){
                        urlImgComplementar5 = modelJodoDoUsuario.getUrlImgComplementar5();
                    }else{
                        urlImgComplementar5 = "https:" + modelJodoDoUsuario.getUrlImgComplementar5();
                    }

                    Picasso.get().load(urlImgComplementar5).into(imagem, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }
            }

        }

    }

    private int getBackgroundColor (int number) {
        switch (number) {
            case 0:
                //return android.R.color.holo_red_light;
                return android.R.color.white;
            case 1:
                //return android.R.color.holo_orange_light;
                return android.R.color.white;
            case 2:
                //return android.R.color.holo_green_light;
                return android.R.color.white;
            case 3:
                //return android.R.color.holo_blue_light;
                return android.R.color.white;
            case 4:
                //return android.R.color.holo_purple;
                return android.R.color.white;
            case 5:
                //return android.R.color.black;
                return android.R.color.white;
            default:
                //return android.R.color.black;
                return android.R.color.white;
        }
    }


}
