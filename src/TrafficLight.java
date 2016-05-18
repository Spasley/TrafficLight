import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TrafficLight implements Runnable {

    //Let's hide some values which user should not change
    //get unix time start when thread (traffic light) started
    private long traffic_light_started = System.currentTimeMillis() / 1000L;
    private volatile boolean isRunning = true;
    private enum COLORS {GREEN, YELLOW, RED}

    public void run() {
        //Some instructions for user
        System.out.println("Hello! Please input minutes value and application will return current traffic light color. " +
                "Input \"Exit\" for exit");
        while (isRunning) {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
            try {
                String input_from_user = inputReader.readLine();
                //Allow user escape the app gracefully
                while (!input_from_user.toLowerCase().equals("exit")) {
                    //Check if input could be recognized as 'minutes'. Allow use partial values.
                    Double minutes_from_user_input = Double.parseDouble(input_from_user);
                    //Can't return light for period when Traffic Light was disabled
                    if (minutes_from_user_input < 0) {
                        System.out.println("Please input positive numeric value or input \"Exit\" for exit.");
                    }
                    else {
                        //Get minutes passed from Traffic Light start to user's requested minute
                        int minutesPassed = getMinutesDiff(minutes_from_user_input);
                        //Seems reasonable for me to see into last digit to dedicate current light
                        int minute = minutesPassed % 10;
                        if (minute == 0 || minute == 1) {
                            System.out.println(COLORS.GREEN);
                        } else if (minute == 2 || minute == 3 || minute == 4) {
                            System.out.println(COLORS.YELLOW);
                        } else {
                            System.out.println(COLORS.RED);
                        }
                    }
                    //Allow another input without app restart
                    input_from_user = inputReader.readLine();
                }
                //Shut down thread if user input == 'exit'
                Thread.currentThread().interrupt();
                return;
            //Do not shut down app if non-numeric values entered
            } catch (IOException | NumberFormatException exc) {
                System.out.println("Please input positive numeric value or input \"Exit\" for exit.");
            }

        }
    }

    public int getMinutesDiff(Double requested_minutes) {
        //Calculating how many minutes passed from Traffic Light start to user's requested minute
        long timestampOfRequestedMinute = traffic_light_started + (long) Math.floor(requested_minutes) * 60;
        long diff = timestampOfRequestedMinute - traffic_light_started;
        return (int) diff / 60;
    }
}
