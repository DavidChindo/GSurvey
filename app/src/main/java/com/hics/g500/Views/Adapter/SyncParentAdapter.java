package com.hics.g500.Views.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.hics.g500.R;
import com.hics.g500.Views.Fragment.NotesVoiceFragment;
import com.hics.g500.Views.Fragment.SyncFragment;

import java.util.ArrayList;

import in.galaxyofandroid.awesometablayout.AwesomeTabBarAdapter;

/**
 * Created by david.barrera on 2/11/18.
 */

public class SyncParentAdapter extends AwesomeTabBarAdapter {
    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();
    int[] colors = {R.color.colorWhite};
    int[] textColors = {R.color.text_tab_unselect};

    public SyncParentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragments.add(new SyncFragment());
        //fragments.add(new NotesVoiceFragment());

        titles.add("GASOLINERAS");
        //titles.add("NOTAS DE VOZ");
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new SyncFragment();
           /* case 1:
                return new NotesVoiceFragment();*/
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getColorResource(int position) {
        return colors[position];
    }

    @Override
    public int getTextColorResource(int position) {
        return textColors[position];
    }

    @Override
    public int getIconResource(int i) {
        return 0;
    }

}
