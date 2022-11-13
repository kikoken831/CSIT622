package Implementation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AlertEngine {

    Event[] events;
    int days;
    Stats[] stats;

    int weights;

    int sumOfWeights;

    public AlertEngine(Event[] events, int days) {
        this.events = events;
        this.days = days;
        try {
            this.stats = ReadEventStatsFile("Baseline.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (Event e : events
        ) {
            this.weights += e.getEventWeight();
        }
        this.sumOfWeights = this.weights * 2;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public Stats[] getStats() {
        return stats;
    }

    public void setStats(Stats[] stats) {
        this.stats = stats;
    }


    private static Stats[] ReadEventStatsFile(String filename) throws FileNotFoundException {
        Scanner file = new Scanner(new FileReader(filename));
        String numLine = file.nextLine();
        int stats = Integer.parseInt(numLine);
        Stats[] statsTypes = new Stats[stats];
        for (int i = 0; i < stats; i++) {
            String line = file.nextLine();
            String[] part = line.split(":");
            String eventName = part[0];
            String mean = part[1];
            String std = part[2];
            double meanValue = Double.parseDouble(mean);
            double stdValue = Double.parseDouble(std);
            statsTypes[i] = new Stats(eventName, meanValue, stdValue);

        }
        return statsTypes;
    }

    @Override
    public String toString() {
        return "AlertEngine{" +
                "events=" + Arrays.toString(events) +
                ", days=" + days +
                ", stats=" + Arrays.toString(stats) +
                '}';
    }

    public void generateAlerts() {
        for (int i = 0; i < this.days; i++) {
            try {
                Double anomalyScore = 0.0;
                System.out.println("==========Day " + i + " Anomaly threshold: " + this.sumOfWeights + "==========");
                Scanner file = new Scanner(new FileReader("AnalysedLiveActivityLog" + i + ".txt"));
                while (file.hasNextLine()) {
                    String line = file.nextLine();
                    String[] part = line.split(":");
                    String eventName = part[0];
                    String value = part[1];
                    for (Event e : this.events
                    ) {
                        for (Stats s : this.stats
                        ) {
                            if (e.getEventName().equals(s.getEventName()) && e.getEventName().equals(eventName)) {

                                double valueDouble = Double.parseDouble(value);
                                double delta = Math.abs(valueDouble - s.getMean());
                                System.out.println("Event: " + eventName + " Value: " + valueDouble + " Mean: " + s.getMean() + " Delta/Anomaly Counter: " + delta + " Std: " + s.getStandardDeviation());
                                if (delta > s.getStandardDeviation()) {
                                    anomalyScore += e.getEventWeight() * (delta/s.getStandardDeviation());
                                }
                            }
                        }
                    }
                }
                System.out.println("Day " + i + " Anomaly score: " + anomalyScore);
                if (anomalyScore > this.sumOfWeights) {
                    System.out.println("Day " + i + " Anomaly score: " + anomalyScore + " is greater than the threshold: " + this.sumOfWeights + " Anomaly detected");
                } else {
                    System.out.println("Day " + i + " Anomaly score: " + anomalyScore + " is less than the threshold: " + this.sumOfWeights + " No anomaly detected");
                }
                System.out.println("==========End of Day " + i + "==========");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
