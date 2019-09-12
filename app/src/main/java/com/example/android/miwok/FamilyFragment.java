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

public class FamilyFragment extends Fragment {

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

    public FamilyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.words, container, false);

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Word> arrayList = new ArrayList<>();
        arrayList.add(new Word("father","apa",R.drawable.family_father, R.raw.family_father));
        arrayList.add(new Word("mother","ata", R.drawable.family_mother, R.raw.family_mother));
        arrayList.add(new Word("son","angsi", R.drawable.family_son, R.raw.family_son));
        arrayList.add(new Word("daughter","tune", R.drawable.family_daughter, R.raw.family_daughter));
        arrayList.add(new Word("older brother","taachi", R.drawable.family_older_brother, R.raw.family_older_brother));
        arrayList.add(new Word("younger brother","chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        arrayList.add(new Word("older sister","tete", R.drawable.family_older_sister, R.raw.family_older_sister));
        arrayList.add(new Word("younger sister","kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        arrayList.add(new Word("grandmother","ama", R.drawable.family_grandmother, R.raw.family_grandmother));
        arrayList.add(new Word("grandfather","paaapa", R.drawable.family_grandfather, R.raw.family_grandfather));

        WordAdapter itemsAdapter = new WordAdapter(getActivity(), arrayList, R.color.category_family);

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
                    mediaPlayer.setOnCompletionListener(onCompletionListener);}
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
