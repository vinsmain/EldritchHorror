package ru.mgusev.eldritchhorror;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class EHFragmentPagerAdapter extends FragmentPagerAdapter {

    private StartingDataFragment startingDataFragment;
    private InvestigatorsChoiceFragment investigatorsChoiceFragment;
    private ResultGameFragment resultGameFragment;

    String[] titleArray;

    EHFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        titleArray = new String[]{context.getString(R.string.activity_add_party_header), context.getString(R.string.activity_investigators_choice_header), context.getString(R.string.activity_result_party_header)};
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            startingDataFragment = StartingDataFragment.newInstance(position);
            return startingDataFragment;
        }
        else if (position == 1) {
            investigatorsChoiceFragment = InvestigatorsChoiceFragment.newInstance(position);
            return investigatorsChoiceFragment;
        }
        else {
            resultGameFragment = ResultGameFragment.newInstance(position);
            return resultGameFragment;
        }
    }

    @Override
    public int getCount() {
        return GamesPagerActivity.PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleArray[position];
    }
}

