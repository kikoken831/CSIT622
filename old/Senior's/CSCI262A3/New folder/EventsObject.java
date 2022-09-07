
//Event object class store the event variable
public class EventsObject {
	
	String Eventname;
	String type;
	String minimum;
	String maximum;
	String units;
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
	
	public String getUnits() {
		return units;
	}
	
	public void setUnits(String units) {
		this.units = units;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	
	
	
	
}
