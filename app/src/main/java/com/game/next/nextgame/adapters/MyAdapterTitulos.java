package com.game.next.nextgame.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.game.next.nextgame.R;

public class MyAdapterTitulos extends RecyclerView.Adapter<MyAdapterTitulos.ViewHolder> {

    private String corTexto;
    private String titulo;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTitulo;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtTitulo = (TextView) v.findViewById(R.id.txtTitulo);

            if (corTexto != null && !corTexto.equals("")) {
                txtTitulo.setTextColor(Color.parseColor(corTexto));
            }
        }
    }

    public MyAdapterTitulos(String titulo) {
        this.titulo = titulo;
    }

    public MyAdapterTitulos(String titulo, String corTexto) {
        this.titulo = titulo;
        this.corTexto = corTexto;
    }

    @Override
    public MyAdapterTitulos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_titulo_layout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.txtTitulo.setText(titulo);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

}