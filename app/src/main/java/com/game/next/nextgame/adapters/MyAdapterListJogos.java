package com.game.next.nextgame.adapters;


import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


import com.game.next.nextgame.R;
import com.game.next.nextgame.entidades.Jogo;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class MyAdapterListJogos extends ArrayAdapter<Jogo> {
    ArrayList<Jogo> customers, tempCustomer, suggestions;

    public MyAdapterListJogos(Context context, ArrayList<Jogo> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.customers = objects;
        this.tempCustomer = new ArrayList<Jogo>(objects);
        this.suggestions = new ArrayList<Jogo>(objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Jogo jogo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_jogo, parent, false);
        }
        TextView txtJogo = (TextView) convertView.findViewById(R.id.txt_jogo_adapterlistjogos);
        ImageView imgJogo = (ImageView) convertView.findViewById(R.id.img_jogo_adapterlistjogos);
        if (txtJogo != null)
            txtJogo.setText(jogo.getNome());
        if (imgJogo != null)
            Picasso.get().load(jogo.getUrlImgJogo()).into(imgJogo, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {

                }
            });
        // Now assign alternate color for rows
        if (position % 2 == 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                convertView.setBackgroundColor(getContext().getColor(R.color.odd));
            }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                convertView.setBackgroundColor(getContext().getColor(R.color.even));
            }

        return convertView;
    }


    @Override
    public Filter getFilter() {
        return myFilter;
    }

    Filter myFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            Jogo jogo = (Jogo) resultValue;
            return jogo.getNome();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Jogo jogo : tempCustomer) {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    if (jogo.getNome().toLowerCase().contains(filterPattern)) {
                        suggestions.add(jogo);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Jogo> jogos = (ArrayList<Jogo>) results.values;
            if (results != null && results.count > 0) {
                if (!jogos.isEmpty()) {
                    clear();
                    addAll(jogos);
                    notifyDataSetChanged();
                }
            }

            /*
            ArrayList<Jogo> jogos = (ArrayList<Jogo>) results.values;
            if (results != null && results.count > 0) {
                clear();
                if(!jogos.isEmpty()){
                    for (Jogo jogo : jogos) {
                        add(jogo);
                        notifyDataSetChanged();
                    }
                }
            }
            */
        }
    };

}
