package praktikum;

import java.util.List;

public class Ingredients {
    private boolean success;
    private List<Ingredient> data;

    public Ingredients(boolean success, List<Ingredient> data) {
        this.success = success;
        this.data = data;
    }
    public Ingredients(){}

    public boolean isSuccess() {
        return success;
    }

    public List<Ingredient> getData() {
        return data;
    }
}
