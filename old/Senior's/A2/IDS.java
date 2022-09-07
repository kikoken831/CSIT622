
public class IDS {
	
	String eventFile = "";
	String statsFile = "";
	int days = 0;
	
	public static void main(String args[]) 
    { 
		
        String hashValue = "This is a test"; 
		if (args.length < 3 || args.length > 3)
		{		
			System.out.println("Usage: IDS [EVENT FILE] [STATS FILE] [DAYS]");
			System.exit(0);
		}
		else if (args.length == 3)
		{
			eventFile = args[0];
			statsFile = args[1];
			days = args[2];
		}
		System.out.println(eventFile);
		System.out.println(statsFile);
		System.out.println(days;
    } 
	
}