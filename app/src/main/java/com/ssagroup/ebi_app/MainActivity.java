package com.ssagroup.ebi_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.ssagroup.ebi_app.adapters.RecyclerViewAdapter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //    private DrawerLayout mDrawerLayout;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
//        ActionBar supportActionBar = getSupportActionBar();
//        if (supportActionBar != null) {
//            VectorDrawableCompat indicator
//                    = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
//            indicator.setTint(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()));
//            supportActionBar.setHomeAsUpIndicator(indicator);
//            supportActionBar.setDisplayHomeAsUpEnabled(true);
//        }
//
//        navigationView.setNavigationItemSelectedListener(
//                new NavigationView.OnNavigationItemSelectedListener() {
//                    // This method will trigger on item Click of navigation menu
//                    @Override
//                    public boolean onNavigationItemSelected(MenuItem menuItem) {
//                        menuItem.setChecked(true);
//
//                        // TODO: handle navigation
//
//                        mDrawerLayout.closeDrawers();
//                        return true;
//                    }
//                });
    }
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "HOME");
        adapter.addFragment(new CoursesFragment(), "COURSES");
        adapter.addFragment(new ScheduleFragment(), "SCHEDULE");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();



        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_contact:
                startActivity(new Intent(this, AboutContacts.class));
                return true;


        }
//        int id = item.getItemId();
//        if (id == R.id.action_contact) {
//            return true;
//        } else if (id == android.R.id.home) {
//            mDrawerLayout.openDrawer(GravityCompat.START);
//        }
        return super.onOptionsItemSelected(item);
    }
}
