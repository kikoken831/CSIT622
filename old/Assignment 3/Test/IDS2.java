import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class IDS2 {
    
    //read event file
    static EventType[] ReadEventTypesFile(String filename) throws FileNotFoundException
    {
        Scanner file = new Scanner(new FileReader(filename));
        String numLine = file.nextLine();
        int totalEvents = Integer.parseInt(numLine);
        EventType [] eventTypes = new EventType[totalEvents];
        for(int i = 0; i < totalEvents; i++)
        {
            double weightVal = 0;
            double minVal = 0;
            double maxVal = Double.MAX_VALUE;
			
			String line = file.nextLine();
            String[] part = line.split(":");

            String name = part[0];
            String type = part[1];
            
			String min = part[2];
			if(!min.equals(""))
               minVal = Double.parseDouble(min);
            
			String max = part[3];
			if(!max.equals(""))
                maxVal=Double.parseDouble(max);
				
            String weight = part[3];
			if(!weight.equals(""))
                weightVal=Double.parseDouble(weight);

            // make a new event object
            eventTypes[i] = new EventType(name, type.charAt(0), minVal, maxVal, weightVal);
        }
        return eventTypes;
    }

    //read stats file
    static void ReadEventStatsFile(String filename, EventType[] eventTypes) throws FileNotFoundException
    {
        Scanner file = new Scanner(new FileReader(filename));
        String numLine = file.nextLine();
        int stats = Integer.parseInt(numLine);
        for(int i = 0; i < stats; i++)
        {

            String line = file.nextLine();
            String[] part = line.split(":");

            String eventName = part [0];
            String mean = part [1];
            String std = part [2];

            double meanValue = Double.parseDouble(mean);
            double stdValue = Double.parseDouble(std);

            // insert the mean into the object
            for(int j = 0; j < eventTypes.length; j++)
			{
                if(eventTypes[j].getName().equals(eventName))
                {
                    eventTypes[j].setMean(meanValue);
                    eventTypes[j].setStd(stdValue);
                }
			}
        }
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        EventType[] initialStats;

        if(args.length != 3)
        {
            System.out.println("Usage: IDS Events.txt Stats.txt Days");
            System.exit(-1);
        }

        initialStats = ReadEventTypesFile(args[0]);

        ReadEventStatsFile(args[1], initialStats);

        int baseDays = Integer.parseInt(args[2]);

		// Activity Engine - each base of day
        ActivityEngine baseActivityEngine = new ActivityEngine(initialStats);
        System.out.println ("Generating Events");
        baseActivityEngine.generateLogs (baseDays,"base day");
        System.out.println ("Done");

		// Analysis Engine - total of calculated
        AnalysisEngine baseAnalysisEngine = new AnalysisEngine(initialStats);
        System.out.println("Analyzing Events");
        EventType[] baseStats = baseAnalysisEngine.processLogs(baseDays, "base day");
        System.out.println("Done");

        Scanner input = new Scanner(System.in);

        while(true)
        {
            System.out.println("Enter 'q' to quit or Enter TestStats Days: ");
            String line = input.nextLine();
            if(line.equals("q") || line.equals("Q"))
                break;

            String parts[] = line.split(" ");
			
			
            if(parts.length == 2)
            {
				int newDays = Integer.parseInt(parts[1]);
                EventType[] testStats = EventType.cloneEvents(initialStats);
                ReadEventStatsFile(parts[0], testStats);
 
                ActivityEngine liveActivityEngine = new ActivityEngine(testStats);
                System.out.println("Generating Events ...");
                liveActivityEngine.generateLogs(newDays, "live day");
                System.out.println("Done");

                AlertEngine alertEngine = new AlertEngine(baseStats);

                alertEngine.testAlerts(newDays, "live day");
                
            }
            else
            {
                System.out.println("Please enter the correct format: StatsFile.txt days");
            }
        }
    }
        
}