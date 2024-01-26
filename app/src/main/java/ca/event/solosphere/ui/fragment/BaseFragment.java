package ca.event.solosphere.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import ca.event.solosphere.ui.activity.BaseFragmentActivity;
import ca.event.solosphere.ui.interfaces.FragmentProvider;

public class BaseFragment extends Fragment implements FragmentProvider {
    protected BaseFragmentActivity baseActivity;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        this.baseActivity = (BaseFragmentActivity) activity;

    }

    public void setToolbarTitle(String title) {
        this.baseActivity.setToolbarTitle(title);
    }

    /*
     * To set the toolbar title.
     *
     * @param titleResId The string resource Id.
     */
    public void setToolbarTitle(int titleResId) {
        this.baseActivity.setToolbarTitle(titleResId);
    }

    public void setToolbarIcon(int icon) {
        this.baseActivity.setToolbarIcon(icon);
    }

    public void hideToolbar() {
        this.baseActivity.hideToolbar();
    }

    @Override
    public BaseFragment createFragment(Bundle bundle) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
