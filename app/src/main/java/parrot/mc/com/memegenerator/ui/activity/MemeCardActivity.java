package parrot.mc.com.memegenerator.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import parrot.mc.com.memegenerator.R;
import parrot.mc.com.memegenerator.ui.fragment.MemeCardFragment;

public class MemeCardActivity extends AppCompatActivity implements MemeCardFragment.OnFragmentInteractionListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private LoadMorePagerAdapter loadMorePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_card);
        initView();
        setEvent();
    }

    private void initView(){
        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("Meme Generator");
        setSupportActionBar(mToolbar);

        tabLayout = findViewById(R.id.tab_lay);
        tabLayout.addTab(tabLayout.newTab().setText("Trending"));
        tabLayout.addTab(tabLayout.newTab().setText("New"));
        tabLayout.addTab(tabLayout.newTab().setText("Popular"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(3);

        loadMorePagerAdapter = new LoadMorePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(loadMorePagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void setEvent(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    private class LoadMorePagerAdapter extends FragmentPagerAdapter {

        final int NUM_TAB = 3;

        public LoadMorePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return MemeCardFragment.newInstance("Trending");
                case 1:
                    return MemeCardFragment.newInstance("New");
                case 2:
                    return MemeCardFragment.newInstance("Popular");
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_TAB;
        }
    }

}
