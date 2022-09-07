import java.io.*;
import java.util.*;
import java.text.DecimalFormat;

class StatsObject {

	String eventName;
	double mean;
	double SD;
	
	public String getEventName() {
		return eventName;
	}
	
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	public double getMean() {
		return mean;
	}
	
	public void setMean(double mean) {
		this.mean = mean;
	}
	
	public double getSD() {
		return SD;
	}
	
	public void setSD(double sD) {
		SD = sD;
	}
	
}

class EventsObject {
	
	String Eventname;
	String type;
	String minimum;
	String maximum;
	int weight;
	
	public String getEventname() {
		return Eventname;
	}
	
	public void setEventname(String eventname) {
		Eventname = eventname;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getMinimum() {
		return minimum;
	}
	
	public void setMinimum(String minimum) {
		this.minimum = minimum;
	}
	
	public String getMaximum() {
		return maximum;
	}
	
	public void setMaximum(String maximum) {
		this.maximum = maximum;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
		
}


class AnalysisEngine {
	
	int days;
	List<EventsObject> EventList;
	List<StatsObject> StatsList;
	double mean;
	double SD;
	int eventCount = 0;
	
	List<StatsObject> outputList = new ArrayList<StatsObject>();
	List<Double> sampleData = new ArrayList<Double>();
	
	//The constructor of the class 
	public AnalysisEngine(int days, List<EventsObject> evList, List<StatsObject> statsList) {
		super();
		this.days = days;
		EventList = evList;
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

class ActivityEngine {
	
	
	int days;
	List<EventsObject> EventList;
	List<StatsObject> StatsList;
	
	Random in = new Random();
	
	//Constructor put the number of days and the list object to the class
	public ActivityEngine(int days, List<EventsObject> evList, List<StatsObject> statsList) {
		super();
		this.days = days;
		EventList = evList;
		StatsList = statsList;
	}

	//Create log file function
	public void createLog()
	{
		System.out.println("Start generate log file ...");
		
		//Create log file for all events
		for(int j=0; j<EventList.size(); j++)
		{
			System.out.println(EventList.get(j).Eventname);
			//Check the event type
			if(EventList.get(j).type.equalsIgnoreCase("D"))
			{
				try{
						//Create a new file for write out all the user generate log
						PrintWriter out = new PrintWriter(EventList.get(j).Eventname+".log");
						boolean done = false;
						int dayCount = 1;
						while(!done){
							//Random generate number of events
							int loginNum = in.nextInt((int)(StatsList.get(j).mean + 2*StatsList.get(j).SD));	//mean + 2*standard deviation + 1
							
							//We set the range of the data from mean +- 2 standard deviation
							if(loginNum >= (int)(StatsList.get(j).mean - 2*StatsList.get(j).SD)){				//mean - 2*standard deviation
								
								//Start to write the number
								out.println("Day" + dayCount);
								//Total recorded number
								out.println(loginNum);	
								for(int i=0;i<loginNum; i++ ){
									out.println(EventList.get(j).Eventname);
								}
								out.println();
								out.println();
								dayCount++;
							}
							//The terminate condition if the day count = the number of day
							if(dayCount-1 == days){
								done = true;
								out.close();
							}
						}
					}catch(IOException e){
					e.printStackTrace();
					}
			}
			
			//Check if the event is the C type
			else if (EventList.get(j).type.equalsIgnoreCase("C"))
			{
				try{
					PrintWriter out = new PrintWriter(EventList.get(j).Eventname+".log");
					boolean done = false;
					int dayCount = 1;
					while(!done){
						//total value per day
						double totalPerDay = 0.0;
						//max value divide mean to obtain average number
						int onlineNum = in.nextInt((int)(Integer.parseInt(EventList.get(j).getMaximum())/StatsList.get(j).mean))+1;	
						{			
							//write day info
							out.println("Day" + dayCount);
							//total recorded number
							out.println("Total Events per day:"+onlineNum);		
							for(int i=0;i<onlineNum;){
								//generate random number less than mean + 2*standard deviation
								double onlineData = in.nextFloat() * (int)(StatsList.get(j).mean + 2*StatsList.get(j).SD);
								if (onlineData >= StatsList.get(j).mean - 2*StatsList.get(j).SD){
									//keep two digital number
									double value = Double.valueOf(String.format("%.2f", onlineData));
									//update total value per day
									totalPerDay += value;
									
									out.println(EventList.get(j).Eventname + value);
									i++;
								}
							}
							out.println();
							//write total value per day
							out.println(String.format("%.2f", totalPerDay/onlineNum));
							out.println();
							dayCount++;
						}
						if(dayCount-1 == days){
							done = true;
							out.close();
						}
					}
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	
}


class AlterEngine {

	List<Double> sampleData;
	List<EventsObject> evList;
	List<StatsObject> outputList;
	List<Double> resultList;
	int threshold;
	int day;
	
	//The constructor of the class
	public AlterEngine(List<Double> sampleData, List<EventsObject> evList, List<StatsObject> outputList, int day) {	
		this.sampleData = sampleData;
		this.evList = evList;
		this.outputList = outputList;
		resultList = new ArrayList<Double>();
		this.day = day;
	}

	//Calculate the threshold
	public void calculateThreshold(){
		
		int sum = 0;
		
		//The sum of the weight
		for(int i=0; i<evList.size(); i++)
		{
			sum += evList.get(i).weight;
		}
		
		int threshold;
		
		threshold = sum*2;
		
		this.threshold = threshold;
		
		System.out.println("The Threshold of the Stats.txt: "+threshold);
	
	}
	
	//Calculate the anomaly counter
	public void calculateCounter()
	{
		int count = 1;
		
		//Calculate the anomaly counter for each
		for(int i=0;i<day;i++){
			int index = i;
			double res = 0;
			for(int j=0;j<evList.size();j++){
				res += (sampleData.get(index+day*j) - outputList.get(j).mean)/outputList.get(j).getSD() * evList.get(j).getWeight();
			}
			resultList.add(res);
		}
		
		//Print the day information and check the anomaly counter for each day
		for(double data: resultList){
			
			if(data >= threshold)
			{
				System.out.println("Day "+count+ " Anomaly counter: "+String.format("%.2f",data )+" anomaly alert detected");
				
			}
			else
			{
				System.out.println("Day "+count+ " Anomaly counter: "+String.format("%.2f",data )+ " work fine");
			}
			
			count++;
			
		}
		
	}
	
	
}


public class IDS {
	
	public static void main(String args[])
	{
	
		String eFile = null;
		String statFile = null;
		int days = 0;

		boolean endProgram = false;
		
		if(args.length == 3)
		{
			eFile = args[0];
			statFile = args[1];
			days = Integer.parseInt(args[2]);
		}
		else
		{
			System.out.println("Please input the correct number of argument");
			System.exit(0);
		}
		
		//Read  Initial input from the document
		InitialReadFile(eFile,statFile, days);
		
		
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
				InitialReadFile(eFile,statFile, newDay);
			}
		}
		
		System.out.println("End of the System");
		
	}
	
	public static void InitialReadFile(String eFile, String StatsFile, int days)
	{
		try {
			
			//Read the Event.txt
			FileInputStream EventIn = new FileInputStream(eFile);
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


