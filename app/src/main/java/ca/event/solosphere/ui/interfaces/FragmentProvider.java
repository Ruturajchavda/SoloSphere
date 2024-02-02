package ca.event.solosphere.ui.interfaces;


import android.os.Bundle;


import java.io.Serializable;

import ca.event.solosphere.ui.fragment.BaseFragment;

public interface FragmentProvider extends Serializable {

    /**
     * Extra key for fragment provider.
     */
    String EXTRA_FRAGMENT_PROVIDER = "com.casinex.extra.FragmentProvider";
    /**
     * Extra key for bundle.
     */
    String EXTRA_BUNDLE = "com.casinex.extra.Bundle";

    /**
     * To create the fragment.
     *
     * @param bundle The input argument bundle.
     * @return The created fragment.
     */

    BaseFragment createFragment(Bundle bundle);

    /**
     * To get the tag for fragment.
     *
     * @return The unique tag to identify fragment.
     */
    String getTag();
}
