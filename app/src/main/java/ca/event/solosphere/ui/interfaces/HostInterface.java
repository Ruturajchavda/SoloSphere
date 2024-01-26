package ca.event.solosphere.ui.interfaces;


import androidx.fragment.app.Fragment;


public interface HostInterface {

    void changeCurrentFragmentTo(Fragment fragment);

    void setToolbarTitle(int resId);

    void setToolbarTitle(String format);

    void hideToolbar();

    void showToolbar();

    void setIcon(int icon);

}
