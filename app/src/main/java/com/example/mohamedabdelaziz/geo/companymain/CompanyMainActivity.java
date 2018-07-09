package com.example.mohamedabdelaziz.geo.companymain;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.mohamedabdelaziz.geo.R;
import com.example.mohamedabdelaziz.geo.chart.ChartFragment;

public class CompanyMainActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_main);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return new OrderFragment();
            else if (position == 1)
                return new ProductFragment();
            else if (position == 2)
                return new TasksFragment();
            else if (position == 3)
                return new ChartFragment();
            else if (position == 4)
                return new FeedbackFragment();
            else if (position == 5)
                return new MembersFragment();
            else if (position == 6)
                return new UsersFragment();
            else
                return new ProfielFragment();
        }

        @Override
        public int getCount() {
            return 8;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Order";
                case 1:
                    return "Product";
                case 2:
                    return "Tasks";
                case 3:
                    return "Performance";
                case 4:
                    return "Feedback";
                case 5:
                    return "Members";
                case 6:
                    return "Users";
                case 7:
                    return "Profile";
            }
            return null;
        }
    }
}
