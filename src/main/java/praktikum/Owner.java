package praktikum;

public class Owner {
    private String name;
    private String email;
    private String createdAt;
    private String updatedAt;

    public Owner(String name, String email, String createdAt, String updatedAt) {
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
}
