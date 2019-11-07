package com.schoolapp.schoolapp.music.fragments;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.schoolapp.schoolapp.R;
import com.schoolapp.schoolapp.music.viewmodel.EQViewModel;
import com.schoolapp.schoolapp.music.Musicplayer;

import java.util.ArrayList;


public class EQ extends Fragment {

    static short numberFreqBands;
    static short lowerEQBandLevel, upperEQBandLevel;
    static short BandLevel;
    private ArrayList<Integer> centerfreq;
    private ArrayList<String> presetlist;
    private Button menu;
    private short[] bandlevelstart;
    private ListView presets;


    public EQ() {
        // Required empty public constructor
    }

    public static EQ newInstance() {
        EQ fragment = new EQ();
        return fragment;
    }

    OnEQChangedListener onEQChangedListener;
    public interface OnEQChangedListener{
        void OnBandLevelChanged(short BandLevel, short BandIndex);
        void OnPresetSelected(short position);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EQViewModel eqViewModel = ViewModelProviders.of(getActivity()).get(EQViewModel.class);
        eqViewModel.getBandlevel().observe(getViewLifecycleOwner(), new Observer<Short[]>() {
            @Override
            public void onChanged(@Nullable Short[] aShort) {
                for(short i=0;i<numberFreqBands;i++){
                    SeekBar sb = getView().findViewById(i);
                    sb.setProgress(aShort[i] + upperEQBandLevel);
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onEQChangedListener = (OnEQChangedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnStateChangeListener");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_equalizer, container, false);
        menu = view.findViewById(R.id.menubttn);
        presets = view.findViewById(R.id.presets);

        if(getArguments()!=null){
            numberFreqBands = getArguments().getShort("Number");
            lowerEQBandLevel = getArguments().getShort("Lower");
            upperEQBandLevel = getArguments().getShort("Upper");
            centerfreq = getArguments().getIntegerArrayList("Freq");
            bandlevelstart = getArguments().getShortArray("StartV");
            presetlist = getArguments().getStringArrayList("Presets");
        }

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Musicplayer)getActivity()).openDrawer();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, presetlist) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(getResources().getColor(R.color.text));
                return textView;
            }
        };
        presets.setAdapter(adapter);
        presets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onEQChangedListener.OnPresetSelected((short) i);
            }
        });


        //****************** SEEKBAR **********************
        LinearLayout linearLayout = view.findViewById(R.id.seekbarholder);

        for(short i = 0; i < numberFreqBands; i++){
            final short eqBandIndex = i;

            TextView fheaderTexview = new TextView(getContext());
            fheaderTexview.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            fheaderTexview.setGravity(Gravity.CENTER_HORIZONTAL);

            fheaderTexview.setTextColor(getResources().getColor(R.color.text));


            LinearLayout seekBarRowLayout = new LinearLayout(getContext());
            seekBarRowLayout.setOrientation(LinearLayout.HORIZONTAL);

            TextView lowEQBandLvl = new TextView(getContext());
            lowEQBandLvl.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            lowEQBandLvl.setTextColor(getResources().getColor(R.color.text));
            lowEQBandLvl.setText((lowerEQBandLevel / 100)+ "dB");
            lowEQBandLvl.setTextSize(14);

            TextView upperEQBandlvl = new TextView(getContext());
            upperEQBandlvl.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            upperEQBandlvl.setTextColor(getResources().getColor(R.color.text));
            upperEQBandlvl.setText((upperEQBandLevel / 100) + "dB");
            upperEQBandlvl.setTextSize(14);

//          ******************** SeekBar *************************
            LinearLayout.LayoutParams seeklay = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            seeklay.weight=1;

            final SeekBar seekBar = new SeekBar(getContext());
            seekBar.setId(i);
            seekBar.setLayoutParams(seeklay);
            seekBar.setMax(upperEQBandLevel - lowerEQBandLevel);
            seekBar.setThumb(getResources().getDrawable(R.drawable.custom_thumb));
            seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.text), PorterDuff.Mode.MULTIPLY);

            seekBar.setProgress(bandlevelstart[i] + upperEQBandLevel);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    onEQChangedListener.OnBandLevelChanged((short)(i + lowerEQBandLevel), eqBandIndex);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            fheaderTexview.setText((centerfreq.get(i))/1000 + "Hz");
            fheaderTexview.setTextSize(16);
            fheaderTexview.setPadding(0,10,0,0);
            linearLayout.addView(fheaderTexview);
            seekBarRowLayout.addView(lowEQBandLvl);
            seekBarRowLayout.addView(seekBar);
            seekBarRowLayout.addView(upperEQBandlvl);
            seekBarRowLayout.setPadding(20,10,20,10);
            linearLayout.addView(seekBarRowLayout);
        }
        return view;
    }

    private void PresetList(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, presetlist);
        presets.setAdapter(adapter);

    }

}
