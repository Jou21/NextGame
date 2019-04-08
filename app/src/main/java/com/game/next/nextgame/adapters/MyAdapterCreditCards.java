package com.game.next.nextgame.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.next.nextgame.PagamentoActivity;
import com.game.next.nextgame.R;
import com.game.next.nextgame.entidades.CartaoUser;
import com.game.next.nextgame.entidades.CreditCard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MyAdapterCreditCards extends RecyclerView.Adapter<MyAdapterCreditCards.ViewHolder> {

    private ArrayList<View> values;
    private DatabaseReference referenceCartaoUser;
    private FirebaseUser user;

    private int entrou = -1;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout cardContainer;
        public View layout;
        public RelativeLayout viewBackground, viewForeground;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            cardContainer = (LinearLayout) v.findViewById(R.id.card_container);
            viewBackground = v.findViewById(R.id.view_background);
            viewForeground = v.findViewById(R.id.view_foreground);
        }
    }

    public void remove(final int position, final PagamentoActivity pagamentoActivity) {

        user = FirebaseAuth.getInstance().getCurrentUser();
        referenceCartaoUser = FirebaseDatabase.getInstance().getReference("UsersCards").child(user.getUid());

        if(!values.isEmpty()) {
            if (values.get(position) != null) {
                final String creditCardTemp = ((CreditCard) values.get(position)).getCardNumber();

                referenceCartaoUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        values.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            CreditCard sampleCreditCardView = new CreditCard(pagamentoActivity);
                            CartaoUser userCard = snapshot.getValue(CartaoUser.class);
                            if (userCard != null) {
                                if (userCard.getCardNumber() == creditCardTemp) {
                                    snapshot.getRef().removeValue();

                                } else {
                                    sampleCreditCardView.setUserId(user.getUid());
                                    sampleCreditCardView.setCVV(userCard.getCvv());
                                    sampleCreditCardView.setCardHolderName(userCard.getName());
                                    sampleCreditCardView.setCardExpiry(userCard.getExpiry());
                                    sampleCreditCardView.setCardNumber(userCard.getCardNumber());
                                    values.add(sampleCreditCardView);
                                }
                            }
                        }
                        notifyDataSetChanged();
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

        //values.remove(position);
        //notifyDataSetChanged();
        //notifyItemRemoved(position);

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapterCreditCards(ArrayList<View> myDataset) {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapterCreditCards.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_credit_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.cardContainer.addView(values.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }
}
