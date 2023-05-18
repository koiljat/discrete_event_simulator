class Wait extends Event {
    private final Server server;

    Wait(Customer customer, Server server) {
        super(customer);
        this.server = server;
    }

    private Server getServer() {
        return this.server;
    }

    public double getTimeStamp() {
        return this.getCustomer().getArrivalTime();
    }

    @Override
    public Pair<Event, ImList<Server>> nextEvent(ImList<Server> serverList) {
        Server server = serverList.get(this.getServer().getServerId() - 1);
        Customer customer = this.getCustomer();
        server = server.getFastest(serverList);
        if (this.getTimeStamp() >= server.getTimeAvailable()) {
            serverList = server.update(serverList);
            Serve newServe = new Serve(customer, server);
            return new Pair<Event,ImList<Server>>(newServe, serverList);
        } else {
            ContinueWait nextWait = new ContinueWait(customer, server);
            return new Pair<Event,ImList<Server>>(nextWait, serverList);
        }
    }

    @Override
    public String toString() {
        return String.format("%.3f", this.getTimeStamp()) +
        String.format(" %d waits at ", this.getCustomer().getCustomerId()) +
        String.format("%s", this.getServer().toString());
    }
}
