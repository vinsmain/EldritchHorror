package ru.mgusev.eldritchhorror.eh_interface;

import android.view.View;

import ru.mgusev.eldritchhorror.adapter.EHFragmentPagerAdapter;
import ru.mgusev.eldritchhorror.model.Game;

public interface PassMeLinkOnObject {

    Game getGame();

    EHFragmentPagerAdapter getPagerAdapter();
}
