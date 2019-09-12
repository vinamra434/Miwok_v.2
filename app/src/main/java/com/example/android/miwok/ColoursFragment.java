package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class ColoursFragment extends Fragment {

    private MediaPlayer mediaPlayer;
    MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    private AudioManager audioManager;

    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener(){
        public void onAudioFocusChange (int focusChange){

            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                mediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();
            }
        }
    };

    public ColoursFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.words, container, false);
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Word> arrayList = new ArrayList<>();
        arrayList.add(new Word("red","wetetti", R.drawable.color_red,R.raw.color_red));
        arrayList.add(new Word("mustard yellow","chiwiita", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));
        arrayList.add(new Word("dusty yellow","topiisa",R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        arrayList.add(new Word("green","chokokki",R.drawable.color_green, R.raw.color_green));
        arrayList.add(new Word("brown","takaakki",R.drawable.color_brown, R.raw.color_brown));
        arrayList.add(new Word("gray","topoppi", R.drawable.color_gray, R.raw.color_gray));
        arrayList.add(new Word("black","kululli", R.drawable.color_black, R.raw.color_black));
        arrayList.add(new Word("white","kelelli", R.drawable.color_white, R.raw.color_white));

        WordAdapter itemsAdapter = new WordAdapter(getActivity(), arrayList, R.color.category_colors);

        ListView listView = (ListView) rootView.findViewById(R.id.words);

        listView.setAdapter(itemsAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Word word = arrayList.get(i);
                int result = audioManager.requestAudioFocus(onAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    releaseMediaPlayer();
                    mediaPlayer = MediaPlayer.create(getActivity(), word.getmAudioResourceID());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(onCompletionListener);
                }

            }
        });


    return rootView;
    }


    public void releaseMediaPlayer(){

        if (mediaPlayer != null){

            mediaPlayer.release();
            mediaPlayer = null;
        }

        audioManager.abandonAudioFocus(onAudioFocusChangeListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
}
