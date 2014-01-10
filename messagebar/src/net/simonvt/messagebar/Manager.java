package net.simonvt.messagebar;

import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import java.util.LinkedList;

class Manager {
    private static final int ANIMATION_DURATION = 600;

    private static Manager mInstance;
    private View mContainer;
    private TextView mTextView;
    private TextView mButton;
    private LinkedList<MessageBar> mMessageBars = new LinkedList<MessageBar>();
    private MessageBar mCurrentMessageBar;
    private boolean mShowing;
    private Handler mHandler;
    private AlphaAnimation mFadeInAnimation;
    private AlphaAnimation mFadeOutAnimation;
    boolean mIsButtonClicked = false;

    public static Manager getInstance() {
        if (mInstance == null) {
            mInstance = new Manager();
        }
        return mInstance;
    }

    private Manager() {
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
        View mbContainer = mCurrentMessageBar.mActivity.findViewById(R.id.mbContainer);
        ((ViewGroup) mbContainer.getParent()).removeView(mbContainer);

        if (!mIsButtonClicked && mCurrentMessageBar != null && mCurrentMessageBar.mMessageBarCallback != null) {
            mCurrentMessageBar.mMessageBarCallback.onDisappearWhithoutMessageClick();
        } else {
            mIsButtonClicked = false;
        }

        MessageBar nextMessageBar = mMessageBars.poll();

        if (nextMessageBar != null) {
            show(nextMessageBar);
        } else {
            mCurrentMessageBar = null;
            mShowing = false;
        }
    }

    public void add(MessageBar messageBar) {
        if (mShowing) {
            mMessageBars.add(messageBar);
        } else {
            show(messageBar);
        }
    }

    private void show(MessageBar messageBar) {
        View v = messageBar.mActivity.getLayoutInflater().inflate(R.layout.mb__messagebar, messageBar.mViewGroup);
        mContainer = v.findViewById(R.id.mbContainer);

        mTextView = (TextView) v.findViewById(R.id.mbMessage);
        mButton = (TextView) v.findViewById(R.id.mbButton);
        mButton.setOnClickListener(mButtonListener);

        mShowing = true;
        mContainer.setVisibility(View.VISIBLE);
        mContainer.setBackgroundResource(messageBar.mBackgroundRes);
        mCurrentMessageBar = messageBar;
        mTextView.setText(messageBar.mMessage);
        if (messageBar.mActionMessage != null) {
            mTextView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            mButton.setVisibility(View.VISIBLE);
            mButton.setText(messageBar.mActionMessage);
            mButton.setCompoundDrawablesWithIntrinsicBounds(messageBar.mActionIcon, 0, 0, 0);
        } else {
            mTextView.setGravity(Gravity.CENTER);
            mButton.setVisibility(View.GONE);
        }

        mFadeInAnimation.setDuration(ANIMATION_DURATION);
        mContainer.startAnimation(mFadeInAnimation);
        if( messageBar.mDuration != MessageBar.DURATION_INFINITE ) {
            mHandler.postDelayed(mHideRunnable, messageBar.mDuration);
        }
    }

    private final View.OnClickListener mButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCurrentMessageBar != null && mCurrentMessageBar.mMessageBarCallback != null) {
                mCurrentMessageBar.mMessageBarCallback.onMessageClick();
                mIsButtonClicked = true;
                mHandler.removeCallbacks(mHideRunnable);
                mHideRunnable.run();
            }
        }
    };

    public void cancelAll() {
        for(MessageBar messageBar : mMessageBars) {
            if(messageBar.mMessageBarCallback != null) {
                messageBar.mMessageBarCallback.onDisappearWhithoutMessageClick();
            }
        }
        mMessageBars.clear();
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

}
