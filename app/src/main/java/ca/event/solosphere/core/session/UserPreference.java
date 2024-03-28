package ca.event.solosphere.core.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class UserPreference {
    private static String USER_PREFERENCE = "user_preference";
    private static String LIKED_EVENTS = "liked_events";


    private static SharedPreferences getPreference(Context mContext) {
        return mContext.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getPreferenceForEdit(Context mContext) {
        return mContext.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE).edit();
    }


    // Save list to SharedPreferences
    public static void saveLikedEvents(Context context, List<String> list) {
        SharedPreferences prefs = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(LIKED_EVENTS, json);
        editor.apply();
    }

    // Retrieve list from SharedPreferences
    public static List<String> getLikedEvents(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(LIKED_EVENTS, null);
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(json, type);
    }


}
