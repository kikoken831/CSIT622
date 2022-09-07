import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IDS {
	
	public static void main(String args[])
	{
	
		String eventFile = null;
		String statFile = null;
		int days = 0;
		
		//Check program ended or not
		boolean endProgram = false;
		
		if(args.length == 3)
		{
			eventFile = args[0];
			statFile = args[1];
			days = Integer.parseInt(args[2]);
		}
		else
		{
			System.out.println("Please input the correct number of argument");
			System.exit(0);
		}
		
		//Read  Initial input from the document
		InitialReadFile(eventFile,statFile, days);
		
		
		while(!endProgram)
		{
			//Let the user input to continue the program or not
			System.out.println("Enter Quit for end program or Continue:");
			Scanner input = new Scanner(System.in);
			
			if(input.nextLine().equalsIgnoreCase("Quit"))
			{
				endProgram = true;
			}
			else
			{
				//Let the user input the new number of day 
				System.out.println("Please enter the new number of days");
				int newDay =  Integer.parseInt(input.nextLine());
				InitialReadFile(eventFile,statFile, newDay);
			}
		}
		
		System.out.println("End of the System");
		
	}
	
	public static void InitialReadFile(String eventFile, String StatsFile, int days)
	{
		try {
			
			//Read the Event.txt
			FileInputStream EventIn = new FileInputStream(eventFile);
			BufferedReader buff = new BufferedReader(new InputStreamReader(EventIn));
			
			String strLine;
			
			//Create a Event object list to store every event
			List<EventsObject> EventList = new ArrayList<EventsObject>();
			
			
			//Read in information
			while ((strLine = buff.readLine()) != null)   {
				  
				  if(strLine.indexOf(":")>=0)
				  {
					  String separator[] = strLine.split(":");
					  
					  //An event object setup all the variable
					  EventsObject Eobject = new EventsObject();
					  
					  Eobject.setEventname(separator[0]);
					  Eobject.setType(separator[1]);
					  Eobject.setMinimum(separator[2]);
					  Eobject.setMaximum(separator[3]);
					  //Eobject.setUnits(separator[4]);
					  Eobject.setWeight(Integer.parseInt(separator[4]));
					  
					  //Add the object to the list
					  EventList.add(Eobject);
					  
				  }
				
			}
			
			
			//Read in the Stats.txt
			FileInputStream StatsIn = new FileInputStream(StatsFile);
			BufferedReader buffStat = new BufferedReader(new InputStreamReader(StatsIn));
			
			//Create a Stats object list to store event information
			List<StatsObject> StatsList = new ArrayList<StatsObject>();
			
			//Read information
			while ((strLine = buffStat.readLine()) != null)   {
				  
				  if(strLine.indexOf(":")>=0)
				  {
					  String separator[] = strLine.split(":");
					  
					  StatsObject stats = new StatsObject();
					  
					  stats.setEventName(separator[0]);
					  stats.setMean(Double.parseDouble(separator[1]));
					  stats.setSD(Double.parseDouble(separator[2]));
					  
					  StatsList.add(stats);
					  
				  }
				
			}
			
			buff.close();
			buffStat.close();
			
			//Create an activity engine object to start the next phase
			ActivityEngine AE = new ActivityEngine(days, EventList, StatsList);
			
			//Create the log file for base the stats.txt file
			AE.createLog();
			
			//After we create our traffic log file read in the information for analysis
			AnalysisEngine AnE = new AnalysisEngine(days, EventList, StatsList);
			
			//Read in the file
			AnE.readAnalysis();
			
			//Create a new stats file with our new mean and standard deviation
			AnE.printNewSD();
			
			//Start for calculate the threshold and final phase
			AnE.startAnalysis();
			
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
