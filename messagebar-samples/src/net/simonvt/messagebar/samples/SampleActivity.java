package net.simonvt.messagebar.samples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import net.simonvt.messagebar.MessageBar;
import net.simonvt.messagebar.MessageBarCallback;

public class SampleActivity extends Activity {
    private int mCount;

    @Override
    protected void onPause() {
        super.onPause();
        MessageBar.cancelAll();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findViewById(R.id.withText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageBar.show(SampleActivity.this, "MessageBar #" + mCount);
                mCount++;
            }
        });

        findViewById(R.id.withTextAndButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageBarCallback messageMessageBarCallback = new MessageBarCallback() {

                    @Override
                    public void onMessageClick() {
                        Toast.makeText(SampleActivity.this, "click", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDisappearWhithoutMessageClick() {
                        Toast.makeText(SampleActivity.this, "whithout click", Toast.LENGTH_SHORT).show();
                    }
                };

                MessageBar.showUndo(SampleActivity.this, messageMessageBarCallback, R.id.whiteContainer);

                mCount++;
            }
        });

        findViewById(R.id.confirmMessageBar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageBar.showConfirm(SampleActivity.this, "Confirm", MessageBar.STYLE_ERROR);
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
