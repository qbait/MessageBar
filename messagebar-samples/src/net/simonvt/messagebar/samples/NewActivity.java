package net.simonvt.messagebar.samples;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import net.simonvt.messagebar.MessageBar;
import net.simonvt.messagebar.MessageBarCallback;

public class NewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity);

        findViewById(R.id.withTextAndButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageBarCallback messageMessageBarCallback = new MessageBarCallback() {

                    @Override
                    public void onMessageClick() {
                        Toast.makeText(NewActivity.this, "click", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDisappearWhithoutMessageClick() {
                        Toast.makeText(NewActivity.this, "whithout click", Toast.LENGTH_SHORT).show();
                    }
                };
                MessageBar.showUndo(NewActivity.this, messageMessageBarCallback);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MessageBar.cancelAll();
    }
}
