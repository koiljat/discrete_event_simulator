import java.util.function.Supplier;

class Customer {
    private final int customerId;
    private final double arrivalTime;
    private final Supplier<Double> serviceTime;

    Customer(int customerId, Supplier<Double> serviceTime, double arrivalTime) {
        this.customerId = customerId;
        this.serviceTime = serviceTime;
        this.arrivalTime = arrivalTime;
    }
    
    public int getCustomerId() {
        return this.customerId;
    }

    public Supplier<Double> getServiceTime() {
        return this.serviceTime;
    }

    public double getArrivalTime() {
        return this.arrivalTime;
    }
}