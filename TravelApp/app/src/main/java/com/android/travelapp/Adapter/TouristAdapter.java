package com.android.travelapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.travelapp.Listener.TouristListener;
import com.android.travelapp.R;
import com.android.travelapp.Ui.TourDetail;
import com.android.travelapp.Model.TouristModel;

import java.util.List;

public class TouristAdapter extends RecyclerView.Adapter<TouristAdapter.ViewHolder> {
    private final TouristListener touristListener;
    private List<TouristModel> touristModelList;
    public TouristAdapter(List<TouristModel> touristModelList, TouristListener touristListener){
        this.touristModelList = touristModelList;
        this.touristListener = touristListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final TouristModel touristModel = touristModelList.get(position);

        holder.nameTour.setText(touristModel.getAl_name_tour());
        holder.priceTour.setText(Integer.toString(touristModel.getAl_price_tour()));

        //chuyen byte -> bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(touristModel.getAl_img_tour(),0, touristModel.getAl_img_tour().length);
        holder.imgTour.setImageBitmap(bitmap);

        holder.linearLayout.setOnClickListener(view -> touristListener.onItemClicked(touristModel));
    }

    @Override
    public int getItemCount() {
        return touristModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgTour;
        TextView nameTour, priceTour, locTour;
        LinearLayout linearLayout;
        private RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);;
            imgTour = itemView.findViewById(R.id.img_tour);
            nameTour = itemView.findViewById(R.id.name_tour);
            locTour = itemView.findViewById(R.id.btn_img_loc);
            priceTour = itemView.findViewById(R.id.price_tour);
            linearLayout = itemView.findViewById(R.id.linear_layout);
        }
    }
}
