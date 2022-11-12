package Implementation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class IDS {
    public static void main(String[] args) throws FileNotFoundException {
        String eventsFilePath = new String();
        String statsFilePath = new String();
        String days = new String();
        int baseDays = 0;
        Event[] initialEvents;
        Stats[] initialStats;
        try {
            eventsFilePath = args[0];
            statsFilePath = args[1];
            days = args[2];
            baseDays = Integer.parseInt(days);
        } catch (Exception e) {
            System.out.println("Error in input parameters.");
            System.out.println("Parameters are as such: IDS Events.txt Stats.txt Days");
            System.exit(-1);
        }
        initialEvents = ReadEventTypesFile(eventsFilePath);
        initialStats = ReadEventStatsFile(statsFilePath);
        if (initialEvents.length != initialStats.length) {
            System.out.println("Error in length for both files.");
            System.exit(-1);
        }
        for (Event e : initialEvents
        ) {
            if (e.validateEvent() == false) {
                System.out.println("Error in Event file.");
                System.exit(-1);
            }
            for (Stats s : initialStats
            ) {
                if (e.getEventName().equals(s.getEventName())) {
                    e.setStats(s);
                }
            }
        }

        System.out.println("IDS is running...");
        System.out.println("Events Logged:");
        for (Event e : initialEvents
        ) {
            System.out.println(e.toString());
        }

        ActivityEngine activityEngine = new ActivityEngine(baseDays, initialEvents);
        System.out.println("Activity Engine created.");
        activityEngine.generateLogs();
        System.out.println("Activity Engine finished.");
        System.out.println("Analysis Engine created.");
        AnalysisEngine analysisEngine = new AnalysisEngine(baseDays, initialEvents);
        System.out.println("==================Analysis Engine Report===================");
        analysisEngine.generateAllStats();
        analysisEngine.computeTotalStats();
        System.out.println("Analysis Engine finished.");
    }

    //file reader for events.txt
    public static Event[] ReadEventTypesFile(String filename) throws FileNotFoundException {
        Scanner file = new Scanner(new FileReader(filename));
        String numLine = file.nextLine();
        int numEvents = Integer.parseInt(numLine);
        Event[] eventTypes = new Event[numEvents];
        for (int i = 0; i < numEvents; i++) {
            String line = file.nextLine();
            String[] part = line.split(":");
            String name = part[0];
            String type = part[1];
            double minVal = 0;
            double maxVal = 0;
            double weightVal = 0;
            String min = part[2];
            if (!min.equals(""))
                minVal = Double.parseDouble(min);
            String max = part[3];
            if (!max.equals(""))
                maxVal = Double.parseDouble(max);
            String weight = part[4];
            if (!weight.equals(""))
                weightVal = Double.parseDouble(weight);
            // make a new event object
            eventTypes[i] = new Event(name, type.charAt(0), minVal, maxVal, weightVal);
        }
        return eventTypes;
    }

    public static Stats[] ReadEventStatsFile(String filename) throws FileNotFoundException {
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

}


