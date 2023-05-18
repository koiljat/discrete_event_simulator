import java.util.function.Supplier;

class Simulator {
    private final int numOfServers;
    private final ImList<Double> arrivalTimes;
    private final Supplier<Double> serviceTime;
    private final int qmax;
    private final int numOfSelfChecks;
    private final Supplier<Double> restTimes;

    Simulator(int numOfServers,
        int numOfSelfChecks,
        int qmax, 
        ImList<Double> arrivalTimes, 
        Supplier<Double> serviceTime,
        Supplier<Double> restTimes) {
        this.numOfServers = numOfServers;
        this.arrivalTimes = arrivalTimes;
        this.qmax = qmax;
        this.serviceTime = serviceTime;
        this.numOfSelfChecks = numOfSelfChecks;
        this.restTimes = restTimes;
    }

    private ImList<Server> createServer() {
        ImList<Server> serverList = new ImList<Server>();
        for (int i = 0; i < numOfServers; i++) {
            serverList = serverList.add(new Server(1 + i, 
                qmax,
                0,
                this.getRestTimes()));
        }
        serverList = serverList.addAll(this.createAutoServers());
        return serverList;
    }

    private ImList<Server> createAutoServers() {
        ImList<Server> autoServers = new ImList<Server>();
        Supplier<Double> restTimes = () -> 0.0;
        for (int i = 0; i < numOfSelfChecks; i++) {
            autoServers = autoServers.add(
                new AutoServer(1 + i + numOfServers, 
                qmax,
                0, 
                restTimes));
        }
        return autoServers;
    }

    private Supplier<Double> getRestTimes() {
        return this.restTimes;
    }

    private Supplier<Double> getServiceTime() {
        return this.serviceTime;
    }

    private ImList<Customer> getQueue() {
        ImList<Customer> queue = new ImList<Customer>();
        for (int i = 0; i < this.arrivalTimes.size(); i++) {
            queue = queue.add(new Customer(i + 1, 
                this.getServiceTime(),
                this.arrivalTimes.get(i)));
        }
        return queue;
    }

    private ImList<Event> getArrivalEvents() {
        ImList<Customer> queue = this.getQueue();   
        ImList<Event> arrivalEvents = new ImList<Event>();
        for (Customer customer: queue) {
            arrivalEvents = arrivalEvents.add(new Arrival(customer));
        }
        return arrivalEvents;
    }

    private double averageWaitingTime(int success, double waitingTime) {
        if (success == 0) {
            return 0;
        } else {
            return waitingTime / success;
        }
    }

    public String simulate() {
        String outputString = "";
        PQ<Event> pq = new PQ<Event>(new TimeStampComaprator());
        for (Event arrival: this.getArrivalEvents()) {
            pq = pq.add(arrival);
        }
        int success = 0;
        int fail = 0;
        double waitingTime = 0;
        ImList<Server> serverList = this.createServer();
        while (!pq.isEmpty()) {
            Pair<Event, PQ<Event>> pair = pq.poll();
            Event currEvent = pair.first();
            pq = pair.second();
            outputString = currEvent.addResult(outputString);
            success = currEvent.success(success);
            fail = currEvent.fail(fail);
            waitingTime = currEvent.waitingTime(waitingTime);
            Pair<Event, ImList<Server>> nextSeqeuncePair = currEvent.nextEvent(serverList);
            Event newEvent = nextSeqeuncePair.first();
            serverList = nextSeqeuncePair.second();
            if (currEvent != newEvent) {
                pq = pq.add(newEvent);
            }
        } 
        double averageWaitingTime = averageWaitingTime(success, waitingTime);
        outputString += String.format("[%.3f ", averageWaitingTime) 
            + success
            + String.format(" %d]", fail);
        return outputString;
    }
}
