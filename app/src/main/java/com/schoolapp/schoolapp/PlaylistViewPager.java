package com.schoolapp.schoolapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PlaylistViewPager extends Fragment {

    protected ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    public PlaylistViewPager() {
        // Required empty public constructor
    }

    public static PlaylistViewPager newInstance() {
        PlaylistViewPager fragment = new PlaylistViewPager();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlist_view_pager, container, false);
        mViewPager = view.findViewById(R.id.playlistVP);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        return view;
    }


    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private final FragmentManager mFragmentManager;
        private Fragment mFragmentAtPos1, replacefrag;
        private boolean repfragchanged = false;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
            {
                if (mFragmentAtPos1 == null)
                {
                    mFragmentAtPos1 = Playlist.newInstance(new Playlist.PlaylistselectedListener()
                    {
                        @Override
                        public void onPlaylistSelected(String table) {
                            mFragmentManager.beginTransaction().remove(replacefrag).commit();
                            replacefrag = Playlistsongs.newInstance(table);
                            repfragchanged = true;
                            notifyDataSetChanged();
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                        }
                    });
                }
                return mFragmentAtPos1;
            }
            else{
                if(!repfragchanged)replacefrag = Playlistsongs.newInstance(null);
                return replacefrag;
            }
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
                return POSITION_NONE;
        }

        @Override
        public int getCount() {
            // Show 2 total pages
            return 2;
        }
    }
}

