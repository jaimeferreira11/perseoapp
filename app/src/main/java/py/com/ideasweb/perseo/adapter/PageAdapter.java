package py.com.ideasweb.perseo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.bumptech.glide.util.LruCache;

import java.util.ArrayList;

/**
 * Created by jaime on 16/11/16.
 */

public class PageAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> fragments;
    private final LruCache<Integer, Fragment> mCache;

    public PageAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);

        this.fragments = fragments;
        mCache = new LruCache<Integer, Fragment>(10);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }


}
