package com.termux.shared.settings.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/** A class that holds {@link SharedPreferences} objects for apps. */
public class AppSharedPreferences {

    /** The {@link Context} for operations. */
    protected final Context mContext;

    /** The {@link SharedPreferences} that ideally should be created with {@link SharedPreferenceUtils#getPrivateSharedPreferences(Context, String)}. */
    protected final SharedPreferences mSharedPreferences;

    /** The {@link SharedPreferences}that ideally should be created with {@link SharedPreferenceUtils#getPrivateAndMultiProcessSharedPreferences(Context, String)}. */
    protected final SharedPreferences mMultiProcessSharedPreferences;

    protected AppSharedPreferences(@NonNull Context context, @Nullable SharedPreferences sharedPreferences) {
        this(context, sharedPreferences, null);
    }

    protected AppSharedPreferences(@NonNull Context context, @Nullable SharedPreferences sharedPreferences,
                                   @Nullable SharedPreferences multiProcessSharedPreferences) {
        mContext = context;
        mSharedPreferences = sharedPreferences;
        mMultiProcessSharedPreferences = multiProcessSharedPreferences;
    }



    /** Get {@link #mContext}. */
    public Context getContext() {
        return mContext;
    }

    /** Get {@link #mSharedPreferences}. */
    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    /** Get {@link #mMultiProcessSharedPreferences}. */
    public SharedPreferences getMultiProcessSharedPreferences() {
        return mMultiProcessSharedPreferences;
    }

    /**
     * Get a suffix for preference keys whose value should be stored per display, like the font size,
     * so that a secondary display like Samsung DeX keeps its own value. Returns an empty string for
     * {@link Display#DEFAULT_DISPLAY} so that existing keys of the primary display are preserved.
     *
     * The display is the one {@link #mContext} is associated with. A context that is not associated
     * with a display, like an application or service context, is treated as the default display.
     */
    protected String getDisplayIdSuffix() {
        Display display;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                display = mContext.getDisplay();
            else
                display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        } catch (Exception e) {
            // Context.getDisplay() throws UnsupportedOperationException for non-UI contexts.
            return "";
        }

        if (display == null || display.getDisplayId() == Display.DEFAULT_DISPLAY)
            return "";
        else
            return Integer.toString(display.getDisplayId());
    }

}
