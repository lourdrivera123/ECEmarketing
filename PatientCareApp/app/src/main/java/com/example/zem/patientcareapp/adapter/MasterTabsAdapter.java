package com.example.zem.patientcareapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.zem.patientcareapp.Fragment.PatientConsultationFragment;
import com.example.zem.patientcareapp.Fragment.ReferralsFragment;
import com.example.zem.patientcareapp.Fragment.TrialPrescriptionFragment;

/**
 * Created by Esel on 5/5/2015.
 */
public class MasterTabsAdapter extends FragmentStatePagerAdapter {

    public MasterTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return new ReferralsFragment();
            case 1:
                return new ReferralsFragment();
            case 2:
                return new TrialPrescriptionFragment();
            case 3:
                return new PatientConsultationFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
