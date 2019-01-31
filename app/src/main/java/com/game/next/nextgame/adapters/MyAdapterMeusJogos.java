package com.game.next.nextgame.adapters;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.game.next.nextgame.R;

public class MyAdapterMeusJogos extends RecyclerView.Adapter<MyAdapterMeusJogos.ViewHolder> {
    private List<String> values;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtHeader;
        public TextView txtFooter;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = (TextView) v.findViewById(R.id.firstLine_meus_jogos);
            txtFooter = (TextView) v.findViewById(R.id.secondLine_meus_jogos);
        }
    }

    public MyAdapterMeusJogos(List<String> myDataset) {
        values = myDataset;
    }

    @Override
    public MyAdapterMeusJogos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
        View v = inflater.inflate(R.layout.row_meus_jogos, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final String name = values.get(position);
        holder.txtHeader.setText(name);
        holder.txtFooter.setText("Footer: " + name);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

}