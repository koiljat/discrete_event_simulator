class Done extends Event {
    private final Server server;

    Done(Customer customer, Server server) {
        super(customer);
        this.server = server;
    }

    public Pair<Event, ImList<Server>> nextEvent(ImList<Server> serverList) {
        Server server = serverList.get(this.getServer().getServerId() - 1);
        Server updatedServer = server.rest(this.getTimeStamp());
        serverList = updatedServer.update(serverList);
        return new Pair<Event, ImList<Server>>(this, serverList);
    }

    private Server getServer() {
        return this.server;
    }

    @Override
    public int success(int success) {
        return success + 1;
    }

    public double getTimeStamp() {
        return this.getServer().getTimeAvailable();
    }

    public String toString() {
        return String.format("%.3f", this.getTimeStamp()) +
            String.format(" %d done serving by ", this.getCustomer().getCustomerId()) +
            String.format("%s", this.getServer().toString());
    }
}
