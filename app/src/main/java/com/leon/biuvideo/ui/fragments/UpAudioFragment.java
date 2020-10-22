package com.leon.biuvideo.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.UpAudioAdapter;
import com.leon.biuvideo.beans.upMasterBean.UpAudio;

import java.util.List;

public class UpAudioFragment extends Fragment {
    private List<UpAudio> upAudios;
    private Context context;

    private View view;
    private RecyclerView up_audio_recyclerView;

    public UpAudioFragment(List<UpAudio> upAudios, Context context) {
        this.upAudios = upAudios;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_up_picture, container, false);

        initView();
        initValue();

        return view;
    }

    private void initView() {
        up_audio_recyclerView  = view.findViewById(R.id.up_audio_recyclerView);
    }

    private void initValue() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        up_audio_recyclerView.setLayoutManager(layoutManager);

        UpAudioAdapter upAudioAdapter = new UpAudioAdapter(upAudios, context);
        up_audio_recyclerView.setAdapter(upAudioAdapter);
    }
}
