package com.example.zem.patientcareapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.zem.patientcareapp.PatientConsultationFragment;
import com.example.zem.patientcareapp.PatientHistoryFragment;
import com.example.zem.patientcareapp.ListOfDoctorsFragment;
import com.example.zem.patientcareapp.PatientProfileFragment;
import com.example.zem.patientcareapp.ProductsFragment;
import com.example.zem.patientcareapp.ShoppingCartFragment;

/**
 * Created by Esel on 5/5/2015.
 */
public class MasterTabsAdapter extends FragmentPagerAdapter {

    public MasterTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return new PatientProfileFragment();
            case 1:
                return new PatientHistoryFragment();
            case 2:
                return new PatientHistoryFragment();
            case 3:
                return new ListOfDoctorsFragment();
            case 4:
                return new PatientConsultationFragment();
            case 5:
                return new ProductsFragment();
            case 6:
                return new ShoppingCartFragment();
            case 7:
                return new PatientHistoryFragment();
            case 8:
                return new PatientHistoryFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 8;
    }
}
