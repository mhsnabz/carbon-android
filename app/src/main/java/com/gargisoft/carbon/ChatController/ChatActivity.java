package com.gargisoft.carbon.ChatController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.gargisoft.carbon.R;

public class ChatActivity extends AppCompatActivity {
    private Boolean isShow = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    public void mediaClik(View view) {
        toggle(true);
    }

    private void toggle(boolean show) {
        CardView toolbar = (CardView)findViewById(R.id.mediaToolbar);
        ViewGroup parent = findViewById(R.id.parent);
        Transition transition = new Slide(Gravity.BOTTOM);
        transition.setDuration(300);
        transition.addTarget(R.id.mediaToolbar);

        TransitionManager.beginDelayedTransition(parent, transition);
        toolbar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
