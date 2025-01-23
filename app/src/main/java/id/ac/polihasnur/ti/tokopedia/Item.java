package id.ac.polihasnur.ti.tokopedia;
public class Item {
    private String id;
    private String name;
    private String description;
    private String imageUrl;

    public Item(String id, String name, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
