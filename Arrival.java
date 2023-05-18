class Arrival extends Event {
    
    Arrival(Customer customer) {
        super(customer);
    }

    private Pair<Event, ImList<Server>> checkServe(ImList<Server> serverList) {
        Customer customer = this.getCustomer();
        for (Server server: serverList) {
            if (server.canServe(customer)) {
                server = server.serve(customer);
                serverList = server.update(serverList);
                Serve newServe = new Serve(customer, server);
                return new Pair<Event,ImList<Server>>(newServe, serverList);
            }
        }
        return this.checkWait(serverList);
    }

    private Pair<Event, ImList<Server>> checkWait(ImList<Server> serverList) {
        Customer customer = this.getCustomer();
        for (Server server: serverList) {
            if (server.canWait()) {
                server = server.wait(customer);
                serverList = server.update(serverList);
                serverList = server.updateAll(serverList);
                Wait newWait = new Wait(customer, server);
                return new Pair<Event,ImList<Server>>(newWait, serverList);
            }
        }
        Leave newLeave = new Leave(customer);
        return new Pair<Event,ImList<Server>>(newLeave, serverList);
    }

    public Pair<Event, ImList<Server>> nextEvent(ImList<Server> serverList) {
        return checkServe(serverList);
    }

    public String toString() {
        return String.format("%.3f", this.getTimeStamp()) +
            String.format(" %d arrives", this.getCustomer().getCustomerId());
    }
}
