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
public class PhrasesFragment extends Fragment {
    private MediaPlayer mediaPlayer;
    MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    private AudioManager audioManager;

    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {

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


    public PhrasesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        View rootView = inflater.inflate(R.layout.words, container, false);


        final ArrayList<Word> arrayList = new ArrayList<>();
        arrayList.add(new Word("Where are you going?", "minto wuksus", R.raw.phrase_where_are_you_going));
        arrayList.add(new Word("What is your name?", "tinna oyaase'na", R.raw.phrase_what_is_your_name));
        arrayList.add(new Word("My name is..", "oyaaset", R.raw.phrase_my_name_is));
        arrayList.add(new Word("How are you feeling", "michaksas?", R.raw.phrase_how_are_you_feeling));
        arrayList.add(new Word("I'm feeling good", "kuchi achit", R.raw.phrase_im_feeling_good));
        arrayList.add(new Word("Are you coming", "aanas'aa?", R.raw.phrase_are_you_coming));
        arrayList.add(new Word("Yes, I'm coming", "haa'aanam", R.raw.phrase_yes_im_coming));
        arrayList.add(new Word("I'm coming", "aanam", R.raw.phrase_im_coming));
        arrayList.add(new Word("Lets go", "yoowutis", R.raw.phrase_lets_go));
        arrayList.add(new Word("Come here", "anni'nem", R.raw.phrase_come_here));

        WordAdapter itemsAdapter = new WordAdapter(getActivity(), arrayList, R.color.category_phrases);

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

    public void releaseMediaPlayer() {

        if (mediaPlayer != null) {

            mediaPlayer.release();
            mediaPlayer = null;
        }

        audioManager.abandonAudioFocus(onAudioFocusChangeListener);
    }
}
