import java.util.*;

public class AlterEngine {

	List<Double> sampleData;
	List<EventsObject> eventList;
	List<StatsObject> outputList;
	List<Double> resultList;
	int threshold;
	int day;
	
	//The constructor of the class
	public AlterEngine(List<Double> sampleData, List<EventsObject> eventList, List<StatsObject> outputList, int day) {	
		this.sampleData = sampleData;
		this.eventList = eventList;
		this.outputList = outputList;
		resultList = new ArrayList<Double>();
		this.day = day;
	}

	//Calculate the threshold
	public void calculateThreshold(){
		
		int sum = 0;
		
		//The sum of the weight
		for(int i=0; i<eventList.size(); i++)
		{
			sum += eventList.get(i).weight;
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
			for(int j=0;j<eventList.size();j++){
				res += (sampleData.get(index+day*j) - outputList.get(j).mean)/outputList.get(j).getSD() * eventList.get(j).getWeight();
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
