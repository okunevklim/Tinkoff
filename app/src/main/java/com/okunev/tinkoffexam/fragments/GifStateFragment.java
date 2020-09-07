package com.okunev.tinkoffexam.fragments;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.okunev.tinkoffexam.adapters.GifPicsAdapter;
import com.okunev.tinkoffexam.databinding.FragmentGifViewBinding;
import com.okunev.tinkoffexam.interfaces.CardStackSimpleListener;
import com.okunev.tinkoffexam.models.GifReference;
import com.okunev.tinkoffexam.models.GifGetRes;
import com.okunev.tinkoffexam.models.GifType;
import com.okunev.tinkoffexam.network.Api;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.okunev.tinkoffexam.utils.Constants.ARGS_STACK_TYPE;

public class GifStateFragment extends Fragment implements CardStackSimpleListener {
    private com.okunev.tinkoffexam.databinding.FragmentGifViewBinding binding;
    private GifType stackType;
    private int pageNum;

    private CardStackLayoutManager layoutManager;
    private GifPicsAdapter adapter;
    private ArrayList<GifReference> gifReferences;

    public static GifStateFragment newInstance(GifType gifType) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_STACK_TYPE, gifType);
        GifStateFragment fragment = new GifStateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGifViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            stackType = (GifType) getArguments().getSerializable(ARGS_STACK_TYPE);
            pageNum = 0;

            gifReferences = new ArrayList<>();
            layoutManager = new CardStackLayoutManager(requireContext(), this);
            layoutManager.setDirections(Collections.singletonList(Direction.Right));
            RewindAnimationSetting rewindSetting = new RewindAnimationSetting.Builder().setDirection(Direction.Right).build();
            layoutManager.setRewindAnimationSetting(rewindSetting);
            layoutManager.setCanScrollVertical(false);
            binding.cardsView.setLayoutManager(layoutManager);
            adapter = new GifPicsAdapter(gifReferences);
            binding.cardsView.setAdapter(adapter);

            binding.next.setOnClickListener(v -> moveNext());
            binding.back.setOnClickListener(v -> rewind());
            binding.falseLayout.retry.setOnClickListener(v -> loadMoreGifs());

            checkRewindPossibility();
            loadMoreGifs();
        }
    }

    private void handleGifs(ArrayList<GifReference> loadedModels) {
        pageNum++;
        gifReferences.addAll(loadedModels);
        adapter.notifyItemRangeInserted(gifReferences.size() - loadedModels.size(), loadedModels.size());
        handleError(false);
        handleLoadingIndicator(false);
        binding.next.setVisibility(gifReferences.isEmpty() ? View.INVISIBLE : View.VISIBLE);
    }

    private void moveNext() {
        binding.cardsView.swipe();
    }

    private void rewind() {
        binding.cardsView.rewind();
    }

    private void loadMoreGifs() {
        handleError(false);
        handleLoadingIndicator(true);
        Api.getService().loadGifs(stackType.getUrlPart(), pageNum).enqueue(new Callback<GifGetRes>() {
            @Override
            public void onResponse(@NotNull Call<GifGetRes> call, @NotNull Response<GifGetRes> response) {
                GifGetRes gifGetRes = response.body();
                if (response.isSuccessful() && gifGetRes != null && gifGetRes.getResult() != null) {
                    requireActivity().runOnUiThread(() -> handleGifs(gifGetRes.getResult()));
                } else {
                    onFailure(call, new Throwable());
                }

            }

            @Override
            public void onFailure(@NotNull Call<GifGetRes> call, @NotNull Throwable t) {
                requireActivity().runOnUiThread(() -> handleError(true));
            }
        });
    }

    private void checkRewindPossibility() {
        TransitionManager.beginDelayedTransition(binding.getRoot());
        boolean shouldShowRewind = layoutManager.getTopPosition() != 0;
        binding.backImage.setAlpha(shouldShowRewind ? 1 : 0.5f);
        binding.back.setEnabled(shouldShowRewind);
    }

    public void handleError(boolean needToShow) {
        TransitionManager.beginDelayedTransition(binding.getRoot());
        binding.falseLayout.getRoot().setVisibility(needToShow ? View.VISIBLE : View.GONE);
        handleLoadingIndicator(false);
        binding.buttonsGroup.setVisibility(needToShow ? View.INVISIBLE : View.VISIBLE);
    }

    public void handleLoadingIndicator(boolean isLoading) {
        TransitionManager.beginDelayedTransition(binding.getRoot(), new AutoTransition().excludeChildren(binding.cardsView, true));
        binding.loadingImage.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.buttonsGroup.setVisibility(isLoading ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void onCardSwiped(Direction direction) {
        if (layoutManager.getTopPosition() == gifReferences.size()) {
            loadMoreGifs();
        }
        if (!binding.back.isEnabled()) {
            checkRewindPossibility();
        }
    }

    @Override
    public void onCardRewound() {
        if (binding.loadingImage.getVisibility() == View.VISIBLE && layoutManager.getTopPosition() == gifReferences.size() - 1) {
            handleLoadingIndicator(false);
            handleError(false);
            binding.next.setVisibility(View.VISIBLE);
        }
        checkRewindPossibility();
    }
}
