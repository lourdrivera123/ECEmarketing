package com.example.zem.patientcareapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.zem.patientcareapp.AccountFragment;
import com.example.zem.patientcareapp.ContactsFragment;
import com.example.zem.patientcareapp.SignUpFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new SignUpFragment();
            case 1:
                return new ContactsFragment();
            case 2:
                return new AccountFragment();

        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}
