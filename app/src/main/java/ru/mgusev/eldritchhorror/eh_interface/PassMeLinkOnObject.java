package ru.mgusev.eldritchhorror.eh_interface;

import android.view.View;

import ru.mgusev.eldritchhorror.model.Game;

public interface PassMeLinkOnObject {

    Game getGame();

    void setCurrentFocusView(View view);

    View getCurrentFocusView();

    void setIdPositionChange(boolean value);

    boolean getIsPositionChange();
}
