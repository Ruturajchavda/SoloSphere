package ca.event.solosphere.core.session;

import android.content.Context;
import android.content.SharedPreferences;

// Class for manage sessions(Shared Preference)
public class SessionManager {
    private static final String TAG = "SessionManager";

    public static final String KEY_IS_SHOW_INTRO = "is_show_intro";

    private static SessionManager sessionManager;
    // Shared Preferences
    private SharedPreferences pref;
    // Editor for Shared preferences
    private SharedPreferences.Editor editor;
    // Context
    private Context _context;
    // Shared pref mode
    private int PRIVATE_MODE = 0;

    public static SessionManager getInstance() {
        if (sessionManager == null) {
            sessionManager = new SessionManager();
        }
        return sessionManager;
    }

    public void initSessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(context.getApplicationInfo().packageName, PRIVATE_MODE);
        editor = pref.edit();
    }


    public boolean isShowIntro() {
        return pref.getBoolean(KEY_IS_SHOW_INTRO, true);
    }

    public void createIntroSession() {
        // Storing login value as TRUE
        editor.putBoolean(KEY_IS_SHOW_INTRO, false);
        // commit changes
        editor.commit();
    }
}
