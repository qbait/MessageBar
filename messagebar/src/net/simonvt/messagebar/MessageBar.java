package net.simonvt.messagebar;

import android.app.Activity;
import android.os.Handler;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import java.util.LinkedList;

public class MessageBar {
    public interface OnMessageClickListener {
        void onMessageClick(Parcelable token);
    }

    public interface OnMessageDisappearWithoutClickListener {
        void onMessageDisappearWithoutClick(Parcelable token);
    }

    public static final int INFO = 0;
    public static final int ERROR = 1;
    public static final int WARNING = 2;
    private static final int ANIMATION_DURATION = 600;
    private static final int HIDE_DELAY = 2000;

    private static MessageBar mInstance;

    private Activity mActivity;
    private View mContainer;
    private TextView mTextView;
    private TextView mButton;
    private LinkedList<Message> mMessages = new LinkedList<Message>();
    private Message mCurrentMessage;
    private boolean mShowing;
    private Handler mHandler;
    private AlphaAnimation mFadeInAnimation;
    private AlphaAnimation mFadeOutAnimation;

    public static MessageBar getInstance() {
        if (mInstance == null) {
            mInstance = new MessageBar();
        }
        return mInstance;
    }

    private MessageBar() {
        mFadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
        mFadeOutAnimation = new AlphaAnimation(1.0f, 0.0f);
        mFadeOutAnimation.setDuration(ANIMATION_DURATION);
        mFadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                hideMessageBar();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mHandler = new Handler();
    }

    private void hideMessageBar() {
        View mbContainer = mActivity.findViewById(R.id.mbContainer);
        ((ViewGroup) mbContainer.getParent()).removeView(mbContainer);

        if (mCurrentMessage != null && mCurrentMessage.mDisappearListener != null) {
            mCurrentMessage.mDisappearListener.onMessageDisappearWithoutClick(mCurrentMessage.mToken);
        }

        Message nextMessage = mMessages.poll();

        if (nextMessage != null) {
            show(nextMessage);
        } else {
            mCurrentMessage = null;
            mContainer.setVisibility(View.GONE);
            mShowing = false;
        }
    }

/*    public void show(String message, int type) {
        show(message, type, null);
    }

    public void show(String message, int type, String actionMessage) {
        show(message, type, actionMessage, 0);
    }

    public void show(String message, int type, String actionMessage, int actionIcon) {
        show(message, type, actionMessage, actionIcon, null);
    }*/

    public void show(Activity activity, String message, int type, String actionMessage, int actionIcon, Parcelable token, OnMessageClickListener clickListener, OnMessageDisappearWithoutClickListener disappearListener, int container) {
        mActivity = activity;
        Message m = new Message(message, type, actionMessage, actionIcon, token, clickListener, disappearListener, container);
        if (mShowing) {
            mMessages.add(m);
        } else {
            show(m);
        }
    }

    private void show(Message message) {
        show(message, false);
    }

    private void show(Message message, boolean immediately) {
        View v = mActivity.getLayoutInflater().inflate(R.layout.mb__messagebar, (ViewGroup) mActivity.findViewById(message.mContainer));
        mContainer = v.findViewById(R.id.mbContainer);

        mTextView = (TextView) v.findViewById(R.id.mbMessage);
        mButton = (TextView) v.findViewById(R.id.mbButton);
        mButton.setOnClickListener(mButtonListener);

        mShowing = true;
        mContainer.setVisibility(View.VISIBLE);
        mContainer.setBackgroundResource(getBackgroundResource(message.mType));
        mCurrentMessage = message;
        mTextView.setText(message.mMessage);
        if (message.mActionMessage != null) {
            mTextView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            mButton.setVisibility(View.VISIBLE);
            mButton.setText(message.mActionMessage);
            mButton.setCompoundDrawablesWithIntrinsicBounds(message.mActionIcon, 0, 0, 0);
        } else {
            mTextView.setGravity(Gravity.CENTER);
            mButton.setVisibility(View.GONE);
        }

        if (immediately) {
            mFadeInAnimation.setDuration(0);
        } else {
            mFadeInAnimation.setDuration(ANIMATION_DURATION);
        }
        mContainer.startAnimation(mFadeInAnimation);
        mHandler.postDelayed(mHideRunnable, HIDE_DELAY);
    }

    private final View.OnClickListener mButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCurrentMessage != null && mCurrentMessage.mClickListener != null) {
                mCurrentMessage.mClickListener.onMessageClick(mCurrentMessage.mToken);
                mCurrentMessage = null;
                mHandler.removeCallbacks(mHideRunnable);
                mHideRunnable.run();
            }
        }
    };

    public void cancelAll() {
        mMessages.clear();
        if (mShowing) {
            hideMessageBar();
        }
        mHandler.removeCallbacks(mHideRunnable);
    }

    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mContainer.startAnimation(mFadeOutAnimation);
        }
    };

    private class Message {
        final String mMessage;
        final int mType;
        final String mActionMessage;
        final int mActionIcon;
        final Parcelable mToken;
        final OnMessageClickListener mClickListener;
        final OnMessageDisappearWithoutClickListener mDisappearListener;
        final int mContainer;

        public Message(String message, int type, String actionMessage, int actionIcon, Parcelable token, OnMessageClickListener clickListener, OnMessageDisappearWithoutClickListener disappearListener, int container) {
            mMessage = message;
            mType = type;
            mActionMessage = actionMessage;
            mActionIcon = actionIcon;
            mToken = token;
            mClickListener = clickListener;
            mDisappearListener = disappearListener;
            mContainer = container;
        }
    }

    private int getBackgroundResource(int type) {
        switch (type) {
            case WARNING:
                return R.drawable.mb__messagebar_background_orange;
            case ERROR:
                return R.drawable.mb__messagebar_background_red;
            default:
                return R.drawable.mb__messagebar_background_gray;
        }
    }
}
