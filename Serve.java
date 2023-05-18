class Serve extends Event {
    private final Server server;
 
    Serve(Customer customer, Server server) {
        super(customer);
        this.server = server;
    }

    public Pair<Event, ImList<Server>> nextEvent(ImList<Server> serverList) {
        Server updatedServer = server.doneServe(this.getTimeStamp(), this.getCustomer());
        serverList = updatedServer.update(serverList);
        serverList = updatedServer.updateAll(serverList);
        Done newDone = new Done(this.getCustomer(), updatedServer);
        return new Pair<Event,ImList<Server>>(newDone, serverList);
    }

    private Server getServer() {
        return this.server;
    }

    public double getTimeStamp() {
        return this.getServer().getTimeAvailable();
    }

    @Override
    public double waitingTime(double waitingTime) {
        double waitTime = this.getTimeStamp() -
            this.getCustomer().getArrivalTime();
        return waitingTime + waitTime;
    }

    @Override
    public String toString() {
        return String.format("%.3f", this.getTimeStamp()) +
        String.format(" %d serves by ", this.getCustomer().getCustomerId()) +
        String.format("%s", this.getServer().toString());
    }
}
