package com.ehospital;

import android.widget.Switch;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


class SectionAdopter extends FragmentPagerAdapter {


    public SectionAdopter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ChatDoctorFragment requestFraq = new ChatDoctorFragment();
                return requestFraq;

            case 1:
                 OnlineDoctorFragment onlineDoctorFragment = new OnlineDoctorFragment();
                 return onlineDoctorFragment;

            case 2:
                 AllDoctorsFragments allDoctorsFragments = new AllDoctorsFragments();
                 return allDoctorsFragments;

            default:
                return  null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }


    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                return "Chat Doctors";

            case 1:
                return "Online Doctors";

            case 2:
                return "All Doctors";

            default:
                return null;
        }
    }
}
