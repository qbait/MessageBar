package eu.szwiec.messagebar;

import android.app.Activity;
import android.view.ViewGroup;

public class MessageBar {
    public static final int STYLE_INFO = 0;
    public static final int STYLE_ERROR = 1;
    public static final int STYLE_WARNING = 2;
    public static final int STYLE_HELP = 3;

    public static final int DURATION_INFINITE = -1;
    public static final int DURATION_SHORT = 3000;
    public static final int DURATION_LONG = 5000;

    Activity mActivity;
    String mMessage;
    String mActionMessage;
    int mDuration;
    int mActionIcon;
    MessageBarCallback mMessageBarCallback;
    ViewGroup mViewGroup;
    int mBackgroundRes;

    private MessageBar(Activity activity, String message, int style, int duration, int viewGroupRes, String actionMessage, int actionIcon, MessageBarCallback messageBarCallback) {
        if ((activity == null) || (message == null)) {
            throw new IllegalArgumentException("Null parameters are not accepted");
        }
        mActivity = activity;
        mMessage = message;
        mDuration = duration;
        mActionMessage = actionMessage;
        mActionIcon = actionIcon;
        mMessageBarCallback = messageBarCallback;
        mViewGroup = (ViewGroup) activity.findViewById(viewGroupRes);
        mBackgroundRes = getBackgroundResource(style);
    }

    public static MessageBar make(Activity activity, String message, int style, int duration, int container, String actionMessage, int actionIcon, MessageBarCallback messageBarCallback) {
        return new MessageBar(activity, message, style, duration, container, actionMessage, actionIcon, messageBarCallback);
    }

    public static void show(Activity activity, String message, int style, int duration, int container, String actionMessage, int actionIcon, MessageBarCallback messageBarCallback) {
        make(activity, message, style, duration, container, actionMessage, actionIcon, messageBarCallback).show();
    }

    public static void show(Activity activity, String message) {
        make(activity, message, STYLE_INFO, DURATION_SHORT, android.R.id.content, null, 0, null).show();
    }

    public static void show(Activity activity, String message, int style) {
        make(activity, message, style, DURATION_SHORT, android.R.id.content, null, 0, null).show();
    }

    public static void show(Activity activity, String message, int style, int container) {
        make(activity, message, STYLE_INFO, DURATION_SHORT, container, null, 0, null).show();
    }

    public static void showUndo(Activity activity, MessageBarCallback messageBarCallback, int container) {
        make(activity, activity.getString(R.string.data_saved), STYLE_INFO, DURATION_LONG, container, activity.getString(R.string.undo), R.drawable.mb__ic_messagebar_undo, messageBarCallback).show();
    }

    public static void showUndo(Activity activity, MessageBarCallback messageBarCallback) {
        make(activity, activity.getString(R.string.data_saved), STYLE_INFO, DURATION_LONG, android.R.id.content, activity.getString(R.string.undo), R.drawable.mb__ic_messagebar_undo, messageBarCallback).show();
    }

    public static void showUndo(Activity activity, String message, MessageBarCallback messageBarCallback) {
        make(activity, message, STYLE_INFO, DURATION_LONG, android.R.id.content, activity.getString(R.string.undo), R.drawable.mb__ic_messagebar_undo, messageBarCallback).show();
    }

    public static void showConfirm(Activity activity, String message, int style, int container) {
        make(activity, message, style, DURATION_INFINITE, container, activity.getString(R.string.ok), 0, defaultMessageBarCallback).show();
    }

    public static void showConfirm(Activity activity, String message, int style) {
        make(activity, message, style, DURATION_INFINITE, android.R.id.content, activity.getString(R.string.ok), 0, defaultMessageBarCallback).show();
    }

    public static void showError(Activity activity, String message) {
        show(activity, message, STYLE_ERROR);
    }

    public static void showWarning(Activity activity, String message) {
        show(activity, message, STYLE_WARNING);
    }

    public static void showHelp(Activity activity, String message) {
        show(activity, message, STYLE_HELP);
    }

    public void show() {
        Manager.getInstance().add(this);
    }

    public static void cancelAll() {
        Manager.getInstance().cancelAll();
    }

    private int getBackgroundResource(int style) {
        switch (style) {
            case STYLE_WARNING:
                return R.drawable.mb__messagebar_background_orange;
            case STYLE_ERROR:
                return R.drawable.mb__messagebar_background_red;
            case STYLE_HELP:
                return R.drawable.mb__messagebar_background_violet;
            default:
                return R.drawable.mb__messagebar_background_gray;
        }
    }

    private static MessageBarCallback defaultMessageBarCallback = new MessageBarCallback() {
        @Override
        public void onMessageClick() {
        }

        @Override
        public void onDisappearWhithoutMessageClick() {
        }
    };
}