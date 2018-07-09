package com.example.mohamedabdelaziz.geo.medicalrep;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.mohamedabdelaziz.geo.R;
import com.example.mohamedabdelaziz.geo.medicalrep.fragment.ClinicFragment;
import com.example.mohamedabdelaziz.geo.medicalrep.fragment.HospitalFragment;
import com.example.mohamedabdelaziz.geo.medicalrep.fragment.OrderFragment;
import com.example.mohamedabdelaziz.geo.medicalrep.fragment.PharmacyFragment;
import com.example.mohamedabdelaziz.geo.medicalrep.fragment.ProfielFragment;
import com.example.mohamedabdelaziz.geo.medicalrep.fragment.TasksFragment;

public class MedicalRepMain extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_main);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return new TasksFragment();
            else if (position == 1)
                return new OrderFragment();
            else if (position == 2)
                return new HospitalFragment();
            else if (position == 3)
                return new ClinicFragment();
            else if (position == 4)
                return new PharmacyFragment();
            else if (position == 5)
                return new ProfielFragment();

            return null;
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Tasks";
                case 1:
                    return "Orders";
                case 2:
                    return "Hospital";
                case 3:
                    return "Clinic";
                case 4:
                    return "Pharmacy";
                case 5:
                    return "Profile";
            }
            return null;
        }
    }
}
