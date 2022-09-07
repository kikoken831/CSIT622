import java.io.*;
import java.util.*;
import java.text.DecimalFormat;

class sObject {

	String evName;
	double mean;
	double SD;
	
	public String getEName() {
		return evName;
	}
	
	public void setEName(String evName) {
		this.evName = evName;
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

class eObject {
	
	String eName;
	String type;
	String min;
	String max;
	int weight;
	
	public String getEName() {
		return eName;
	}
	
	public void setEName(String ename) {
		eName = ename;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getMin() {
		return min;
	}
	
	public void setMin(String min) {
		this.min = min;
	}
	
	public String getMax() {
		return max;
	}
	
	public void setMax(String max) {
		this.max = max;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
		
}


class analysisEngine {
	
	int days;
	List<eObject> eList;
	List<sObject> sList;
	double mean;
	double SD;
	int eCount = 0;
	
	List<sObject> outputList = new ArrayList<sObject>();
	List<Double> sData = new ArrayList<Double>();
	
	public analysisEngine(int days, List<eObject> evList, List<sObject> statList) {
		super();
		this.days = days;
		eList = evList;
		sList = statList;
	}
	
	public void readAnalysis()
	{
		
		System.out.println("Generate log file completed starting analysis");
		
		try {
			
			for(int j=0; j<eList.size(); j++)
			{

				if(eList.get(j).type.equalsIgnoreCase("D"))
				{
					FileInputStream eIn;
					

					eIn = new FileInputStream(eList.get(j).eName+".log");
					BufferedReader buff = new BufferedReader(new InputStreamReader(eIn));
					String strLine;
					while ((strLine = buff.readLine()) != null)   {
						
						if(numCheck(strLine))
						{
							sData.add(Double.parseDouble(strLine));
						}
						
					}

					calSD(sData, days, eCount);
					eCount++;
					sObject newStats = new sObject();
					newStats.setEName(eList.get(j).eName);
					newStats.setMean(mean);
					newStats.setSD(SD);
					outputList.add(newStats);
					
				}
				
				
				else if(eList.get(j).type.equalsIgnoreCase("C"))
				{
					FileInputStream eIn;
					eIn = new FileInputStream(eList.get(j).eName+".log");

					BufferedReader buff = new BufferedReader(new InputStreamReader(eIn));
					
					String strLine;
					while ((strLine = buff.readLine()) != null)   {
						
						if(numCheck(strLine))
						{
							sData.add(Double.parseDouble(strLine));
						}
						
					}

					calSD(sData, days, eCount);
					eCount++;
					
					sObject newStats = new sObject();
					newStats.setEName(eList.get(j).eName);
					newStats.setMean(mean);
					newStats.setSD(SD);
					
					outputList.add(newStats);
					
				}
				
			}
			
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{	
			e.printStackTrace();
		}
	}
	
	public boolean numCheck(String str)
	{
		try  
		  { 
		    double d = Double.parseDouble(str);  
		  }  
		  catch(NumberFormatException nfe)  
		  {  
		    return false;  
		  }  
		  return true;
	}
	
	public void calSD(List<Double> list, int days, int count)
	{
		double mean = 0.0;
		for(int i = days*count; i < days*(count+1); i++)
		{
			mean += list.get(i);
		}	
		mean = mean /(double)(days);
		double SD = 0.0;
		for(int i = days*count; i< days*(count+1); i++)
		{
			SD += (list.get(i)-mean)*(list.get(i)-mean);
		}
		
		SD = Math.sqrt(SD/(double)(days-1));
		
		this.mean = mean;
		
		this.SD = SD;
		
	}
	
	public void outputNewSD()
	{
		try
		{
			PrintWriter out = new PrintWriter("NewStats.txt");
			
			for(int i=0; i<outputList.size(); i++)
			{		
					out.println(outputList.get(i).evName+":"+ new DecimalFormat("##.##").format(outputList.get(i).mean)
							+":"+new DecimalFormat("##.##").format(outputList.get(i).SD));	
			}
					
			out.close();

		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public void startAnalysis()
	{
		alertEngine alEngine = new alertEngine(sData,eList,outputList,days);
		alEngine.calThreshold();
		alEngine.calCounter();
	}
	
}

class activityEngine {
	
	
	int days;
	List<eObject> eList;
	List<sObject> sList;
	
	Random in = new Random();
	public activityEngine(int days, List<eObject> evList, List<sObject> statList) {
		super();
		this.days = days;
		eList = evList;
		sList = statList;
	}

	public void createLogFile()
	{
		System.out.println("Start generate log file ...");
		for(int j = 0; j < eList.size(); j++)
		{
			if(eList.get(j).type.equalsIgnoreCase("D"))
			{
				try{
						PrintWriter out = new PrintWriter(eList.get(j).eName+".log");
						boolean done = false;
						int daysCount = 1;
						while(!done){
							int loginNo = in.nextInt((int)(sList.get(j).mean + 2*sList.get(j).SD));	
							if(loginNo >= (int)(sList.get(j).mean - 2*sList.get(j).SD)){			
								out.println("Day " + daysCount);
								out.println(loginNo);	
								for(int i=0;i<loginNo; i++ ){
									out.println(eList.get(j).eName);
								}
								out.println();
								out.println();
								daysCount++;
							}
							
							if(daysCount-1 == days){
								done = true;
								out.close();
							}
						}
					}catch(IOException e){
					e.printStackTrace();
					}
			}
			
			else if (eList.get(j).type.equalsIgnoreCase("C"))
			{
				try{
					PrintWriter out = new PrintWriter(eList.get(j).eName+".log");
					boolean done = false;
					int daysCount = 1;
					while(!done){
						
						double totalEachDay = 0.0;
						int onlineNo = in.nextInt((int)(Integer.parseInt(eList.get(j).getMax())/sList.get(j).mean))+1;	
						{			
							out.println("Day " + daysCount);
							out.println("Total Events per day: "+ onlineNo);		
							for(int i = 0; i < onlineNo;){
							
								double onlineD = in.nextFloat() * (int)(sList.get(j).mean + 2*sList.get(j).SD);
								
								if (onlineD >= sList.get(j).mean - 2*sList.get(j).SD)
								{
									double value = Double.valueOf(String.format("%.2f", onlineD));
									totalEachDay += value;
									
									out.println(eList.get(j).eName + value);
									i++;
								}
							}
							out.println();
							out.println(String.format("%.2f", totalEachDay/onlineNo));
							out.println();
							daysCount++;
						}
						if(daysCount-1 == days)
						{
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


class alertEngine {

	List<Double> sData;
	List<eObject> evList;
	List<sObject> outputList;
	List<Double> resultList;
	int threshold;
	int day;

	public alertEngine(List<Double> sData, List<eObject> evList, List<sObject> outputList, int day) {	
		this.sData = sData;
		this.evList = evList;
		this.outputList = outputList;
		resultList = new ArrayList<Double>();
		this.day = day;
	}

	public void calThreshold(){
		
		int sum = 0;
		for(int i=0; i<evList.size(); i++)
		{
			sum += evList.get(i).weight;
		}
		
		int threshold;
		threshold = sum*2;
		this.threshold = threshold;
		
		System.out.println("The Threshold of the Stats.txt: "+threshold);
	
	}

	public void calCounter()
	{
		int count = 1;
		for(int i = 0; i < day; i++)
		{
			int index = i;
			double res = 0;
			for(int j=0;j<evList.size();j++){
				res += (sData.get(index+day*j) - outputList.get(j).mean)/outputList.get(j).getSD() * evList.get(j).getWeight();
			}
			resultList.add(res);
		}
		for(double data: resultList){
			
			if(data >= threshold)
			{
				System.out.println("Day "+count+ " Anomaly counter: "+String.format("%.2f",data )+" anomaly detected");
				
			}
			else
			{
				System.out.println("Day "+count+ " Anomaly counter: "+String.format("%.2f",data )+ " working fine");
			}
			
			count++;
			
		}
		
	}
	
	
}


public class IDS {
	
	public static void main(String args[])
	{
	
		String eFile = null;
		String sFile = null;
		int days = 0;

		boolean end = false;
		
		if(args.length == 3)
		{
			eFile = args[0];
			sFile = args[1];
			days = Integer.parseInt(args[2]);
		}
		else
		{
			System.out.println("Please input the correct number of argument");
			System.out.println("[IDS] [EVENT FILE] [STAT FILE] [DAYS]");
			System.exit(0);
		}
		
		readFile(eFile, sFile, days);
		
		
		while(end == false)
		{
			System.out.println("Enter Quit to end the program, to continue input anything: ");
			Scanner input = new Scanner(System.in);
			
			if(input.nextLine().equalsIgnoreCase("Quit"))
			{
				end = true;
			}
			else
			{
				System.out.println("Please enter the number of days");
				int nDays =  Integer.parseInt(input.nextLine());
				readFile(eFile,sFile, nDays);
			}
		}
		
		System.out.println("Ending program...");
		
	}
	
	public static void readFile(String eFile, String StatsFile, int days)
	{
		try {

			FileInputStream eIn = new FileInputStream(eFile);
			BufferedReader buff = new BufferedReader(new InputStreamReader(eIn));
			String strLine;
			List<eObject> eList = new ArrayList<eObject>();
			
			while ((strLine = buff.readLine()) != null)   {
				  
				  if(strLine.indexOf(":")>=0)
				  {
					  String separator[] = strLine.split(":");
					  eObject eventObject = new eObject();
					  eventObject.setEName(separator[0]);
					  eventObject.setType(separator[1]);
					  eventObject.setMin(separator[2]);
					  eventObject.setMax(separator[3]);
					  eventObject.setWeight(Integer.parseInt(separator[4]));
					  eList.add(eventObject);
					  
				  }
				
			}
			

			FileInputStream StatsIn = new FileInputStream(StatsFile);
			BufferedReader buffStat = new BufferedReader(new InputStreamReader(StatsIn));
			
			List<sObject> sList = new ArrayList<sObject>();
			
			while ((strLine = buffStat.readLine()) != null)   {
				  
				  if(strLine.indexOf(":")>=0)
				  {
					  String separator[] = strLine.split(":");
					  
					  sObject stats = new sObject();
					  
					  stats.setEName(separator[0]);
					  stats.setMean(Double.parseDouble(separator[1]));
					  stats.setSD(Double.parseDouble(separator[2]));
					  sList.add(stats);
					  
				  }
				
			}
			
			buff.close();
			buffStat.close();
		
			activityEngine acEngine = new activityEngine(days, eList, sList);
			acEngine.createLogFile();
			

			analysisEngine alEngine = new analysisEngine(days, eList, sList);
			alEngine.readAnalysis();
			alEngine.outputNewSD();
			alEngine.startAnalysis();
			
				
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	
}


