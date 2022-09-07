import java.io.*;
import java.util.*;

public class IDS {
	
	static String eventFile = "";
	static String statsFile = "";
	static int days = 0;
	
	public static String readFiles()
	{
		//Events File 
		int noOfEvent = 0;
		String eventName = "";
		String eventType = "";
		String min = "";
		String max = "";
		String weight = "";
		try
		{
						
			BufferedReader eventReader = new BufferedReader(new FileReader(eventFile));
			String event = eventReader.readLine();
			noOfEvent = Integer.parseInt(event);
			event = eventReader.readLine();
			while (event != null)
			{
				eventName = event.split(":")[0];
				eventType = event.split(":")[1];
				min = event.split(":")[2];
				max = event.split(":")[3];
				weight = event.split(":")[4];								
				//System.out.println(event);
				System.out.println(eventName + ":" + eventType + ":" + min + ":" + max + ":" + weight + ":" );
				event = eventReader.readLine();
			}
			
		}
		catch (IOException e) 
		{	
			e.printStackTrace();
		}
		
		//StatsFile
		String statsEName = "";
		String mean = "";
		String SD = "";
		try
		{
						
			BufferedReader statsReader = new BufferedReader(new FileReader(statsFile));
			String stats = statsReader.readLine();
			noOfEvent = Integer.parseInt(stats);
			stats = statsReader.readLine();
			while (stats != null)
			{
				statsEName = stats.split(":")[0];
				mean = stats.split(":")[1];
				SD = stats.split(":")[2];
				System.out.println(statsEName + ":" + mean + ":" + SD + ":" );
				stats = statsReader.readLine();
			}
			
		}
		catch (IOException e) 
		{	
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public static void main(String args[]) 
    { 
		//Check if argument has the correct input
        String hashValue = "This is a test"; 
		if (args.length < 3 || args.length > 3)
		{		
			System.out.println("Usage: IDS [EVENT FILE] [STATS FILE] [DAYS]");
			System.exit(0);
		}
		//if arguments has the correct number of input
		else if (args.length == 3)
		{
			eventFile = args[0];
			statsFile = args[1];
			String argDays = args[2];
			days = Integer.parseInt(argDays);
			readFiles();
		}
    } 
}