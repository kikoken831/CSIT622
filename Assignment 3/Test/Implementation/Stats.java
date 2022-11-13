package Implementation;
public class Stats {
    String eventName;
    double mean;
    double standardDeviation;

    public Stats(String eventName, double mean, double standardDeviation) {
        this.eventName = eventName;
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

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

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    @Override
    public String toString() {
        return "Ship.Stats{" +
                "eventName='" + eventName + '\'' +
                ", mean=" + mean +
                ", standardDeviation=" + standardDeviation +
                '}';
    }

    public boolean validateStats() {

        return true;
    }
}
