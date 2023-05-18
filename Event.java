abstract class Event {
    //All event would have customers 
    private final Customer customer;

    //Constructor for event
    Event(Customer customer) {
        this.customer = customer;
    }

    //Getter for customer
    public Customer getCustomer() {
        return this.customer;
    }

    //Default getter for timestamp of event
    public double getTimeStamp() {
        return this.getCustomer().getArrivalTime();
    }

    public String addResult(String output) {
        return output + this.toString() + "\n";
    }

    //Method to generate the next event in the sequence
    abstract Pair<Event, ImList<Server>> nextEvent(ImList<Server> server);

    //Below are the methods for tabulating summary
    public int success(int success) {
        return success;
    }
    
    public int fail(int fail) {
        return fail;
    }

    public double waitingTime(double waitingTime) {
        return waitingTime;
    }
}