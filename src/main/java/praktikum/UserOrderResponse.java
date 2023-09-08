package praktikum;
import java.util.List;

public class UserOrderResponse {
    private boolean success;
    private List<UserOrder> orders;
    private int total;
    private int totalToday;

    public UserOrderResponse(boolean success, List<UserOrder> orders, int total, int totalToday) {
        this.success = success;
        this.orders = orders;
        this.total = total;
        this.totalToday = totalToday;
    }
    public UserOrderResponse(){}

    public boolean isSuccess() {
        return success;
    }

    public List<UserOrder> getOrders() {
        return orders;
    }

    public int getTotal() {
        return total;
    }

    public int getTotalToday() {
        return totalToday;
    }
}
