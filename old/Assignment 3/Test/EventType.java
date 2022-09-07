import java.util.Random;
 
class EventType
{
    String name;
    char type;
    double min;
    double max;
    double weight;
    
    double mean;
    double std;
    
    
    public static EventType[] cloneEvents(EventType[] arg)
    {
        EventType newEvents[] = new EventType[arg.length];
        
        for(int i = 0;i <arg.length; i++)
            newEvents[i] = new EventType(arg[i]);
        
        return newEvents;
    }
	
	// default constructor
    public EventType(String name, char type, double min, double max, double weight)
    {
        this.name = name;
        this.type = type;
        this.min = min;
        this.max = max;
        this.weight=weight;
     
        this.mean = 0.0;
        this.std = 0.0;
    }
	
	// copy constructor
	public EventType(EventType copy)
    {
        this.name = copy.name;
        this.type = copy.type;
        this.min = copy.min;
        this.max = copy.max;
        this.weight = copy.weight;
        
        this.mean = copy.mean;
        this.std = copy.std;
    }
	
	// getters functions
    public String getName() {
        return name;
    }

    public char getType() {
        return type;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getMean() {
        return mean;
    }

    public double getStd() {
        return std;
    }

    public double getWeight() {
        return weight;
    }
	
	// setters functions
    public void setMean(double mean) {
        this.mean = mean;
    }

    public void setStd(double std) {
        this.std = std;
    }
    
    public double getRandValue() {
        
        Random rand = new Random();
        while(true)
        {
            double value = mean + std * rand.nextGaussian();
            // round the value if it's discrete
            if(type == 'D')
                value = Math.round(value);

            if(value >= min && value <= max)
            {
                return value;
            }
        }
    }
};