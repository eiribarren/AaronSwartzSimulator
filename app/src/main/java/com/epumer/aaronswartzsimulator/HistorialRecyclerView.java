package com.epumer.aaronswartzsimulator;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

public class HistorialRecyclerView extends RecyclerView.Adapter<HistorialRecyclerView.HistorialViewHolder> {

    private List<Puntuacion> mDataset;

    public HistorialRecyclerView(List<Puntuacion> mDataset) {
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public HistorialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.historial_text_view, parent, false);
        HistorialViewHolder hvh = new HistorialViewHolder(v);
        return hvh;
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialViewHolder historialViewHolder, int position) {
        historialViewHolder.fecha.setText(mDataset.get(position).getFecha());
        historialViewHolder.puntuacion.setText(mDataset.get(position).getPuntuacionAsString());
        historialViewHolder.nombre.setText(mDataset.get(position).getNombre());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class HistorialViewHolder extends RecyclerView.ViewHolder {

        public TextView fecha, puntuacion, nombre;

        public HistorialViewHolder(View tv) {
            super(tv);
            this.fecha = tv.findViewById(R.id.fechaText);
            this.puntuacion = tv.findViewById(R.id.puntuacionText);
            this.nombre = tv.findViewById(R.id.nombreText);
        }
    }
}
