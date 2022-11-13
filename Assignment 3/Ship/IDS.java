
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
            System.out.println("Parameters are as such: IDS Events.txt Ship.Stats.txt Days");
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
        AnalysisEngine analysisEngine = new AnalysisEngine(baseDays, initialEvents);
        System.out.println("==================Analysis Engine Report===================");
        analysisEngine.generateAllStats();
        analysisEngine.computeTotalStats();
        System.out.println("Analysis Engine finished.");
        System.out.println("Begin Alert Engine.");

        while (true) {
            System.out.println("Enter 'q' to quit or Enter <List Ship.Stats File path> <No of days> (i.e Ship.Stats.txt 5): ");
            Scanner input = new Scanner(System.in);
            String line = input.nextLine();
            if (line.equals("q") || line.equals("Q"))
                break;
            String parts[] = line.split(" ");
            if (parts.length == 2) {
                int newDays = Integer.parseInt(parts[1]);
                Stats[] testStats = ReadEventStatsFile(parts[0]);
                for (Event e : initialEvents
                ) {
                    for (Stats s : testStats
                    ) {
                        if (e.getEventName().equals(s.getEventName())) {
                            e.setStats(s);
                        }
                    }
                }
                ActivityEngine liveActivityEngine = new ActivityEngine(newDays, initialEvents);
                System.out.println("Generating Events ...");
                liveActivityEngine.generateLogs("LiveActivityLog");
                AnalysisEngine liveAnalysisEngine = new AnalysisEngine(newDays, initialEvents);
                System.out.println("==================Live Analysis Engine Report===================");
                liveAnalysisEngine.generateAllStats("LiveActivityLog");
                AlertEngine alertEngine = new AlertEngine(initialEvents,baseDays);
                alertEngine.generateAlerts();
                System.out.println("Done");

            } else {
                System.out.println("Invalid input try again.");
            }
        }

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
            int weightVal = 0;
            String min = part[2];
            if (!min.equals(""))
                minVal = Double.parseDouble(min);
            String max = part[3];
            if (!max.equals(""))
                maxVal = Double.parseDouble(max);
            String weight = part[4];
            if (!weight.equals(""))
                weightVal = Integer.parseInt(weight);
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


