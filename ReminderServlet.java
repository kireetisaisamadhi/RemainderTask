package com.inventions;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

  //@WebServlet("/ReminderServlet")
public class ReminderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Override
    protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String day = request.getParameter("day");
        String time = request.getParameter("time");
        String activity = request.getParameter("activity");

        // Schedule the reminder
        scheduleReminder(day, time, activity);
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h2>Reminder Set Successfully!</h2>");
        out.println("<p>Activity: " + activity + "</p>");
        out.println("<p>Day: " + day + "</p>");
        out.println("<p>Time: " + time + "</p>");
        out.close();
    }
    
    private void scheduleReminder(String day, String time, String activity) {
        Timer timer = new Timer();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE HH:mm", Locale.ENGLISH);

        try {
            Date reminderTime = dateFormat.parse(day + " " + time);
            Date currentTime = new Date();
            
            if (reminderTime.before(currentTime)) {
               System.out.println("The time has already passed for today.");
                return;
            }

            long delay = reminderTime.getTime() - currentTime.getTime();
            timer.schedule(new ReminderTask(activity), delay);

        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
        }
    }

    static class ReminderTask extends TimerTask {
        private String activity;

        public ReminderTask(String activity) {
            this.activity = activity;
        }

        public void run() {
            // Play sound (simulated with a print statement for simplicity)
        	 playSound("soundfile.wav");
            System.out.println("Reminder: It's time for " + activity + "!");
            // Ideally, you would play a sound here using an external library
        }
        private void playSound(String soundFilePath) {
            try {
                File soundFile = new File(soundFilePath);
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.out.println("Error playing sound: " + e.getMessage());
            }
    }
}
}
