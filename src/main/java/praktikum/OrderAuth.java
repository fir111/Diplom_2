package praktikum;

import java.util.List;

public class OrderAuth {
    private List<Ingredient> ingredients;
    private String _id;
    private Owner owner;
    private String status;
    private String name;
    private String createdAt;
    private String updatedAt;
    private int number;
    private int price;

    public OrderAuth(List<Ingredient> ingredients, String id, Owner owner,
                     String status, String name, String createdAt, String updatedAt, int number, int price) {
        this.ingredients = ingredients;
        _id = id;
        this.owner = owner;
        this.status = status;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.number = number;
        this.price = price;
    }
    public OrderAuth(){}

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public String get_id() {
        return _id;
    }

    public Owner getOwner() {
        return owner;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public int getNumber() {
        return number;
    }

    public int getPrice() {
        return price;
    }
}
