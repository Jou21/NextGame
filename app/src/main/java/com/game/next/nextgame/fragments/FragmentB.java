package com.game.next.nextgame.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.game.next.nextgame.ActivityCarteira;
import com.game.next.nextgame.ActivityChat;
import com.game.next.nextgame.ActivityMapa;
import com.game.next.nextgame.ActivityMeusJogos;
import com.game.next.nextgame.R;
import com.game.next.nextgame.adapters.MyAdapterOfRecyclersB;

public class FragmentB extends Fragment {

    private ProgressBar mProgressBar;

    private FloatingActionButton fabB;
    private FloatingActionButton fab2B;
    private ConstraintLayout layoutFragmentPS4;
    private LinearLayout layoutButtonsB;
    private LinearLayout layoutContentB;
    private LinearLayout layoutEscuroB;
    private boolean isOpen = false;
    private float posicaoXInicialPesquisar;
    private float posicaoYInicialPesquisar;
    private RecyclerView recyclerViewOfRecyclers;
    private RecyclerView.Adapter mAdapterOfRecyclers;
    private RecyclerView.LayoutManager layoutManagerOfRecyclers;

    private Button btnMyGames, btnChat, btnCarteira, btnMapa;

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_b, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarB);
        exibirProgress(true);

        layoutFragmentPS4 = (ConstraintLayout) view.findViewById(R.id.layoutFragmentPS4);
        layoutButtonsB = (LinearLayout) view.findViewById(R.id.layoutButtonsB);
        layoutContentB = (LinearLayout) view.findViewById(R.id.layoutContentB);
        layoutEscuroB = (LinearLayout) view.findViewById(R.id.layoutEscuroB);

        recyclerViewOfRecyclers = (RecyclerView) view.findViewById(R.id.b_my_recycler_view_of_recyclers);
        recyclerViewOfRecyclers.setHasFixedSize(true);
        layoutManagerOfRecyclers = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewOfRecyclers.setLayoutManager(layoutManagerOfRecyclers);
        mAdapterOfRecyclers = new MyAdapterOfRecyclersB(this);
        recyclerViewOfRecyclers.setAdapter(mAdapterOfRecyclers);

        fabB = (FloatingActionButton) view.findViewById(R.id.floatingActionButtonB);
        fabB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSearch();
            }
        });

        fab2B = (FloatingActionButton) view.findViewById(R.id.floatingActionButton2B);
        fab2B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSearch();
            }
        });

        btnMyGames = (Button) view.findViewById(R.id.my_games_ps4);
        btnChat = (Button) view.findViewById(R.id.chat_ps4);
        btnCarteira = (Button) view.findViewById(R.id.carteira_ps4);
        btnMapa = (Button) view.findViewById(R.id.mapa_ps4);

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

            posicaoXInicialPesquisar = fabB.getX();
            posicaoYInicialPesquisar = fabB.getY();

            int x = layoutContentB.getRight();
            int y = layoutButtonsB.getMeasuredHeight() / 2;

            int startRadius = 0;
            int endRadius = (int) Math.hypot(layoutFragmentPS4.getWidth(), layoutFragmentPS4.getHeight());

            fabB.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(), android.R.color.white, null)));
            fabB.setImageResource(R.drawable.ic_close_grey);

            Animator anim = ViewAnimationUtils.createCircularReveal(layoutButtonsB, x, y, startRadius, endRadius);


            layoutEscuroB.setVisibility(View.VISIBLE);

            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    layoutButtonsB.setVisibility(View.VISIBLE);
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

            ObjectAnimator animatorX = ObjectAnimator.ofFloat(fabB, "x", 10);//move pra direita
            ObjectAnimator animatorX2 = ObjectAnimator.ofFloat(fabB, "x", 840);//move pra direita
            ObjectAnimator animatorX3 = ObjectAnimator.ofFloat(fab2B, "x", 840);//move pra direita
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(fabB,"y",80.0f);//move pra cima
            ObjectAnimator animatorY2 = ObjectAnimator.ofFloat(fab2B,"y",80.0f);//move pra cima
            ObjectAnimator animatorRotate = ObjectAnimator.ofFloat(fabB, "rotation", 360.0f, 0.0f);
            ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(layoutEscuroB,View.ALPHA,0.0f,1.0f);//desaparece
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

            int x = layoutButtonsB.getRight();
            int y = layoutButtonsB.getMeasuredHeight() / 2;

            int startRadius = Math.max(layoutContentB.getWidth(), layoutContentB.getHeight());
            int endRadius = 0;

            Animator anim = ViewAnimationUtils.createCircularReveal(layoutButtonsB, x, y, startRadius, endRadius);

            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    layoutButtonsB.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            //=========================================

            ObjectAnimator animatorX = ObjectAnimator.ofFloat(fabB,"x",posicaoXInicialPesquisar);//move pra direita
            ObjectAnimator animatorX2 = ObjectAnimator.ofFloat(fab2B,"x",posicaoXInicialPesquisar);//move pra direita
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(fabB,"y",posicaoYInicialPesquisar);//move pra baixo
            ObjectAnimator animatorY2 = ObjectAnimator.ofFloat(fab2B,"y",posicaoYInicialPesquisar);//move pra cima
            ObjectAnimator animatorRotate = ObjectAnimator.ofFloat(fabB,"rotation",0.0f,360.0f);
            ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(layoutEscuroB,View.ALPHA,1.0f,0.0f);//desaparece
            animatorAlpha.setDuration(1000);
            animatorRotate.setDuration(390);
            animatorX.setDuration(390);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(animatorY).with(animatorX).with(animatorRotate).with(animatorAlpha).before(animatorY2).with(animatorX2);
            //animatorSet.play(animatorY).with(animatorX).with(animatorRotate).with(animatorAlpha).before(animatorY2).before(animatorX2);
            animatorSet.start();

            //=========================================
            anim.setDuration(390);
            anim.start();

            isOpen = false;
        }
    }
}
