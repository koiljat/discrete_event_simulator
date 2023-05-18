class ContinueWait extends Event {
    private final Server server;

    ContinueWait(Customer customer, Server server) {
        super(customer);
        this.server = server;    
    }

    @Override
    public double getTimeStamp() {
        return this.getServer().getTimeAvailable();
    }

    private Server getServer() {
        return this.server;
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
    public String addResult(String output) {
        return output;
    }
}