package com.johny.musicplayer.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.widget.Button;

import com.johny.musicplayer.R;
import com.johny.musicplayer.activity.LibraryActivity;

public final class Themes {

    private static int primary;
    private static int primaryDark;
    private static int accent;

    private static int background;
    private static int backgroundElevated;
    private static int backgroundMiniplayer;

    /**
     * This class is never instantiated
     */
    private Themes() {

    }

    // Get Methods
    public static int getPrimary() {
        return primary;
    }

    public static int getPrimaryDark() {
        return primaryDark;
    }

    public static int getAccent() {
        return accent;
    }

    public static int getBackground() {
        return background;
    }

    public static int getBackgroundElevated() {
        return backgroundElevated;
    }

    public static int getBackgroundMiniplayer() {
        return backgroundMiniplayer;
    }

    @SuppressWarnings("deprecation")
    public static boolean isLight(Context context) {
        return background == context.getResources().getColor(R.color.background);
    }

    // Update method
    @SuppressWarnings("deprecation")
    public static void updateColors(Context context) {
        SharedPreferences prefs = Prefs.getPrefs(context);
        Resources resources = context.getResources();

        switch (Integer.parseInt(prefs.getString(Prefs.PRIMARY_COLOR, "5"))) {
            case 0: //Black
                primary = resources.getColor(R.color.primary_grey);
                primaryDark = resources.getColor(R.color.primary_dark_grey);
                accent = resources.getColor(R.color.accent_grey);
                break;
            case 1: //Red
                primary = resources.getColor(R.color.primary_red);
                primaryDark = resources.getColor(R.color.primary_dark_red);
                accent = resources.getColor(R.color.accent_red);
                break;
            case 2: //Orange
                primary = resources.getColor(R.color.primary_orange);
                primaryDark = resources.getColor(R.color.primary_dark_orange);
                accent = resources.getColor(R.color.accent_orange);
                break;
            case 3: //Yellow
                primary = resources.getColor(R.color.primary_yellow);
                primaryDark = resources.getColor(R.color.primary_dark_yellow);
                accent = resources.getColor(R.color.accent_yellow);
                break;
            case 4: //Green
                primary = resources.getColor(R.color.primary_green);
                primaryDark = resources.getColor(R.color.primary_dark_green);
                accent = resources.getColor(R.color.accent_green);
                break;
            case 6: //Purple
                primary = resources.getColor(R.color.primary_purple);
                primaryDark = resources.getColor(R.color.primary_dark_purple);
                accent = resources.getColor(R.color.accent_purple);
                break;
            default: //Blue & Unknown
                primary = resources.getColor(R.color.primary);
                primaryDark = resources.getColor(R.color.primary_dark);
                accent = resources.getColor(R.color.accent);
                break;
        }

        switch (Integer.parseInt(prefs.getString("prefBaseTheme", "1"))) {
            case 1: // Material Light
                background = resources.getColor(R.color.background);
                backgroundElevated = resources.getColor(R.color.background_elevated);
                backgroundMiniplayer = resources.getColor(R.color.background_miniplayer);
                break;
            default: // Material Dark
                background = resources.getColor(R.color.background_dark);
                backgroundElevated = resources.getColor(R.color.background_elevated_dark);
                backgroundMiniplayer = resources.getColor(R.color.background_miniplayer_dark);
                break;
        }
    }

    @StyleRes
    public static int getTheme(Context context) {
        SharedPreferences prefs = Prefs.getPrefs(context);
        int basePref = Integer.parseInt(prefs.getString(Prefs.BASE_COLOR, "1"));
        int primaryPref = Integer.parseInt(prefs.getString(Prefs.PRIMARY_COLOR, "5"));

        if (basePref == 1) {
            // Light Base
            switch (primaryPref) {
                case 0: // Black
                    return R.style.AppThemeLight_Black;
                case 1: // Red
                    return R.style.AppThemeLight_Red;
                case 2: // Orange
                    return R.style.AppThemeLight_Orange;
                case 3: // Yellow
                    return R.style.AppThemeLight_Yellow;
                case 4: // Green
                    return R.style.AppThemeLight_Green;
                case 6: // Purple
                    return R.style.AppThemeLight_Purple;
                default: // Blue or Unknown
                    return R.style.AppThemeLight_Blue;
            }
        } else {
            // Dark or Unknown Base
            switch (primaryPref) {
                case 0: // Black
                    return R.style.AppTheme_Black;
                case 1: // Red
                    return R.style.AppTheme_Red;
                case 2: // Orange
                    return R.style.AppTheme_Orange;
                case 3: // Yellow
                    return R.style.AppTheme_Yellow;
                case 4: // Green
                    return R.style.AppTheme_Green;
                case 6: // Purple
                    return R.style.AppTheme_Purple;
                default: // Blue or Unknown
                    return R.style.AppTheme_Blue;
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static void setTheme(Activity activity) {
        updateColors(activity);
        activity.setTheme(getTheme(activity));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(
                    activity.getResources().getString(R.string.app_name),
                    getIcon(activity),
                    primary);
            activity.setTaskDescription(taskDescription);
        } else {
            if (activity.getActionBar() != null) {
                activity.getActionBar().setBackgroundDrawable(new ColorDrawable(primary));
                if (!activity.getClass().equals(LibraryActivity.class)) {
                    activity.getActionBar().setIcon(new ColorDrawable(Color.TRANSPARENT));
                } else {
                    activity.getActionBar().setIcon(getIconId(activity));
                }
            }
        }
    }

    public static Bitmap getIcon(Context context) {
        return BitmapFactory.decodeResource(context.getResources(), getIconId(context));
    }

    public static Bitmap getLargeIcon(Context context, int density) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

            // Use a density 1 level higher than the display in case the launcher uses large icons
            if (density <= 0) {
                switch (context.getResources().getDisplayMetrics().densityDpi) {
                    case DisplayMetrics.DENSITY_LOW:
                        density = DisplayMetrics.DENSITY_MEDIUM;
                        break;
                    case DisplayMetrics.DENSITY_MEDIUM:
                        density = DisplayMetrics.DENSITY_HIGH;
                        break;
                    case DisplayMetrics.DENSITY_HIGH:
                        density = DisplayMetrics.DENSITY_XHIGH;
                        break;
                    case DisplayMetrics.DENSITY_XHIGH:
                        density = DisplayMetrics.DENSITY_XXHIGH;
                        break;
                    default:
                        density = DisplayMetrics.DENSITY_XXXHIGH;
                }
            }

            @SuppressWarnings("deprecation")
            BitmapDrawable icon = (BitmapDrawable) context.getResources()
                    .getDrawableForDensity(getIconId(context), density);

            if (icon != null) {
                return icon.getBitmap();
            }
        }
        return getIcon(context);
    }

    @DrawableRes
    public static int getIconId(Context context) {
        switch (Integer.parseInt(Prefs.getPrefs(context).getString(Prefs.PRIMARY_COLOR, "5"))) {
            case 0:
                return R.drawable.ic_launcher;
            case 1:
                return R.drawable.ic_launcher;
            case 2:
                return R.drawable.ic_launcher;
            case 3:
                return R.drawable.ic_launcher;
            case 4:
                return R.drawable.ic_launcher;
            case 6:
                return R.drawable.ic_launcher;
            default:
                return R.drawable.ic_launcher;
        }
    }

    public static void themeAlertDialog(AlertDialog dialog) {
        Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        Button neutral = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

        if (positive != null) {
            positive.setTextColor(getAccent());
        }
        if (negative != null) {
            negative.setTextColor(getAccent());
        }
        if (neutral != null) {
            neutral.setTextColor(getAccent());
        }
    }

    public static void updateLauncherIcon(Context context) {
        Intent launcherIntent = new Intent(context, LibraryActivity.class);

        // Uncomment to delete JMusic icons from the launcher
        // Don't forget to add permission "com.android.launcher.permission.UNINSTALL_SHORTCUT"
        // to AndroidManifest
        /*
        Intent delIntent = new Intent();
        delIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);
        delIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
            context.getResources().getString(R.string.app_name));
        delIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        context.sendBroadcast(delIntent);
        */

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources().getString(R.string.app_name));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, getLargeIcon(context, -1));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(addIntent);
    }

    public static void setApplicationIcon(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager.TaskDescription taskDescription =
                    new ActivityManager.TaskDescription(
                            activity.getResources().getString(R.string.app_name),
                            getIcon(activity), primary);
            activity.setTaskDescription(taskDescription);
        }
    }
}
