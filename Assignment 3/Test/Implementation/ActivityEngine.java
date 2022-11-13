package Implementation;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ActivityEngine {
    int days;
    Event[] events;

    public ActivityEngine(int days, Event[] events) {
        this.days = days;
        this.events = events;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }


    @Override
    public String toString() {
        return "ActivityEngine{" +
                "events=" + Arrays.toString(events) +
                '}';
    }

    private String generateActivityTimestamp() {
        int hh = (int) Math.floor(Math.random() * 24);
        int mm = (int) Math.floor(Math.random() * 60);
        int ss = (int) Math.floor(Math.random() * 60);
        String myTime = String.format("%02d-%02d-%02d", hh, mm, ss);
        return myTime;
    }

    public void generateLogs() {
        for (int i = 0; i < this.days; i++) {
            List<String> eventLog = new ArrayList<>();
            for (int j = 0; j < this.events.length; j++) {
                if (this.events[j].getEventType() == 'C') {
                    double value = this.events[j].getValueWithinDistribution();
                    eventLog.add(generateActivityTimestamp() + ":"
                            + this.events[j].getEventName() + ":"
                            + String.format("%.2f", value));
                } else if (this.events[j].getEventType() == 'D') {
                    int events = (int) this.events[j].getValueWithinDistribution();
                    //create k amount of discrete events
                    for (int k = 0; k < events; k++) {
                        eventLog.add(generateActivityTimestamp() + ":" + this.events[j].getEventName() + ":" + 1);
                    }
                }
            }
            //sort the event log
            Collections.sort(eventLog);
            //write each line to file
            try {
                PrintWriter output = new PrintWriter("BaseActivityLog" + i + ".txt");
                for (String line : eventLog)
                    output.println(line);
                output.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public void generateLogs(String customFileName) {
        for (int i = 0; i < this.days; i++) {
            List<String> eventLog = new ArrayList<>();
            for (int j = 0; j < this.events.length; j++) {
                if (this.events[j].getEventType() == 'C') {
                    double value = this.events[j].getValueWithinDistribution();
                    eventLog.add(generateActivityTimestamp() + ":"
                            + this.events[j].getEventName() + ":"
                            + String.format("%.2f", value));
                } else if (this.events[j].getEventType() == 'D') {
                    int events = (int) this.events[j].getValueWithinDistribution();
                    //create k amount of discrete events
                    for (int k = 0; k < events; k++) {
                        eventLog.add(generateActivityTimestamp() + ":" + this.events[j].getEventName() + ":" + 1);
                    }
                }
            }
            //sort the event log
            Collections.sort(eventLog);
            //write each line to file
            try {
                PrintWriter output = new PrintWriter(customFileName + i + ".txt");
                for (String line : eventLog)
                    output.println(line);
                output.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
