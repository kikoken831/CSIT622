import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

public class ActivityEngine {
	
	
	int days;
	List<EventsObject> EventList;
	List<StatsObject> StatsList;
	
	Random in = new Random();
	
	//Constructor put the number of days and the list object to the class
	public ActivityEngine(int days, List<EventsObject> eventList, List<StatsObject> statsList) {
		super();
		this.days = days;
		EventList = eventList;
		StatsList = statsList;
	}

	//Create log file function
	public void createLog()
	{
		System.out.println("Start generate log file ...");
		
		//Create log file for all events
		for(int j=0; j<EventList.size(); j++)
		{
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
			else if (EventList.get(j).type.equalsIgnoreCase("E"))
			{
				try{
					PrintWriter out = new PrintWriter(EventList.get(j).Eventname+".log");
					boolean done = false;
					int dayCount = 1;
					while(!done){
						//total value per day
						long totalPerDay = 0;
						//define max 100 records per day
						int num = in.nextInt(100)+1;
						{	
							//write day info
							out.println("Day" + dayCount);
							//total recorded number
							out.println("Total Events per day:"+num);
							for(int i=0;i<num;){
								//generate random number less than mean + 2*standard deviation
								int data = in.nextInt((int)(StatsList.get(j).mean + 2*StatsList.get(j).SD));
								
								//Check if the event has a minimum
								if(EventList.get(j).getMinimum().equals("0")){	
										//update total value per day
										totalPerDay += data;
										out.println(EventList.get(j).Eventname + data);
										
										i++;
								}
								else	
								{
									//If the event has no minimum we can also accept negative data
									if (data <= Math.abs(StatsList.get(j).mean - 2*StatsList.get(j).SD)){
										int judgeSymbol = in.nextInt(2);
										if(judgeSymbol == 0)
											data = -data;
									}
										//update total value per day	
										totalPerDay += data;	
										out.println(EventList.get(j).Eventname + data);
										i++;
									
								}
							}
							out.println();
							
							//write total value per day
							out.println(totalPerDay/num);
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
