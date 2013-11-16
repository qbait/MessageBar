package net.simonvt.messagebar.samples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import net.simonvt.messagebar.MessageBar;

public class SampleActivity extends Activity {
    private int mCount;

    @Override
    protected void onPause() {
        super.onPause();
        MessageBar.getInstance().cancelAll();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findViewById(R.id.withText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageBar.getInstance().show(SampleActivity.this, "Message #" + mCount, MessageBar.INFO, null, 0, null, null, null, android.R.id.content);
                mCount++;
            }
        });

        findViewById(R.id.withTextAndButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageBar messageBar = MessageBar.getInstance();
                MessageBar.OnMessageClickListener clickListener = new MessageBar.OnMessageClickListener() {
                    @Override
                    public void onMessageClick(Parcelable token) {
                        Toast.makeText(SampleActivity.this, "click", Toast.LENGTH_SHORT).show();
                    }
                };
                MessageBar.OnMessageDisappearWithoutClickListener disappearWithoutClickListener = new MessageBar.OnMessageDisappearWithoutClickListener() {
                    @Override
                    public void onMessageDisappearWithoutClick(Parcelable token) {
                        Toast.makeText(SampleActivity.this, "whithout click", Toast.LENGTH_SHORT).show();
                    }
                };
                messageBar.show(SampleActivity.this, "Message #" + mCount, MessageBar.ERROR, "Button!", R.drawable.ic_messagebar_undo, null, clickListener, disappearWithoutClickListener, R.id.whiteContainer);
                mCount++;
            }
        });

        findViewById(R.id.newActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SampleActivity.this, NewActivity.class);
                startActivity(intent);
            }
        });
    }
}
