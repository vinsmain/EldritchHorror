package ru.mgusev.eldritchhorror.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import ru.mgusev.eldritchhorror.activity.GamesPagerActivity;
import ru.mgusev.eldritchhorror.fragment.InvestigatorsChoiceFragment;
import ru.mgusev.eldritchhorror.eh_interface.PassMeLinkOnObject;
import ru.mgusev.eldritchhorror.R;
import ru.mgusev.eldritchhorror.fragment.ResultGameFragment;
import ru.mgusev.eldritchhorror.fragment.StartingDataFragment;

public class EHFragmentPagerAdapter extends FragmentPagerAdapter {

    private StartingDataFragment startingDataFragment;
    private InvestigatorsChoiceFragment investigatorsChoiceFragment;
    private ResultGameFragment resultGameFragment;
    private String[] titleArray;

    public EHFragmentPagerAdapter(Context context, FragmentManager fm, PassMeLinkOnObject activity) {
        super(fm);
        titleArray = new String[]{context.getString(R.string.activity_add_party_header), context.getString(R.string.activity_investigators_choice_header), context.getString(R.string.activity_result_party_header)};
        startingDataFragment = StartingDataFragment.newInstance(0, activity);
        investigatorsChoiceFragment = InvestigatorsChoiceFragment.newInstance(1, activity);
        resultGameFragment = ResultGameFragment.newInstance(2, activity);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) return startingDataFragment;
        else if (position == 1) return investigatorsChoiceFragment;
        else return resultGameFragment;
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