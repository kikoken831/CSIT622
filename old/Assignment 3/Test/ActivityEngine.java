import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ActivityEngine {
    EventType[] eventStats;

    
    public ActivityEngine(EventType[] eventStats)
    {
        this.eventStats = eventStats;
    }
    
    
    public String generateTimestamp()
    {
        int hour = (int)Math.floor(Math.random() * 24);
        int minute 	= (int)Math.floor(Math.random() * 60);
        int second = (int)Math.floor(Math.random() * 60);
        return String.format("%02d-%02d-%02d", hour,minute,second);
    }
    
    // generate base day logs
    void generateLogs(int days, String prefix) throws FileNotFoundException
    {
        for(int i = 0; i < days; i++)
        {
            List <String> eventLog = new ArrayList <String>();
            
            for(int j = 0; j < eventStats.length; j++)
            {
                if(eventStats[j].getType() == 'C')
                {
                    
                    double value = eventStats[j].getRandValue();
                    eventLog.add(generateTimestamp() + ":"
					+ eventStats[j].getName() + ":"
					+ String.format("%.2f", value));
                }
                else if(eventStats[j].getType() == 'D')
                {
                    int events = (int)eventStats[j].getRandValue();
                    
                    for(int k = 0; k < events; k++)
                        eventLog.add(generateTimestamp() + ":" + eventStats[j].getName() + ":" + 1);
                }
            }
            
            //print each line to each file
			PrintWriter output = new PrintWriter(new File(prefix + " " + i + ".txt"));
            for(String line  : eventLog)
                output.println(line); 
            output.close();
        }
    }
}