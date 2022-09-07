import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AnalysisEngine {
	
	int days;
	List<EventsObject> EventList;
	List<StatsObject> StatsList;
	double mean;
	double SD;
	int eventCount = 0;
	
	List<StatsObject> outputList = new ArrayList<StatsObject>();
	List<Double> sampleData = new ArrayList<Double>();
	
	//The constructor of the class 
	public AnalysisEngine(int days, List<EventsObject> eventList, List<StatsObject> statsList) {
		super();
		this.days = days;
		EventList = eventList;
		StatsList = statsList;
	}
	
	//Read the log file and calculate the mean and SD
	public void readAnalysis()
	{
		
		System.out.println("Generate log file completed start analysis");
		
		try {
			
			//List<Double> sampleData = new ArrayList<Double>();
			
			for(int j=0; j<EventList.size(); j++)
			{
				//Check the event type
				
				//We read in the event log file by the event type
				if(EventList.get(j).type.equalsIgnoreCase("D"))
				{
					FileInputStream EventIn;
					
					//Write the new log file
					EventIn = new FileInputStream(EventList.get(j).Eventname+".log");
					
					//Create a buffer for read in the line
					BufferedReader buff = new BufferedReader(new InputStreamReader(EventIn));
					
					String strLine;
					
					//Read in the data from the log file
					while ((strLine = buff.readLine()) != null)   {
						
						if(isNumeric(strLine))
						{
							sampleData.add(Double.parseDouble(strLine));
						}
						
					}
					
					//Calculate the mean and standard deviation
					calculateSD(sampleData, days, eventCount);
					eventCount++;
					
					//Create a new stats object for store new mean and standard deviation
					StatsObject newStats = new StatsObject();
					newStats.setEventName(EventList.get(j).Eventname);
					newStats.setMean(mean);
					newStats.setSD(SD);
					
					//Create the new list for new stats object
					outputList.add(newStats);
					
				}
				
				
				else if(EventList.get(j).type.equalsIgnoreCase("C"))
				{
					FileInputStream EventIn;
					
					//Write the new log file
					EventIn = new FileInputStream(EventList.get(j).Eventname+".log");
					
					//Create a buffer for read in the line
					BufferedReader buff = new BufferedReader(new InputStreamReader(EventIn));
					
					String strLine;
					
					//Read in the data from the log file
					while ((strLine = buff.readLine()) != null)   {
						
						if(isNumeric(strLine))
						{
							sampleData.add(Double.parseDouble(strLine));
						}
						
					}
					
					//Calculate the mean and standard deviation
					calculateSD(sampleData, days, eventCount);
					eventCount++;
					
					//Create a new stats object for store new mean and standard deviation
					StatsObject newStats = new StatsObject();
					newStats.setEventName(EventList.get(j).Eventname);
					newStats.setMean(mean);
					newStats.setSD(SD);
					
					//Create the new list for new stats object
					outputList.add(newStats);
					
				}
				
				else if(EventList.get(j).type.equalsIgnoreCase("E"))
				{
					FileInputStream EventIn;
					
					//Write the new log file
					EventIn = new FileInputStream(EventList.get(j).Eventname+".log");
					
					//Create a buffer for read in the line
					BufferedReader buff = new BufferedReader(new InputStreamReader(EventIn));
					
					String strLine;
					
					//Read in the data from the log file
					while ((strLine = buff.readLine()) != null)   {
						
						if(isNumeric(strLine))
						{
							sampleData.add(Double.parseDouble(strLine));
						}
					}
					
					//Calculate the mean and standard deviation
					calculateSD(sampleData, days, eventCount);
					eventCount++;
					
					//Create a new stats object for store new mean and standard deviation
					StatsObject newStats = new StatsObject();
					newStats.setEventName(EventList.get(j).Eventname);
					newStats.setMean(mean);
					newStats.setSD(SD);
					
					//Create the new list for new stats object
					outputList.add(newStats);
								
				}
				
			}
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	//read the line in the file check if it's number
	public boolean isNumeric(String str)
	{
		try  
		  { 
			//Convert the number if there is an exception return false
		    double d = Double.parseDouble(str);  
		  }  
		  catch(NumberFormatException nfe)  
		  {  
		    return false;  
		  }  
		  return true;
	}
	
	//Calculate the mean and standard deviation
	public void calculateSD(List<Double> list, int days, int count)
	{
		double mean = 0.0;
		
		//Calculate the sum of means
		for(int i=days*count; i<days*(count+1); i++)
		{
			mean += list.get(i);
		}
		
		mean = mean/(double)(days);
		
		double SD = 0.0;
		
		//Calculate the standard deviation
		for(int i=days*count; i<days*(count+1); i++)
		{
			SD += (list.get(i)-mean)*(list.get(i)-mean);
		}
		
		SD = Math.sqrt(SD/(double)(days-1));
		
		this.mean = mean;
		
		this.SD = SD;
		
	}
	
	//Create a new stats file for new mean and standard deivation
	public void printNewSD()
	{
		try
		{
			PrintWriter out = new PrintWriter("NewStats.txt");
			
			for(int i=0; i<outputList.size(); i++)
			{		
					out.println(outputList.get(i).eventName+":"+
							new DecimalFormat("##.##").format(outputList.get(i).mean)
							+":"+new DecimalFormat("##.##").format(outputList.get(i).SD));	
			}
					
			out.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//Start the Alter engine for final phase
	public void startAnalysis()
	{
		//Create the alter engine object
		AlterEngine ALE = new AlterEngine(sampleData,EventList,outputList,days);
		
		//calculate threshold
		ALE.calculateThreshold();
		
		//calculate anomaly counter
		ALE.calculateCounter();
	}
	
}
