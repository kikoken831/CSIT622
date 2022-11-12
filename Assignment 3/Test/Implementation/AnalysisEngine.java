package Implementation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AnalysisEngine {
    Event[] events;
    int days;

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

    public AnalysisEngine(int days, Event[] events) {
        this.events = events;
        this.days = days;
    }

    public void generateAllStats() {
        for (int i = 0; i < this.days; i++) {
            try {
                generateStats("BaseActivityLog" + i + ".txt");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // This method is to read the log file and generate the stats for each event
    public void generateStats(String fileName) throws FileNotFoundException {
        //map of all event name as key in map
        Map<String, Double> eventMap = new HashMap<>();

        for (Event event : events) {
            eventMap.put(event.getEventName(), 0.0);
        }

        //read the log file
        Scanner file = new Scanner(new FileReader(fileName));
        while (file.hasNextLine()) {
            String line = file.nextLine();
            String[] part = line.split(":");
            String eventName = part[1];
            String eventValue = part[2];
            for (Event e : events
            ) {
                if (e.getEventName().equals(eventName)) {
                    double temp = eventMap.get(eventName) + (Double.parseDouble(eventValue));
                    eventMap.put(eventName, temp);
                }
            }
        }
        //write the stats to a file
        try {
            PrintWriter output = new PrintWriter("Analysed" + fileName);
            for (Event e : events) {
                if (e.getEventType() == 'C') {
                    output.println(e.getEventName() + ":" + eventMap.get(e.getEventName()));
                } else {
                    output.println(e.getEventName() + ":" + eventMap.get(e.getEventName()).intValue());
                }
            }
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void computeTotalStats() {
        //aggregate event list
        //map of all event name as key in map
        Map<String, Double> eventMap = new HashMap<>();
        Map<String, Double> meanEventMap = new HashMap<>();
        Map<String, Double> stdDevMap = new HashMap<>();
        for (Event event : events) {
            eventMap.put(event.getEventName(), 0.0);
        }
        //generate mean for each event
        for (int i = 0; i < this.days; i++) {
            try {

                meanEventMap = computeTotal("AnalysedBaseActivityLog" + i + ".txt", eventMap);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        //update map values to mean
        for (Event e : events
        ) {
            meanEventMap.put(e.getEventName(), eventMap.get(e.getEventName()) / this.days);
        }
        //generate std dev for each event
        for (int i = 0; i < this.days; i++) {
            try {
                stdDevMap = computeStdDev("AnalysedBaseActivityLog" + i + ".txt", meanEventMap, stdDevMap);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        //update map values to std dev
        for (Event e : events
        ) {
            stdDevMap.put(e.getEventName(), Math.sqrt((stdDevMap.get(e.getEventName()) / this.days)));
        }


        //write the stats to a file
        try {
            PrintWriter output = new PrintWriter("Baseline.txt");
            for (Event e : events) {
                if (e.getEventType() == 'C') {
                    output.println(e.getEventName() + ":" + Math.round(meanEventMap.get(e.getEventName()) * 100.0) / 100.0 + ":" + Math.round(stdDevMap.get(e.getEventName()) * 100.0) / 100.0);
                    System.out.println("Event Name: "+ e.getEventName() + " Mean :" + Math.round(meanEventMap.get(e.getEventName()) * 100.0) / 100.0  + " Standard Deviation:"+ Math.round(stdDevMap.get(e.getEventName()) * 100.0) / 100.0);
                } else {
                    output.println(e.getEventName() + ":" + Math.round(meanEventMap.get(e.getEventName()) * 100.0) / 100.0  + ":" + + Math.round(stdDevMap.get(e.getEventName()) * 100.0) / 100.0);
                    System.out.println("Event Name: "+ e.getEventName() + " Mean :" + Math.round(meanEventMap.get(e.getEventName()) * 100.0) / 100.0  + " Standard Deviation:"+ Math.round(stdDevMap.get(e.getEventName()) * 100.0) / 100.0);
                }
            }
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private Map<String, Double> computeTotal(String fileName, Map<String, Double> totalEventStats) throws FileNotFoundException {
        //read the log file
        Map<String, Double> meanMap = new HashMap<>();
        Scanner file = new Scanner(new FileReader(fileName));
        while (file.hasNextLine()) {
            String line = file.nextLine();
            String[] part = line.split(":");
            String eventName = part[0];
            String eventValue = part[1];
            for (Event e : events
            ) {
                if (e.getEventName().equals(eventName)) {
                    double temp = totalEventStats.get(eventName) + (Double.parseDouble(eventValue));
                    totalEventStats.put(eventName, temp);
                    meanMap.put(eventName, temp);
                }
            }
        }
        return meanMap;
    }

    private Map<String, Double> computeStdDev(String fileName, Map<String, Double> totalEventMean, Map<String, Double> stdDevMap) throws FileNotFoundException {
        //read the log file
        Scanner file = new Scanner(new FileReader(fileName));
        while (file.hasNextLine()) {
            String line = file.nextLine();
            String[] part = line.split(":");
            String eventName = part[0];
            String eventValue = part[1];
            for (Event e : events
            ) {
                if (e.getEventName().equals(eventName)) {
                    double temp = stdDevMap.getOrDefault(eventName, 0.0) + Math.pow((Double.parseDouble(eventValue) - totalEventMean.get(eventName)), 2);
                    stdDevMap.put(eventName, temp);
                }
            }
        }
        return stdDevMap;
    }

}
