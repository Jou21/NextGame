package com.game.next.nextgame.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.game.next.nextgame.ActivityCarteira;
import com.game.next.nextgame.ActivityChat;
import com.game.next.nextgame.ActivityIdentificaJogo;
import com.game.next.nextgame.ActivityMapa;
import com.game.next.nextgame.ActivityMeusJogos;
import com.game.next.nextgame.MainActivity;
import com.game.next.nextgame.R;
import com.game.next.nextgame.adapters.MyAdapterListJogos;
import com.game.next.nextgame.adapters.MyAdapterOfRecyclers;
import com.game.next.nextgame.entidades.Jogo;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FragmentA extends Fragment {

    private ProgressBar mProgressBar;

    private FloatingActionButton fab;
    private FloatingActionButton fab2;
    private ConstraintLayout layoutFragmentXbox;
    private LinearLayout layoutButtons;
    private LinearLayout layoutContent;
    private LinearLayout layoutBranco;
    private boolean isOpen = false;
    private float posicaoXInicialPesquisar;
    private float posicaoYInicialPesquisar;
    private RecyclerView recyclerViewOfRecyclers;
    private RecyclerView.Adapter mAdapterOfRecyclers;
    private RecyclerView.LayoutManager layoutManagerOfRecyclers;

    private AutoCompleteTextView autoCompletePesquisar;

    private Button btnMyGames, btnChat, btnCarteira, btnMapa;

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_a, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        exibirProgress(true);

        layoutFragmentXbox = (ConstraintLayout) view.findViewById(R.id.layoutFragmentXbox);
        layoutButtons = (LinearLayout) view.findViewById(R.id.layoutButtons);
        layoutContent = (LinearLayout) view.findViewById(R.id.layoutContent);
        layoutBranco = (LinearLayout) view.findViewById(R.id.layoutBranco);

        recyclerViewOfRecyclers = (RecyclerView) view.findViewById(R.id.my_recycler_view_of_recyclers);
        recyclerViewOfRecyclers.setHasFixedSize(true);
        layoutManagerOfRecyclers = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewOfRecyclers.setLayoutManager(layoutManagerOfRecyclers);
        mAdapterOfRecyclers = new MyAdapterOfRecyclers(this);
        recyclerViewOfRecyclers.setAdapter(mAdapterOfRecyclers);

        fab = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSearch();
            }
        });

        fab2 = (FloatingActionButton) view.findViewById(R.id.floatingActionButton2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSearch();
            }
        });

        btnMyGames = (Button) view.findViewById(R.id.my_games_xbox);
        btnChat = (Button) view.findViewById(R.id.chat_xbox);
        btnCarteira = (Button) view.findViewById(R.id.carteira_xbox);
        btnMapa = (Button) view.findViewById(R.id.mapa_xbox);

        autoCompletePesquisar = (AutoCompleteTextView) view.findViewById(R.id.pesquisarAutoCompleteFragmentA);

        btnMyGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaMeusJogos = new Intent(view.getContext(), ActivityMeusJogos.class);
                startActivity(telaMeusJogos);
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaMeusJogos = new Intent(view.getContext(), ActivityChat.class);
                startActivity(telaMeusJogos);
            }
        });

        btnCarteira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaMeusJogos = new Intent(view.getContext(), ActivityCarteira.class);
                startActivity(telaMeusJogos);
            }
        });

        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaMeusJogos = new Intent(view.getContext(), ActivityMapa.class);
                startActivity(telaMeusJogos);
            }
        });

        return view;
    }

    public RecyclerView.Adapter getMAdapterOfRecyclers() {
        return mAdapterOfRecyclers;
    }

    public void exibirProgress(boolean exibir) {
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }


    private void openSearch() {

        if (!isOpen) {

            autoCompletePesquisar.setText("");

            posicaoXInicialPesquisar = fab.getX();
            posicaoYInicialPesquisar = fab.getY();

            int x = layoutContent.getRight();
            int y = layoutButtons.getMeasuredHeight() / 2;

            int startRadius = 0;
            int endRadius = (int) Math.hypot(layoutFragmentXbox.getWidth(), layoutFragmentXbox.getHeight());

            fab.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(), android.R.color.white, null)));
            fab.setImageResource(R.drawable.ic_close_grey);

            Animator anim = ViewAnimationUtils.createCircularReveal(layoutButtons, x, y, startRadius, endRadius);

            layoutBranco.setVisibility(View.VISIBLE);

            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    layoutButtons.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            //=========================================

            ObjectAnimator animatorX = ObjectAnimator.ofFloat(fab2, "x", (layoutContent.getWidth()-236.0f));//move pra direita
            ObjectAnimator animatorX2 = ObjectAnimator.ofFloat(fab, "x", (layoutContent.getWidth()-1070.0f));//move pra direita
            ObjectAnimator animatorX3 = ObjectAnimator.ofFloat(fab2, "x", (layoutContent.getWidth()-1070.0f));//move pra direita
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(fab,"y",80.0f);//move pra cima
            ObjectAnimator animatorY2 = ObjectAnimator.ofFloat(fab2,"y",80.0f);//move pra cima
            ObjectAnimator animatorRotate = ObjectAnimator.ofFloat(fab2, "rotation", 0.0f, 360.0f);
            ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(layoutBranco,View.ALPHA,0.0f,1.0f);//desaparece
            anim.setDuration(210);
            animatorAlpha.setDuration(1000);
            animatorRotate.setDuration(125);
            animatorX.setDuration(125);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(animatorY).with(animatorY2).with(animatorX2).with(animatorX3).before(animatorX).with(animatorRotate).with(animatorAlpha).before(anim);
            animatorSet.start();

            //=========================================

            isOpen = true;

        }
    }
    private void closeSearch() {
        if (isOpen) {

            ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    autoCompletePesquisar.getWindowToken(), 0);

            int x = layoutButtons.getLeft();
            int y = layoutButtons.getMeasuredHeight() / 2;

            int startRadius = Math.max(layoutContent.getWidth(), layoutContent.getHeight());
            int endRadius = 0;

            Animator anim = ViewAnimationUtils.createCircularReveal(layoutButtons, x, y, startRadius, endRadius);

            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    //fab3.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    fab2.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(),R.color.colorAccent,null)));
                    layoutButtons.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            //=========================================

            ObjectAnimator animatorX = ObjectAnimator.ofFloat(fab2,"x",posicaoXInicialPesquisar);//move pra direita
            ObjectAnimator animatorX2 = ObjectAnimator.ofFloat(fab,"x",posicaoXInicialPesquisar);//move pra direita
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(fab,"y",posicaoYInicialPesquisar);//move pra baixo
            ObjectAnimator animatorY2 = ObjectAnimator.ofFloat(fab2,"y",posicaoYInicialPesquisar);//move pra cima
            ObjectAnimator animatorRotate = ObjectAnimator.ofFloat(fab2,"rotation",360.0f,0.0f);
            ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(layoutBranco,View.ALPHA,1.0f,0.0f);//desaparece
            animatorAlpha.setDuration(1000);
            animatorRotate.setDuration(390);
            animatorX.setDuration(390);
            AnimatorSet animatorSet = new AnimatorSet();
            //animatorSet.play(animatorX).with(animatorRotate).with(animatorAlpha).before(animatorY).with(animatorY2).before(animatorX2);
            animatorSet.play(animatorX).with(animatorRotate).with(animatorAlpha).before(animatorY).with(animatorY2).with(animatorX2);
            animatorSet.start();

            //=========================================
            anim.setDuration(390);
            anim.start();

            isOpen = false;
        }
    }



}
