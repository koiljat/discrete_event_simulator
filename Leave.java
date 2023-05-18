class Leave extends Event {

    Leave(Customer customer) {
        super(customer);
    }
    
    //When a customer leaves, it does not affect any event
    public Pair<Event, ImList<Server>> nextEvent(ImList<Server> serverList) {
        return new Pair<Event, ImList<Server>>(this, serverList);
    }

    public String toString() {
        return String.format("%.3f", this.getTimeStamp()) 
            + String.format(" %d leaves", this.getCustomer().getCustomerId());
    }

    @Override
    public int fail(int fail) {
        return fail + 1;
    }
}