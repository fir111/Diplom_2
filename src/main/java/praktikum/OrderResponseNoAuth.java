package praktikum;

public class OrderResponseNoAuth {
    private boolean success;
    private String name;
    private OrderNoAuth order;

    public OrderResponseNoAuth(boolean success, String name, OrderNoAuth order) {
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

    public void setName(String name) {
        this.name = name;
    }

    public OrderNoAuth getOrder() {
        return order;
    }

    public void setOrder(OrderNoAuth orderNoAuth) {
        this.order = orderNoAuth;
    }
}
