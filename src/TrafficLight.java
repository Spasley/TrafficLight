import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TrafficLight implements Runnable {

    //Let's hide some values which user should not change
    //get unix time start when thread (traffic light) started
    private long trafficLightStarted = System.currentTimeMillis() / 1000L;
    private volatile boolean isRunning = true;
    private enum COLORS {GREEN, YELLOW, RED}

    public void run() {
        //Some instructions for user
        System.out.println("Hello! Please input minutes value and application will return current traffic light color. " +
                "Input \"Exit\" for exit");
        while (isRunning) {
            //BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
            try (BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in))) {
                String inputFromUser = inputReader.readLine();
                //Allow user escape the app gracefully
                while (!inputFromUser.toLowerCase().equals("exit")) {
                    //Check if input could be recognized as 'minutes'. Allow use partial values.
                    Double minutesFromUserInput = Double.parseDouble(inputFromUser);
                    //Can't return light for period when Traffic Light was disabled
                    if (minutesFromUserInput < 0) {
                        System.out.println("Please input positive numeric value or input \"Exit\" for exit.");
                    }
                    else {
                        //Get minutes passed from Traffic Light start to user's requested minute
                        int minutesPassed = getMinutesDiff(minutesFromUserInput);
                        //Seems reasonable for me to see into last  digit of minutes diff to dedicate current light
                        int requestedMinuteOfPeriod = minutesPassed % 10;
                        outputColor(requestedMinuteOfPeriod);
                    }
                    //Allow another input without app restart
                    inputFromUser = inputReader.readLine();
                }
                //Shut down thread if user input == 'exit'
                inputReader.close();
                Thread.currentThread().interrupt();
                return;
            //Do not shut down app if non-numeric values entered
            } catch (IOException | NumberFormatException exc) {
                System.out.println("Please input positive numeric value or input \"Exit\" for exit.");
            }
        }
    }

    public int getMinutesDiff(Double requestedMinutes) {
        //Calculating how many minutes passed from Traffic Light start to user's requested minute
        long timestampOfRequestedMinute = trafficLightStarted + (long) Math.floor(requestedMinutes) * 60;
        long diff = timestampOfRequestedMinute - trafficLightStarted;
        return (int) diff / 60;
    }

    public void outputColor(int minute) {
        if (minute == 0 || minute == 1) {
            System.out.println(COLORS.GREEN);
        } else if (minute == 2 || minute == 3 || minute == 4) {
            System.out.println(COLORS.YELLOW);
        } else {
            System.out.println(COLORS.RED);
        }
    }

}
