package dk.ak.timertime;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    LinearLayout timersLayout;
    EditText timerNameInput, hoursInput, minutesInput, secondsInput;
    Button startButton, addPopcornButton;

    ArrayList<CountDownTimer> timerList = new ArrayList<>();

    // MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timersLayout = findViewById(R.id.timersLayout);
        timerNameInput = findViewById(R.id.timerNameInput);
        hoursInput = findViewById(R.id.hoursInput);
        minutesInput = findViewById(R.id.minutesInput);
        secondsInput = findViewById(R.id.secondsInput);
        startButton = findViewById(R.id.startButton);
        addPopcornButton = findViewById(R.id.addPopcornButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
            }
        });

        addPopcornButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPopcornTimer();
            }
        });

        // Initialize MediaPlayer for playing a sound
        // mediaPlayer = MediaPlayer.create(this, R.raw.la_vaguelette);
    }

    void startTimer() {
        String timerName = timerNameInput.getText().toString();
        if (timerName.isEmpty()) {
            timerName = "Nameless Timer";
        }

        int hours = parseEditTextValue(hoursInput);
        int minutes = parseEditTextValue(minutesInput);
        int seconds = parseEditTextValue(secondsInput);

        long totalTime = (hours * 3600 + minutes * 60 + seconds) * 1000;

        // Store the original input for the timer
        TimerInput originalInput = new TimerInput(timerName, hours, minutes, seconds);

        CountDownTimer timeTimer = createNewTimer(originalInput, totalTime);
        timerList.add(timeTimer);
    }

    void addPopcornTimer() {
        String timerName = "Popcorn";
        int hours = 0;
        int minutes = 3;
        int seconds = 20;

        long totalTime = (hours * 3600 + minutes * 60 + seconds) * 1000;

        // Store the original input for the timer
        TimerInput originalInput = new TimerInput(timerName, hours, minutes, seconds);

        CountDownTimer popcornTimer = createNewTimer(originalInput, totalTime);
        timerList.add(popcornTimer);
    }

    int parseEditTextValue(EditText editText) {
        String valueStr = editText.getText().toString();
        if (!valueStr.isEmpty()) {
            return Integer.parseInt(valueStr);
        }
        return 0;
    }

    CountDownTimer createNewTimer(final TimerInput originalInput, long time) {
        final LinearLayout timerLayout = new LinearLayout(this);
        timersLayout.addView(timerLayout);

        final TextView timerNameTextView = new TextView(this);
        //timerNameTextView.setText("Name: " + originalInput.timerName);
        timerLayout.addView(timerNameTextView);

        final TextView timestamp = new TextView(this);
        timerLayout.addView(timestamp);

        Button cancelButton = new Button(this);
        cancelButton.setText("Cancel");
        timerLayout.addView(cancelButton);

        Button resetButton = new Button(this);
        resetButton.setText("Reset");
        timerLayout.addView(resetButton);

        CountDownTimer timeTimer = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                long hoursRemaining = secondsRemaining / 3600;
                long minutesRemaining = (secondsRemaining % 3600) / 60;
                long seconds = secondsRemaining % 60;

                String timerDisplay = "Name: " + originalInput.timerName + "\n" +
                        hoursRemaining + "h " +
                        minutesRemaining + "m " +
                        seconds + "s";
                timerNameTextView.setText(timerDisplay);
            }


            public void onFinish() {
                /*
                // Check if mediaPlayer is not null before calling start
                if (mediaPlayer != null) {
                    // Play a sound when the timer hits 0h 0m 0s
                    mediaPlayer.start();
                }
                */

                // Display timer details on three lines
                timerNameTextView.setText("Name: " + originalInput.timerName);
                timestamp.setText(" Done! ❤");

                /*
                // Release the MediaPlayer after the sound has finished playing
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.release();
                    }
                });
                */
            }
        };

        setupTimerButtons(timeTimer, timerLayout, cancelButton, resetButton, originalInput);

        timeTimer.start();
        return timeTimer;
    }

    void setupTimerButtons(final CountDownTimer timer, final LinearLayout timerLayout, Button cancel, Button reset, final TimerInput originalInput) {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                if (mediaPlayer != null) {
                    // Stop the MediaPlayer only if it is not null and not released
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                */

                timer.cancel();
                timersLayout.removeView(timerLayout);
                timerList.remove(timer);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                if (mediaPlayer != null) {
                    // Stop the MediaPlayer only if it is not null and not released
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                */

                timer.cancel();
                timersLayout.removeView(timerLayout);
                timerList.remove(timer);

                // Start a new timer using the original input
                CountDownTimer newTimer = createNewTimer(originalInput, originalInput.getTotalTime());
                timerList.add(newTimer);
            }
        });
    }

    // Class to hold the original input for each timer
    private static class TimerInput {
        String timerName;
        int hours;
        int minutes;
        int seconds;

        TimerInput(String timerName, int hours, int minutes, int seconds) {
            this.timerName = timerName;
            this.hours = hours;
            this.minutes = minutes;
            this.seconds = seconds;
        }

        long getTotalTime() {
            return (hours * 3600 + minutes * 60 + seconds) * 1000;
        }
    }
}
