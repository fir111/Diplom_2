package praktikum;

public class Ingredient {
    private String _id;
    private String name;
    private String type;
    private int proteins;
    private int fat;
    private int carbohydrates;
    private int calories;
    private int price;
    private String image;
    private String image_mobile;
    private String image_large;

    public Ingredient(String id, String name, String type, int proteins,
                      int fat, int carbohydrates, int calories, int price,
                      String image, String imageMobile, String imageLarge) {
        _id = id;
        this.name = name;
        this.type = type;
        this.proteins = proteins;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.calories = calories;
        this.price = price;
        this.image = image;
        image_mobile = imageMobile;
        image_large = imageLarge;
    }

    public Ingredient(){}

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public int getProteins() {
        return proteins;
    }

    public int getFat() {
        return fat;
    }

    public int getCarbohydrates() {
        return carbohydrates;
    }

    public int getCalories() {
        return calories;
    }

    public int getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getImage_mobile() {
        return image_mobile;
    }

    public String getImage_large() {
        return image_large;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient)) return false;

        Ingredient that = (Ingredient) o;

        if (getProteins() != that.getProteins()) return false;
        if (getFat() != that.getFat()) return false;
        if (getCarbohydrates() != that.getCarbohydrates()) return false;
        if (getCalories() != that.getCalories()) return false;
        if (getPrice() != that.getPrice()) return false;
        if (!get_id().equals(that.get_id())) return false;
        if (!getName().equals(that.getName())) return false;
        if (!getType().equals(that.getType())) return false;
        if (!getImage().equals(that.getImage())) return false;
        if (!getImage_mobile().equals(that.getImage_mobile())) return false;
        return getImage_large().equals(that.getImage_large());
    }

    @Override
    public int hashCode() {
        int result = get_id().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getType().hashCode();
        result = 31 * result + getProteins();
        result = 31 * result + getFat();
        result = 31 * result + getCarbohydrates();
        result = 31 * result + getCalories();
        result = 31 * result + getPrice();
        result = 31 * result + getImage().hashCode();
        result = 31 * result + getImage_mobile().hashCode();
        result = 31 * result + getImage_large().hashCode();
        return result;
    }
}
