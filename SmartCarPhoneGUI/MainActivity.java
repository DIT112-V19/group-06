package se.gu.student.smartcargui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class MainActivity extends Activity implements View.OnClickListener {

    ImageButton frontButton;
    ImageButton backButton;
    Button stopButton;
    SeekBar speedBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frontButton = (ImageButton)findViewById(R.id.frontButton);
        frontButton.setOnClickListener(this);

        backButton = (ImageButton)findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        stopButton = (Button)findViewById(R.id.stopButton);
        stopButton.setOnClickListener(this);

        speedBar = (SeekBar)findViewById(R.id.speedBar);
        speedBar.setMax(100); //Max of speedBar is 100%
        speedBar.setProgress(20); //Default speedBar is at 20%
        }

    @Override
    public void onClick(View view) {

    }
}
