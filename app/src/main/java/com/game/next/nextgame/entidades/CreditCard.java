package com.game.next.nextgame.entidades;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cooltechworks.creditcarddesign.CardSelector;
import com.cooltechworks.creditcarddesign.CreditCardUtils;
import com.cooltechworks.creditcarddesign.FlipAnimator;
import com.cooltechworks.creditcarddesign.R.id;
import com.cooltechworks.creditcarddesign.R.styleable;
import com.game.next.nextgame.interfaces.ICustomCardSelector;
import com.cooltechworks.creditcarddesign.R.drawable;
import com.cooltechworks.creditcarddesign.R.layout;


import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;


public class CreditCard extends FrameLayout {
    private static final int TEXTVIEW_CARD_HOLDER_ID;
    private static final int TEXTVIEW_CARD_EXPIRY_ID;
    private static final int TEXTVIEW_CARD_NUMBER_ID;
    private static final int TEXTVIEW_CARD_CVV_ID;
    private static final int FRONT_CARD_ID;
    private static final int BACK_CARD_ID;
    private static final int FRONT_CARD_OUTLINE_ID;
    private static final int BACK_CARD_OUTLINE_ID;
    private int mCurrentDrawable;
    private String mRawCardNumber;
    private ICustomCardSelector mSelectorLogic;
    private String mCardHolderName;
    private String mCVV;
    private String mExpiry;
    private String mMes;
    private String mAno;
    private String token;
    private String erro;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public CreditCard(Context context) {
        super(context);
        this.init();
    }

    public CreditCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs);
    }

    public CreditCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs);
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String... erros) {
        for(String e : erros){
            if(e.equalsIgnoreCase("card_number")){
                this.erro += "Número do cartão inválido!";
            }

        }
        this.erro = erro;
        Log.d("ERRO","error" + erro);
    }

    public String getToken() {
        return token;
    }


    public void setToken(String token) {
        this.token = token;
        Log.d("TOKEN","token: " + token);
    }


    public String getmMes() {
        mMes = getExpiry().substring(0,2);
        return mMes;
    }

    public String getmAno() {
        mAno = getExpiry().substring(3,5);
        return mAno;
    }


    public String getCardHolderName() {
        return this.mCardHolderName;
    }


    public String getCVV() {
        return this.mCVV;
    }


    public String getExpiry() {
        return this.mExpiry;
    }

    private void init() {
        this.mCurrentDrawable = drawable.card_color_round_rect_default;
        this.mRawCardNumber = "";
        LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(String.valueOf("layout_inflater"));
        inflater.inflate(layout.view_creditcard, this, true);
    }

    private void init(AttributeSet attrs) {
        this.init();
        TypedArray a = this.getContext().obtainStyledAttributes(attrs, styleable.creditcard, 0, 0);
        String cardHolderName = a.getString(styleable.creditcard_card_holder_name);
        String expiry = a.getString(styleable.creditcard_card_expiration);
        String cardNumber = a.getString(styleable.creditcard_card_number);
        int cvv = a.getInt(styleable.creditcard_cvv, 0);
        int cardSide = a.getInt(styleable.creditcard_card_side, 1);
        this.setCardNumber(cardNumber);
        this.setCVV(cvv);
        this.setCardExpiry(expiry);
        this.setCardHolderName(cardHolderName);
        if (cardSide == 0) {
            this.showBackImmediate();
        }

        this.paintCard();
        a.recycle();
    }

    private void flip(boolean ltr, boolean isImmediate) {
        View layoutContainer = this.findViewById(id.card_outline_container);
        View frontView = this.findViewById(FRONT_CARD_OUTLINE_ID);
        View backView = this.findViewById(BACK_CARD_OUTLINE_ID);
        View frontContentView = this.findViewById(FRONT_CARD_ID);
        View backContentView = this.findViewById(BACK_CARD_ID);
        View layoutContentContainer = this.findViewById(id.card_container);
        if (isImmediate) {
            frontContentView.setVisibility(ltr ? View.GONE : View.VISIBLE);
            backContentView.setVisibility(ltr ? View.VISIBLE : View.GONE);
        } else {
            int duration = 600;
            FlipAnimator flipAnimator = new FlipAnimator(frontView, backView, frontView.getWidth() / 2, backView.getHeight() / 2);
            flipAnimator.setInterpolator(new OvershootInterpolator(0.5F));
            flipAnimator.setDuration((long)duration);
            if (ltr) {
                flipAnimator.reverse();
            }

            flipAnimator.setTranslateDirection(3);
            flipAnimator.setRotationDirection(2);
            layoutContainer.startAnimation(flipAnimator);
            FlipAnimator flipAnimator1 = new FlipAnimator(frontContentView, backContentView, frontContentView.getWidth() / 2, backContentView.getHeight() / 2);
            flipAnimator1.setInterpolator(new OvershootInterpolator(0.5F));
            flipAnimator1.setDuration((long)duration);
            if (ltr) {
                flipAnimator1.reverse();
            }

            flipAnimator1.setTranslateDirection(3);
            flipAnimator1.setRotationDirection(2);
            layoutContentContainer.startAnimation(flipAnimator1);
        }

    }

    public void setCardNumber(String rawCardNumber) {
        this.mRawCardNumber = rawCardNumber == null ? "" : rawCardNumber;
        String newCardNumber = this.mRawCardNumber;

        for(int i = this.mRawCardNumber.length(); i < 16; ++i) {
            newCardNumber = newCardNumber + 'X';
        }

        String cardNumber = CreditCardUtils.handleCardNumber(newCardNumber, "  ");
        ((TextView)this.findViewById(TEXTVIEW_CARD_NUMBER_ID)).setText(cardNumber);
        if (this.mRawCardNumber.length() == 3) {
            this.revealCardAnimation();
        } else {
            this.paintCard();
        }

    }

    public void setCVV(int cvvInt) {
        if (cvvInt == 0) {
            this.setCVV("");
        } else {
            String cvv = String.valueOf(cvvInt);
            this.setCVV(cvv);
        }

    }

    public void showFront() {
        this.flip(true, false);
    }

    public void showFrontImmediate() {
        this.flip(true, true);
    }

    public void showBack() {
        this.flip(false, false);
    }

    public void showBackImmediate() {
        this.flip(false, true);
    }

    public void setCVV(String cvv) {
        if (cvv == null) {
            cvv = "";
        }

        this.mCVV = cvv;
        ((TextView)this.findViewById(TEXTVIEW_CARD_CVV_ID)).setText(cvv);
    }

    public void setCardExpiry(String dateYear) {
        dateYear = dateYear == null ? "" : CreditCardUtils.handleExpiration(dateYear);
        this.mExpiry = dateYear;
        ((TextView)this.findViewById(TEXTVIEW_CARD_EXPIRY_ID)).setText(dateYear);
    }

    public void setCardHolderName(String cardHolderName) {
        cardHolderName = cardHolderName == null ? "" : cardHolderName;
        if (cardHolderName.length() > 16) {
            cardHolderName = cardHolderName.substring(0, 16);
        }

        this.mCardHolderName = cardHolderName;
        ((TextView)this.findViewById(TEXTVIEW_CARD_HOLDER_ID)).setText(cardHolderName);
    }

    public void paintCard() {
        CardSelector card = this.selectCard();
        View cardContainer = this.findViewById(id.card_outline_container);
        View chipContainer = this.findViewById(id.chip_container);
        View chipInner = this.findViewById(id.chip_inner_view);
        View cardBack = this.findViewById(BACK_CARD_OUTLINE_ID);
        View cardFront = this.findViewById(FRONT_CARD_OUTLINE_ID);
        chipContainer.setBackgroundResource(card.getResChipOuterId());
        chipInner.setBackgroundResource(card.getResChipInnerId());
        ImageView frontLogoImageView = (ImageView)cardContainer.findViewById(id.logo_img);
        frontLogoImageView.setImageResource(card.getResLogoId());
        ImageView centerImageView = (ImageView)cardContainer.findViewById(id.logo_center_img);
        centerImageView.setImageResource(card.getResCenterImageId());
        ImageView backLogoImageView = (ImageView)this.findViewById(BACK_CARD_ID).findViewById(id.logo_img);
        backLogoImageView.setImageResource(card.getResLogoId());
        cardBack.setBackgroundResource(card.getResCardId());
        cardFront.setBackgroundResource(card.getResCardId());
    }

    public void revealCardAnimation() {
        CardSelector card = this.selectCard();
        View cardFront = this.findViewById(FRONT_CARD_OUTLINE_ID);
        View cardContainer = this.findViewById(id.card_outline_container);
        this.paintCard();
        this.animateChange(cardContainer, cardFront, card.getResCardId());
    }

    public CardSelector selectCard() {
        return this.mSelectorLogic != null ? this.mSelectorLogic.getCardSelector(this.mRawCardNumber) : CardSelector.selectCard(this.mRawCardNumber);
    }

    public void animateChange(View cardContainer, View v, int drawableId) {
        this.showAnimation(cardContainer, v, drawableId);
    }

    public void showAnimation(final View cardContainer, View v, final int drawableId) {
        v.setBackgroundResource(drawableId);
        if (this.mCurrentDrawable != drawableId) {
            int duration = 1000;
            int cx = v.getLeft();
            int cy = v.getTop();
            int radius = Math.max(v.getWidth(), v.getHeight()) * 4;
            if (VERSION.SDK_INT < 21) {
                SupportAnimator animator = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0.0F, (float)radius);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(duration);
                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        cardContainer.setBackgroundResource(drawableId);
                    }
                }, (long)duration);
                v.setVisibility(View.GONE);
                animator.start();
                this.mCurrentDrawable = drawableId;
            } else {
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(v, cx, cy, 0.0F, (float)radius);
                v.setVisibility(View.GONE);
                anim.setDuration((long)duration);
                anim.start();
                anim.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        cardContainer.setBackgroundResource(drawableId);
                    }
                });
                this.mCurrentDrawable = drawableId;
            }

        }
    }

    public void setSelectorLogic(ICustomCardSelector mSelectorLogic) {
        this.mSelectorLogic = mSelectorLogic;
    }


    public String getCardNumber() {
        return this.mRawCardNumber;
    }

    static {
        TEXTVIEW_CARD_HOLDER_ID = id.front_card_holder_name;
        TEXTVIEW_CARD_EXPIRY_ID = id.front_card_expiry;
        TEXTVIEW_CARD_NUMBER_ID = id.front_card_number;
        TEXTVIEW_CARD_CVV_ID = id.back_card_cvv;
        FRONT_CARD_ID = id.front_card_container;
        BACK_CARD_ID = id.back_card_container;
        FRONT_CARD_OUTLINE_ID = id.front_card_outline;
        BACK_CARD_OUTLINE_ID = id.back_card_outline;
    }


}
