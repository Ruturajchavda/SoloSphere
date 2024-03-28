package ca.event.solosphere.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.github.ybq.android.spinkit.SpinKitView;

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
    public void hideToolbarBack() {
        this.baseActivity.hideToolbarBack();
    }

    public void showProgress(View view) {
        view.setVisibility(View.VISIBLE);
        baseActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void hideProgress(View view) {
        view.setVisibility(View.GONE);
        baseActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

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
