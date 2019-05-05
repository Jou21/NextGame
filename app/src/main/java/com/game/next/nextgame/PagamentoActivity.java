package com.game.next.nextgame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cooltechworks.creditcarddesign.CardEditActivity;
import com.cooltechworks.creditcarddesign.CreditCardUtils;
import com.game.next.nextgame.adapters.MyAdapterCreditCards;
import com.game.next.nextgame.adapters.RecyclerItemTouchHelper;
import com.game.next.nextgame.entidades.CartaoUser;
import com.game.next.nextgame.entidades.Carteira;
import com.game.next.nextgame.entidades.CreditCard;
import com.game.next.nextgame.entidades.TransacaoUser;
import com.game.next.nextgame.entidades.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.uol.pslibs.checkout_in_app.PSCheckout;
import br.com.uol.pslibs.checkout_in_app.transparent.listener.PSCheckoutListener;
import br.com.uol.pslibs.checkout_in_app.transparent.vo.PSCheckoutResponse;
import br.com.uol.pslibs.checkout_in_app.transparent.vo.PSTransparentDefaultRequest;
import br.com.uol.pslibs.checkout_in_app.wallet.util.PSCheckoutConfig;

public class PagamentoActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private final int CREATE_NEW_CARD = 0;

    private ArrayList<View> creditCardList;
    private Button btnAddCard;

    private DatabaseReference referenceCartaoUser, referenceTransacaoUser, referenceCarteiraUser;
    private FirebaseUser user;

    private boolean entrou = false;

    private String name = "";
    private String cvv = "";
    private String expiry = "";
    private String cardNumber = "";

    private String saldoParaAddCarteira;

    private RecyclerView recyclerView;
    private MyAdapterCreditCards mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento);

        entrou = false;

        user = FirebaseAuth.getInstance().getCurrentUser();
        referenceCartaoUser = FirebaseDatabase.getInstance().getReference("UsersCards").child(user.getUid());
        referenceTransacaoUser = FirebaseDatabase.getInstance().getReference("Transacoes").child(user.getUid());
        referenceCarteiraUser = FirebaseDatabase.getInstance().getReference("CarteiraUsers").child(user.getUid());

        if(getIntent().hasExtra("ADICIONARSALDO")){
            saldoParaAddCarteira = getIntent().getStringExtra("ADICIONARSALDO");

            Log.d("SALDO", "" + saldoParaAddCarteira);
        }else {
            Toast.makeText(PagamentoActivity.this, "Activity cannot find  extras " + "ADICIONARSALDO", Toast.LENGTH_SHORT).show();
            Log.d("EXTRASJOGO", "Activity cannot find  extras " + "ADICIONARSALDO");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setSystemBarTheme(PagamentoActivity.this,true,R.color.branco);
        }

        btnAddCard = (Button) findViewById(R.id.btn_add_card);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_credit_cards);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView);

        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PagamentoActivity.this, CardEditActivity.class);
                startActivityForResult(intent, CREATE_NEW_CARD);
            }
        });

        populate();
    }

    private void populate() {

        //Carregar cartões do usuário
        referenceCartaoUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                creditCardList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    CreditCard sampleCreditCardView = new CreditCard(PagamentoActivity.this);
                    CartaoUser userCard =  snapshot.getValue(CartaoUser.class);

                    if(userCard != null) {
                        name = userCard.getName();
                        cvv = userCard.getCvv();
                        expiry = userCard.getExpiry();
                        cardNumber = userCard.getCardNumber();

                        sampleCreditCardView.setUserId(user.getUid());
                        sampleCreditCardView.setCVV(cvv);
                        sampleCreditCardView.setCardHolderName(name);
                        sampleCreditCardView.setCardExpiry(expiry);
                        sampleCreditCardView.setCardNumber(cardNumber);

                        creditCardList.add(sampleCreditCardView);

                        mAdapter = new MyAdapterCreditCards(creditCardList);
                        recyclerView.setAdapter(mAdapter);

                        addCardListener(sampleCreditCardView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            String name = data.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME);
            String cardNumber = data.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER);
            String expiry = data.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY);
            String cvv = data.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV);

            if (reqCode == CREATE_NEW_CARD) {

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("userId", user.getUid());
                hashMap.put("name", name);
                hashMap.put("cardNumber", cardNumber);
                hashMap.put("expiry", expiry);
                hashMap.put("cvv", cvv);

                referenceCartaoUser.push().setValue(hashMap);

                populate();

            }
        }

    }

    private void addCardListener(CreditCard creditCardView) {
        creditCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreditCard creditCard = (CreditCard) v;
                pagarComCartao(creditCard);

            }
        });
    }

    private void pagarComCartao( CreditCard creditCard ){


        //TODO DESATIVAR QUANDO TIVER EM PRODUÇÃO

        referenceCarteiraUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && entrou == false){
                    Carteira userCarteira = dataSnapshot.getValue(Carteira.class);
                    String saldoTotal = String.valueOf(Double.parseDouble(userCarteira.getSaldo()) + Double.parseDouble(saldoParaAddCarteira));

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", user.getUid());
                    hashMap.put("saldo", saldoTotal);

                    referenceCarteiraUser.setValue(hashMap);

                    //Intent mainIntent = new Intent(PagamentoActivity.this, MainActivity.class);
                    //startActivity(mainIntent);
                    Toast.makeText(PagamentoActivity.this, "Parabéns, você adicionou saldo a sua carteira!", Toast.LENGTH_LONG).show();

                    entrou = true;
                    finish();
                }else if(!dataSnapshot.exists() && entrou == false){
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", user.getUid());
                    hashMap.put("saldo", saldoParaAddCarteira);

                    referenceCarteiraUser.setValue(hashMap);

                    //Intent mainIntent = new Intent(PagamentoActivity.this, MainActivity.class);
                    //startActivity(mainIntent);
                    Toast.makeText(PagamentoActivity.this, "Parabéns, você adicionou saldo a sua carteira!", Toast.LENGTH_LONG).show();
                    entrou = true;
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        //Inicialização a lib com parametros necessarios
        PSCheckoutConfig psCheckoutConfig = new PSCheckoutConfig();
        psCheckoutConfig.setSellerEmail("jvsb21@gmail.com");
        psCheckoutConfig.setSellerToken("4E3C345F7A124E149704B4BE2CC7DB01");

        //Inicializa apenas os recursos de pagamento transparente e boleto
        PSCheckout.initTransparent(PagamentoActivity.this, psCheckoutConfig);

        PSTransparentDefaultRequest psTransparentDefaultRequest = new PSTransparentDefaultRequest();

            psTransparentDefaultRequest
                    .setDocumentNumber("99404021040")
                    .setName(creditCard.getCardHolderName())
                    .setEmail(user.getEmail())
                    .setAreaCode("34")
                    .setPhoneNumber("999508523")
                    .setStreet("Rua Tapajos")
                    .setAddressComplement("")
                    .setAddressNumber("23")
                    .setDistrict("Saraiva")
                    .setCity("Uberlândia")
                    .setState("MG")
                    .setCountry("BRA")
                    .setPostalCode("38408414")
                    .setTotalValue(saldoParaAddCarteira)
                    .setAmount(saldoParaAddCarteira)
                    .setDescriptionPayment("Pagamento do teste de integração")
                    .setQuantity(1)
                    .setCreditCard(creditCard.getCardNumber())
                    .setCvv(creditCard.getCVV())
                    .setExpMonth(creditCard.getmMes())
                    .setExpYear(creditCard.getmAno())
                    .setBirthDate("04/05/1988");


        Log.d("DADOSCARTAO","NOME: " + creditCard.getCardHolderName() +
                "\nNUMEROCARTAO: " + creditCard.getCardNumber() +
                "\nCVV: " + creditCard.getCVV() +
                "\nMES: " + creditCard.getmMes() +
                "\nANO: " + creditCard.getmAno());


        PSCheckoutListener psCheckoutListener = new PSCheckoutListener() {
            @Override
            public void onSuccess(PSCheckoutResponse responseVO) {
                // responseVO.getCode() - Código da transação
                // responseVO.getStatus() - Status da transação
                // responseVO.getMessage() - Mensagem de retorno da transação(Sucesso/falha)
                Toast.makeText(PagamentoActivity.this, "Success: " + responseVO.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("CODE1","CODE: " + responseVO.getCode());
                Log.d("SUCESSO1","SUCESSO: " + responseVO.getMessage());
                Log.d("STATUS1","STATUS: " + responseVO.getStatus());

                //TODO ATIVAR QUANDO TIVER EM PRODUÇÃO
                /*
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("userId", user.getUid());
                hashMap.put("fornecedorId", fornecedorId);
                hashMap.put("valorAluguel", precoAluguelJogo);
                hashMap.put("valorCaucao", precoJogo);

                referenceTransacaoUser.push().setValue(hashMap);
                */
            }

            @Override
            public void onFailure(PSCheckoutResponse responseVO) {




                Toast.makeText(PagamentoActivity.this, "Fail: "+responseVO.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("ERRO1","ERRO: " + responseVO.getCode());

            }

            @Override
            public void onProcessing() {




                Toast.makeText(PagamentoActivity.this, "Processing...", Toast.LENGTH_LONG).show();
                Log.d("PROCESS1","PROCESS: " + "Processing");
            }
        };


        PSCheckout.payTransparentDefault(psTransparentDefaultRequest, psCheckoutListener, (AppCompatActivity) PagamentoActivity.this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PSCheckout.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, final int position) {
        if (viewHolder instanceof MyAdapterCreditCards.ViewHolder) {
            mAdapter.remove(viewHolder.getAdapterPosition(),PagamentoActivity.this);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static final void setSystemBarTheme(final Activity pActivity, final boolean textIsDark, int corStatusBar) {

        Window window = pActivity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(pActivity,corStatusBar));

        // Fetch the current flags.
        final int lFlags = window.getDecorView().getSystemUiVisibility();
        // Update the SystemUiVisibility dependening on whether we want a Light or Dark theme.
        pActivity.getWindow().getDecorView().setSystemUiVisibility(textIsDark ? (lFlags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) : (lFlags & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));
    }
}
