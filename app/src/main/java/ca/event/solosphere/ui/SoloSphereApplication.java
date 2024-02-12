package ca.event.solosphere.ui;

import android.app.Application;

import ca.event.solosphere.core.session.SessionManager;


public class SoloSphereApplication extends Application {

    private static final String TAG = SoloSphereApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        //Initiate Session Manger class to access store Shared Preferences
        SessionManager.getInstance().initSessionManager(getApplicationContext());
    }
}