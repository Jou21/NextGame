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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.game.next.nextgame.ActivityChat;
import com.game.next.nextgame.ActivityIdentificaJogo;
import com.game.next.nextgame.ActivityMapa;
import com.game.next.nextgame.ActivityMeusJogos;
import com.game.next.nextgame.ActivityQuemTemOJogo;
import com.game.next.nextgame.CaptureActivityPortrait;
import com.game.next.nextgame.CustomScannerActivity;
import com.game.next.nextgame.R;
import com.game.next.nextgame.adapters.MyAdapterListJogos;
import com.game.next.nextgame.adapters.MyAdapterOfRecyclersB;
import com.game.next.nextgame.entidades.Jogo;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class FragmentB extends Fragment {

    private ProgressBar mProgressBar;

    private FloatingActionButton fabB;
    private FloatingActionButton fab2B;
    private ConstraintLayout layoutFragmentPS4;
    private LinearLayout layoutButtonsB;
    private LinearLayout layoutContentB;
    private LinearLayout layoutEscuroB;
    private LinearLayout linearLayoutB;
    private boolean isOpen = false;
    private float posicaoXInicialPesquisar;
    private float posicaoYInicialPesquisar;
    private RecyclerView recyclerViewOfRecyclers;
    private RecyclerView.Adapter mAdapterOfRecyclers;
    private RecyclerView.LayoutManager layoutManagerOfRecyclers;
    private MyAdapterListJogos adapter;

    private AutoCompleteTextView autoCompletePesquisar;

    private Button btnMyGames, btnChat, btnScan, btnMapa;

    private String contents;

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_b, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarB);
        exibirProgress(true);

        autoCompletePesquisar = (AutoCompleteTextView) view.findViewById(R.id.pesquisarAutoCompleteFragmentB);

        layoutFragmentPS4 = (ConstraintLayout) view.findViewById(R.id.layoutFragmentPS4);
        layoutButtonsB = (LinearLayout) view.findViewById(R.id.layoutButtonsB);
        layoutContentB = (LinearLayout) view.findViewById(R.id.layoutContentB);
        layoutEscuroB = (LinearLayout) view.findViewById(R.id.layoutEscuroB);
        linearLayoutB = (LinearLayout) view.findViewById(R.id.b_linearLayout);

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
        btnScan = (Button) view.findViewById(R.id.scan_ps4);
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

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setPrompt("Mantenha um palmo de distancia do código de barras");
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setOrientationLocked(true);
                integrator.setBeepEnabled(true);
                integrator.addExtra("QUALTELA","MAIN");
                integrator.setCaptureActivity(CustomScannerActivity.class);
                integrator.initiateScan();
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

            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    recyclerViewOfRecyclers.setVisibility(View.VISIBLE);
                    linearLayoutB.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    // ...
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    recyclerViewOfRecyclers.setVisibility(View.GONE);
                    linearLayoutB.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    // ...
                }
            });

            //=========================================

            isOpen = true;

        }
    }
    private void closeSearch() {
        if (isOpen) {

            ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    autoCompletePesquisar.getWindowToken(), 0);

            int x = layoutButtonsB.getRight();
            int y = layoutButtonsB.getMeasuredHeight() / 2;

            int startRadius = Math.max(layoutContentB.getWidth(), layoutContentB.getHeight());
            int endRadius = 0;

            Animator anim = ViewAnimationUtils.createCircularReveal(layoutButtonsB, x, y, startRadius, endRadius);

            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    recyclerViewOfRecyclers.setVisibility(View.VISIBLE);
                    linearLayoutB.setVisibility(View.VISIBLE);
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

    public void acionaDrop(ArrayList<Jogo> listTodosJogosPS4) {

        if(getActivity() != null) {
            adapter = new MyAdapterListJogos(getActivity(), listTodosJogosPS4);
        }
        autoCompletePesquisar.setAdapter(adapter);

        autoCompletePesquisar.setThreshold(1);//Começa a procurar do segundo caractere

        autoCompletePesquisar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final Jogo jogoSelecionado = (Jogo) parent.getItemAtPosition(position);

            Intent quemTemOJogo = new Intent(getActivity(), ActivityQuemTemOJogo.class);
            quemTemOJogo.putExtra("JOGO", jogoSelecionado);
            getActivity().startActivity(quemTemOJogo);

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
