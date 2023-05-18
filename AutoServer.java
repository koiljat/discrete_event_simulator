import java.util.function.Supplier;

class AutoServer extends Server {

    AutoServer(int serverId, int queueAvailable, double timeAvailable, Supplier<Double> restTimes) {
        super(serverId, queueAvailable, timeAvailable, restTimes);
    }

    public ImList<Server> updateServe(ImList<Server> serverList) {
        serverList = serverList.set(this.getServerId() - 1, this);
        return serverList;
    }

    public boolean isFaster(double timeStamp) {
        return this.getTimeAvailable() < timeStamp;
    }

    public Server getFastest(ImList<Server> serverList) {
        Server fastestServer = this;
        for (Server server: serverList) {
            if (server.isFaster(this.getTimeAvailable())) {
                fastestServer = server;
            }
        }
        return fastestServer;
    }

    @Override
    public Server doneServe(double timeStamp, Customer customer) {
        int queueAvailable = this.getQueueAvailable();
        if (timeStamp != customer.getArrivalTime()) {
            queueAvailable += 1;
        }
        return new AutoServer(
            this.getServerId(), 
            queueAvailable,
            timeStamp + customer.getServiceTime().get(),
            this.getRestTimes());
    }

    public Server updateQueue(int newQueue) {
        return new AutoServer(
            this.getServerId(), 
            newQueue, 
            this.getTimeAvailable(), 
            this.getRestTimes());
    }

    @Override
    public ImList<Server> updateAll(ImList<Server> serverList) {
        ImList<Server> updatedServerList = serverList;
        int currQueue = this.getQueueAvailable();
        for (Server server: serverList) {
            if (server.getServerId() != this.getServerId()) {
                Server updatedServer = server.updateQueue(currQueue);
                updatedServerList = updatedServer.update(serverList);
            }
        }
        return updatedServerList;
    }

    @Override
    public Server rest(double timeStamp) {
        return new AutoServer(
            this.getServerId(),
            this.getQueueAvailable(),
            timeStamp,
            this.getRestTimes()
        );
    }

    public Server serve(Customer customer) {
        return new AutoServer(this.getServerId(),
            this.getQueueAvailable(), 
            customer.getArrivalTime(),
            this.getRestTimes());
    }

    public Server wait(Customer customer) {
        return new AutoServer(this.getServerId(),
            this.getQueueAvailable() - 1, 
            this.getTimeAvailable(),
            this.getRestTimes());
    }

    public String toString() {
        String output = "self-check " + this.getServerId();
        return output;
    }
}