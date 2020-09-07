package com.okunev.tinkoffexam.viewholders;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.okunev.tinkoffexam.databinding.ItemPicsBinding;
import com.okunev.tinkoffexam.models.GifReference;
import com.okunev.tinkoffexam.models.LoadingStates;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class GifViewHolder extends RecyclerView.ViewHolder {

    private final ItemPicsBinding binding;

    public GifViewHolder(ItemPicsBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(GifReference gifReference) {
        loadGif(gifReference.getGifURL());
        binding.falseLayout.retry.setOnClickListener(v -> loadGif(gifReference.getGifURL()));
        binding.descriptionSection.setVisibility(gifReference.getDescription().isEmpty() ? View.GONE : View.VISIBLE);
        binding.descriptionInSection.setText(gifReference.getDescription());
    }

    private void loadGif(String url) {
        handleLoadingState(LoadingStates.STATE_LOADING);
        Glide.with(binding.gifView.getContext())
                .asGif()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        handleLoadingState(LoadingStates.STATE_ERROR);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        handleLoadingState(LoadingStates.STATE_LOADED);
                        return false;
                    }
                })
                .transition(withCrossFade())
                .into(binding.gifView);
    }

    private void handleLoadingState(LoadingStates loadingStates) {
        binding.loadingIndicator.setVisibility(loadingStates == LoadingStates.STATE_LOADING ? View.VISIBLE : View.GONE);
        binding.falseLayout.getRoot().setVisibility(loadingStates == LoadingStates.STATE_ERROR ? View.VISIBLE : View.GONE);
    }
}