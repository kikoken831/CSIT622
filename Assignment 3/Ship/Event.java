
import java.util.Random;

class Event {
    String eventName;
    char eventType;
    double eventMin;
    double eventMax;
    int eventWeight;

    Stats stats;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public char getEventType() {
        return eventType;
    }

    public void setEventType(char eventType) {
        this.eventType = eventType;
    }

    public double getEventMin() {
        return eventMin;
    }

    public void setEventMin(double eventMin) {
        this.eventMin = eventMin;
    }

    public double getEventMax() {
        return eventMax;
    }

    public void setEventMax(double eventMax) {
        this.eventMax = eventMax;
    }

    public double getEventWeight() {
        return eventWeight;
    }

    public void setEventWeight(int eventWeight) {
        this.eventWeight = eventWeight;
    }

    public Stats getStats() {
        return stats;
    }
    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public Event(String eventName, char eventType, double eventMin, double eventMax, int eventWeight) {
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventMin = eventMin;
        this.eventMax = eventMax;
        this.eventWeight = eventWeight;
    }


    @Override
    public String toString() {
        return "Event{" +
                "eventName='" + eventName + '\'' +
                ", eventType='" + eventType + '\'' +
                ", eventMin=" + eventMin +
                ", eventMax=" + eventMax +
                ", eventWeight=" + eventWeight +
                ", stats=" + stats.toString() +
                "}";
    }

    public boolean validateEvent() {

        if (this.eventType == 'C') {
            if (this.eventMin < 0 || this.eventMax < 0 || this.eventWeight < 0) {
                return false;
            } else if (this.eventMin > this.eventMax) {
                return false;
            }
        } else if (this.eventType == 'D') {
            if(this.eventMax > 1440){
                return false;
            }
            if(intChecker(this.eventMin) == false
                    || intChecker(this.eventMax) == false || intChecker(this.eventWeight) == false){
                return false;
            }
            if (this.eventMin < 0 || this.eventMax < 0 || this.eventWeight < 0) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean intChecker(double n){
        if(n % 1 == 0){
            return true;
        }
        return false;
    }

    public double getValueWithinDistribution() {

        Random rand = new Random();
        while (true) {
            double value = stats.getMean() + stats.getStandardDeviation() * rand.nextGaussian();
            // round the value if it's discrete
            if (eventType == 'D') {
                value = Math.round(value);
                if (value >= eventMin && (value <= eventMax || eventMax == 0.0)) {
                    return value;
                }
            }
            if (value >= eventMin && (value <= eventMax || eventMax == 0.0)) {
                return value;
            }
        }
    }
}