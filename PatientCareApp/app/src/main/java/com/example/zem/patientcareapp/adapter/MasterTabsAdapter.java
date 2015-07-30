package com.example.zem.patientcareapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.zem.patientcareapp.Fragment.PatientConsultationFragment;
import com.example.zem.patientcareapp.Fragment.PatientHistoryFragment;
import com.example.zem.patientcareapp.Fragment.ListOfDoctorsFragment;
import com.example.zem.patientcareapp.Fragment.PatientProfileFragment;
import com.example.zem.patientcareapp.Fragment.ProductsFragment;
import com.example.zem.patientcareapp.Fragment.PromoFragment;
import com.example.zem.patientcareapp.Fragment.ShoppingCartFragment;
import com.example.zem.patientcareapp.Fragment.TrialPrescriptionFragment;

/**
 * Created by Esel on 5/5/2015.
 */
//public class MasterTabsAdapter extends FragmentPagerAdapter {
public class MasterTabsAdapter extends FragmentStatePagerAdapter{

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
                return new TrialPrescriptionFragment();
            case 3:
                return new ListOfDoctorsFragment();
            case 4:
                return new PatientConsultationFragment();
            case 5:
                return new ProductsFragment();
            case 6:
                return new ShoppingCartFragment();
            case 7:
                return new PromoFragment();
            case 8:
                return new PatientHistoryFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 9;
    }
}
