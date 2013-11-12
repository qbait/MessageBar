package net.simonvt.messagebar.samples;

import net.simonvt.messagebar.MessageBar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class SampleActivity extends Activity implements MessageBar.OnMessageClickListener, MessageBar.OnMessageDisappearWithoutClickListener {

    private static final String STATE_MESSAGEBAR = "net.simonvt.messagebar.samples.SampleActivity.messageBar";
    private static final String STATE_COUNT = "net.simonvt.messagebar.samples.SampleActivity.count";

    private MessageBar mMessageBar;

    private TextView mTextView;

    private int mCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mTextView = (TextView) findViewById(R.id.messageClickedTextView);

        mMessageBar = new MessageBar(this);
        mMessageBar.setOnClickListener(this);
        mMessageBar.setOnDisappearListener(this);

        findViewById(R.id.withText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessageBar.show("Message #" + mCount, MessageBar.INFO);
                mCount++;
            }
        });

        findViewById(R.id.withTextAndButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putInt("count", mCount);
                mMessageBar.show("Message #" + mCount, MessageBar.ERROR, "Button!", R.drawable.ic_messagebar_undo, b);
                mCount++;
            }
        });

        findViewById(R.id.newActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessageBar.clear();
                Intent intent = new Intent(SampleActivity.this, NewActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        mMessageBar.onRestoreInstanceState(inState.getBundle(STATE_MESSAGEBAR));
        mCount = inState.getInt(STATE_COUNT);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(STATE_MESSAGEBAR, mMessageBar.onSaveInstanceState());
        outState.putInt(STATE_COUNT, mCount);
    }

    @Override
    public void onMessageClick(Parcelable token) {
        Bundle b = (Bundle) token;
        final int count = b.getInt("count");
        mTextView.setText("You clicked message #" + count);
        Log.d("dupa", "onMessageClick");
    }

    @Override
    public void onMessageDisappearWithoutClick(Parcelable token) {
        Log.d("dupa", "onMessageDisappearWithoutClick");
    }
}
