import java.util.Comparator;

class TimeStampComaprator implements Comparator<Event> {
    @Override
    public int compare(Event firstRecord, Event secondRecord) {
        int result =  Double.compare(firstRecord.getTimeStamp(),secondRecord.getTimeStamp());
        if (result == 0) {
            result = Integer.compare(firstRecord.getCustomer().getCustomerId(),
                secondRecord.getCustomer().getCustomerId());
        }
        return result;
    }
}
