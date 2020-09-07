package com.okunev.tinkoffexam.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.okunev.tinkoffexam.databinding.ItemPicsBinding;
import com.okunev.tinkoffexam.models.GifReference;
import com.okunev.tinkoffexam.viewholders.GifViewHolder;

import java.util.ArrayList;

public class GifPicsAdapter extends RecyclerView.Adapter<GifViewHolder> {
    private final ArrayList<GifReference> models;

    public GifPicsAdapter(ArrayList<GifReference> models) {
        this.models = models;
    }

    @NonNull
    @Override
    public GifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GifViewHolder(ItemPicsBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull GifViewHolder holder, int position) {
        GifReference gifReference = models.get(position);
        holder.bind(gifReference);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
