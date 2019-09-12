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

/**
 * A simple {@link Fragment} subclass.
 */
public class NumbersFragment extends Fragment {

    private MediaPlayer mediaPlayer;

    private AudioManager audioManager;
    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };


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

    public NumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.words, container, false);
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> arrayList = new ArrayList<>();
        arrayList.add(new Word("one","lutti",R.drawable.number_one, R.raw.number_one));
        arrayList.add(new Word("two","otiiko", R.drawable.number_two, R.raw.number_two));
        arrayList.add(new Word("three","tolookosu", R.drawable.number_three, R.raw.number_three));
        arrayList.add(new Word("four","oyyisa", R.drawable.number_four, R.raw.number_four ));
        arrayList.add(new Word("five","massokka", R.drawable.number_five, R.raw.number_five));
        arrayList.add(new Word("six","temmokka", R.drawable.number_six, R.raw.number_six));
        arrayList.add(new Word("seven","kenekaku", R.drawable.number_seven, R.raw.number_seven));
        arrayList.add(new Word("eight","kawinta", R.drawable.number_eight, R.raw.number_eight));
        arrayList.add(new Word("nine","wo'e", R.drawable.number_nine, R.raw.number_nine));
        arrayList.add(new Word("ten","na'accha", R.drawable.number_ten, R.raw.number_ten));

        WordAdapter itemsAdapter = new WordAdapter(getActivity(), arrayList, R.color.category_numbers);

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

    @Override
    public void onStop() {
        super.onStop();

        releaseMediaPlayer();
    }

    public void releaseMediaPlayer(){

        if (mediaPlayer != null){

            mediaPlayer.release();
            mediaPlayer = null;
        }

        audioManager.abandonAudioFocus(onAudioFocusChangeListener);
    }
}


