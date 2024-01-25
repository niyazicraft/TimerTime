package dk.ak.timertime;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    LinearLayout timersLayout;
    EditText hoursInput, minutesInput, secondsInput;
    Button startButton;

    ArrayList<CountDownTimer> timerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timersLayout = findViewById(R.id.timersLayout);
        hoursInput = findViewById(R.id.hoursInput);
        minutesInput = findViewById(R.id.minutesInput);
        secondsInput = findViewById(R.id.secondsInput);
        startButton = findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
            }
        });
    }

    void startTimer() {
        int hours = parseEditTextValue(hoursInput);
        int minutes = parseEditTextValue(minutesInput);
        int seconds = parseEditTextValue(secondsInput);

        long totalTime = (hours * 3600 + minutes * 60 + seconds) * 1000;

        CountDownTimer timeTimer = createNewTimer(totalTime);
        timerList.add(timeTimer);
    }

    int parseEditTextValue(EditText editText) {
        String valueStr = editText.getText().toString();
        if (!valueStr.isEmpty()) {
            return Integer.parseInt(valueStr);
        }
        return 0;
    }

    CountDownTimer createNewTimer(long time) {
        final LinearLayout timerLayout = new LinearLayout(this);
        timersLayout.addView(timerLayout);

        final TextView timestamp = new TextView(this);
        timerLayout.addView(timestamp);

        Button cancelButton = new Button(this);
        cancelButton.setText("Cancel");
        timerLayout.addView(cancelButton);

        Button pauseButton = new Button(this);
        pauseButton.setText("Pause");
        timerLayout.addView(pauseButton);

        Button resetButton = new Button(this);
        resetButton.setText("Reset");
        timerLayout.addView(resetButton);

        CountDownTimer timeTimer = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
                timestamp.setText("Time remaining: " +
                        millisUntilFinished / 1000 / 3600 + "h " +
                        (millisUntilFinished / 1000 % 3600) / 60 + "m " +
                        (millisUntilFinished / 1000 % 60) + "s");
            }

            public void onFinish() {
                timestamp.setText("Done!");
            }
        };

        setupTimerButtons(timeTimer, cancelButton, pauseButton, resetButton);

        timeTimer.start();
        return timeTimer;
    }

    void setupTimerButtons(final CountDownTimer timer, Button cancel, Button pause, Button reset) {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                LinearLayout timerLayout = findTimerLayout(timer);
                if (timerLayout != null) {
                    timersLayout.removeView(timerLayout);
                    timerList.remove(timer);
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add pause functionality here
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                LinearLayout timerLayout = findTimerLayout(timer);
                if (timerLayout != null) {
                    timersLayout.removeView(timerLayout);
                    timerList.remove(timer);
                    startTimer();
                }
            }
        });
    }

    LinearLayout findTimerLayout(CountDownTimer timer) {
        for (int i = 0; i < timersLayout.getChildCount(); i++) {
            View child = timersLayout.getChildAt(i);
            if (child instanceof LinearLayout && timerList.get(i) == timer) {
                return (LinearLayout) child;
            }
        }
        return null;
    }

}
