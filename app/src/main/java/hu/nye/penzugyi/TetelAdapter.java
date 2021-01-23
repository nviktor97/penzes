package hu.nye.penzugyi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TetelAdapter extends RecyclerView.Adapter<TetelAdapter.TetelAdapterViewHolder>{
    private ArrayList<TetelItem> mTetelList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    void setOnItemClickListener(OnItemClickListener listener){ mListener = listener; }

    void setNewList(ArrayList<TetelItem> mTetelList) {
        this.mTetelList = mTetelList;
        notifyDataSetChanged();
    }

    public static class TetelAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView mNev;
        public TextView mDatum;
        public TextView mErtek;
        public TextView mTipus;
        public Button mTorles;

        public TetelAdapterViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mNev = itemView.findViewById(R.id.tetel_nev);
            mDatum = itemView.findViewById(R.id.tetel_datum);
            mErtek = itemView.findViewById(R.id.tetel_ertek);
            mTorles = itemView.findViewById(R.id.button_torol);

            mTorles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public TetelAdapter(ArrayList<TetelItem> TetelList){
        mTetelList = TetelList;
    }

    @Override
    public TetelAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tetelitem, parent, false);
        TetelAdapterViewHolder nav = new TetelAdapterViewHolder(v, mListener);
        return nav;
    }

    @Override
    public void onBindViewHolder(@NonNull TetelAdapterViewHolder holder, int position) {
        TetelItem currentItem = mTetelList.get(position);

        Log.d("SAJAT", currentItem.getmTipus());

        Context c = holder.itemView.getContext();

        if(currentItem.getmTipus().equals("KIADAS")){
            ((CardView) holder.itemView).setCardBackgroundColor(c.getResources().getColor(R.color.kiadas));
        }else{
            ((CardView) holder.itemView).setCardBackgroundColor(c.getResources().getColor(R.color.bevetel));
        }

        String[] datum = currentItem.getmDatum().split(" ");

        holder.mNev.setText(currentItem.getmNev());
        holder.mDatum.setText(datum[0].toString());
        holder.mErtek.setText(currentItem.getmErtek() + " Ft");
    }

    @Override
    public int getItemCount() {
        return mTetelList.size();
    }
}
