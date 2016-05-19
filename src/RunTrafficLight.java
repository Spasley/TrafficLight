public class RunTrafficLight {
    public static void main(String[] args) {
        Thread trafficLight = new Thread(new TrafficLight());
        trafficLight.start();
    }
}