package com.example.progressionbar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    // UI elements
    Button buttonStart, buttonReset;
    ProgressBar progressBar1, progressBar2,
            progressBar3, progressBar4, progressBar5;

    // Threads for counting progress
    CountThread countThread1, countThread2,
            countThread3, countThread4, countThread5;

    // ExecutorService to manage thread execution
    ExecutorService executorService = null;

    // EditText to display the percentage progress
    EditText percentageEditText;

    // Counter for completed progress bars
    int completedBars = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        buttonStart = findViewById(R.id.start);
        buttonReset = findViewById(R.id.reset);
        progressBar1 = findViewById(R.id.progressbar1);
        progressBar2 = findViewById(R.id.progressbar2);
        progressBar3 = findViewById(R.id.progressbar3);
        progressBar4 = findViewById(R.id.progressbar4);
        progressBar5 = findViewById(R.id.progressbar5);
        percentageEditText = findViewById(R.id.editTextPercentage);

        // Set click listener for the start button
        buttonStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create CountThread instances for each ProgressBar
                countThread1 = new CountThread(progressBar1);
                countThread2 = new CountThread(progressBar2);
                countThread3 = new CountThread(progressBar3);
                countThread4 = new CountThread(progressBar4);
                countThread5 = new CountThread(progressBar5);

                // Initialize and execute threads using an ExecutorService
                executorService = Executors.newFixedThreadPool(1);
                executorService.execute(countThread1);
                executorService.execute(countThread2);
                executorService.execute(countThread3);
                executorService.execute(countThread4);
                executorService.execute(countThread5);
            }
        });

        // Set click listener for the reset button
        buttonReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset progress bars and update the percentage
                resetProgressBars();
            }
        });
    }

    // Custom Thread class for counting progress and updating ProgressBar
    public class CountThread extends Thread {

        // ProgressBar associated with the thread
        ProgressBar progressBar;
        final int MAX_PROGRESS = 10;
        int progress;

        // Constructor to associate a ProgressBar with the thread
        CountThread(ProgressBar progressBar) {
            this.progressBar = progressBar;
            progress = 0;
        }

        @Override
        public void run() {
            // Loop to simulate progress updates
            for (int i = 0; i < MAX_PROGRESS; i++) {
                progress++;

                // Update ProgressBar on the main UI thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(progress);
                        updatePercentage();
                    }
                });

                try {
                    // Simulate work being done
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // When the progress is complete, increment the completedBars counter
            completedBars++;
            updatePercentage();
        }
    }

    // Method to reset progress bars and update the percentage
    private void resetProgressBars() {
        progressBar1.setProgress(0);
        progressBar2.setProgress(0);
        progressBar3.setProgress(0);
        progressBar4.setProgress(0);
        progressBar5.setProgress(0);

        // Reset the completedBars counter
        completedBars = 0;
        updatePercentage();
    }

    // Method to update the percentage in the EditText
    private void updatePercentage() {
        // Calculate the total percentage based on the number of completed bars
        int totalBars = 5; // Assuming there are 5 progress bars
        int percentage = (completedBars * 100) / totalBars;

        // Update the EditText with the calculated percentage
        percentageEditText.setText(percentage + "%");
    }
}
