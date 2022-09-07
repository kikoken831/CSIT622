import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

public class AnalysisEngine 
{
    EventType[] eventStats;
    
    //analyse files using event types
    public AnalysisEngine(EventType[] eventTypes )
    {
        this.eventStats = eventTypes;
    }
    
    //count events for day from a file
    public double [] countDayEvents(String filename) throws FileNotFoundException
    {
        double eventCounter[] = new double [eventStats.length];

        Scanner file = new Scanner(new FileReader(filename));
        while(file.hasNextLine())
        {
            String line = file.nextLine();
            String[] part = line.split(":");
            
			if(part.length!=3)
                continue;

            String event = part[1];
            double value = Double.parseDouble(part[2]);
            for(int j = 0; j < eventStats.length; j++)
            {
				if(eventStats[j].getName().equals(event))
					eventCounter[j] = eventCounter[j] + value;
			}
        }
        return eventCounter;
    }
    
    //process logs for multiple days, 
    //returns new event stats calculated on these logs
    public EventType[] processLogs(int days, String prefix) throws FileNotFoundException
    {
        List <double[]> totalEventCounts = new ArrayList <>();
        for(int i = 0; i < days; i++)
        {
            double eventCounter[] = countDayEvents(prefix + " " + i + ".txt");
            totalEventCounts.add(eventCounter);
            
            //print out even total for each day
            PrintWriter output = new PrintWriter (new File ("total " + prefix + " " + i + ".txt"));
                for(int j = 0 ; j < eventStats.length; j++)
					output.printf ("%s:%.2f%n", eventStats[j].getName(), eventCounter[j]); 
            output.close();
        }
        
       
        EventType[] processedEvents = EventType.cloneEvents(eventStats);
		PrintWriter totalOutput = new PrintWriter(new File("processed " + prefix + ".txt"));
		for(int j = 0; j < eventStats.length; j++)
		{
			//calculating mean & storing it
			double total = 0;
			for(int i = 0; i < days; i++)
				total = total + totalEventCounts.get(i)[j];

			double mean = total/days;
			processedEvents[j].setMean (mean);
			
			//calculate Standard Deviation & storing it
			double sumDiff = 0;
			for(int i=0; i < days; i++)
				sumDiff += (totalEventCounts.get(i)[j] - mean) * (totalEventCounts.get(i)[j] - mean);

			double std = Math.sqrt (sumDiff / days);
			processedEvents[j].setStd (std);
			// write the output in the file
			totalOutput.printf("%s:%.2f:%.2f%n",eventStats[j].getName(), mean ,std);	
		}
		totalOutput.close();
		return processedEvents;
    }    
}