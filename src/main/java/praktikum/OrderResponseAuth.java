package praktikum;

public class OrderResponseAuth {
    private boolean success;
    private String name;
    private OrderAuth order;

    public OrderResponseAuth(boolean success, String name, OrderAuth order) {
        this.success = success;
        this.name = name;
        this.order = order;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getName() {
        return name;
    }

    public OrderAuth getOrder() {
        return order;
    }
}
