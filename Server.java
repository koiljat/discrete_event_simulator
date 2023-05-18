import java.util.function.Supplier;

class Server {
    private final int serverId;
    private final int queueAvailable;
    private final double timeAvailable;
    private final Supplier<Double> restTimes;

    Server(int serverId, int queueAvailable, double timeAvailable, Supplier<Double> restTimes) {
        this.serverId = serverId;
        this.queueAvailable = queueAvailable;
        this.timeAvailable = timeAvailable;
        this.restTimes = restTimes;
    }

    public Supplier<Double> getRestTimes() {
        return this.restTimes;
    }

    public Server getFastest(ImList<Server> serverList) {
        return this;
    }

    public boolean isFaster(double timeStamp) {
        return false;
    }

    public Server rest(double timeStamp) {
        return new Server(
            this.getServerId(),
            this.getQueueAvailable(),
            timeStamp + this.getRestTimes().get(),
            this.getRestTimes()
        );
    }

    public Server doneServe(double timeStamp, Customer customer) {
        int queueAvailable = this.getQueueAvailable();
        if (timeStamp != customer.getArrivalTime()) {
            queueAvailable += 1;
        }
        return new Server(
            this.getServerId(), 
            queueAvailable,
            timeStamp + customer.getServiceTime().get(),
            this.getRestTimes());
    }

    public Server updateQueue(int newQueue) {
        return this;
    }

    public ImList<Server> update(ImList<Server> serverList) {
        serverList = serverList.set(this.getServerId() - 1, this);
        return serverList;
    }

    public ImList<Server> updateAll(ImList<Server> serverList) {
        serverList = serverList.set(this.getServerId() - 1, this);
        return serverList;
    }

    public Server serve(Customer customer) {
        return new Server(this.getServerId(),
            this.getQueueAvailable(), 
            customer.getArrivalTime(),
            this.getRestTimes());
    }

    public Server wait(Customer customer) {
        return new Server(this.getServerId(),
            this.getQueueAvailable() - 1, 
            this.getTimeAvailable(),
            this.getRestTimes());
    }

    public int getServerId() {
        return this.serverId;
    }

    public int getQueueAvailable() {
        return this.queueAvailable;
    }

    public double getTimeAvailable() {
        return this.timeAvailable;
    }

    public boolean canWait() {
        return this.getQueueAvailable() > 0;
    }

    public boolean canServe(Customer customer) {
        return customer.getArrivalTime() >= this.getTimeAvailable();
    }

    @Override
    public String toString() {
        String output = "";
        output += this.getServerId();
        return output;
    }
}
