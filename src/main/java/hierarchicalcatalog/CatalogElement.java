package hierarchicalcatalog;

public class CatalogElement {

    private Integer id;
    private ElementType type;
    private Integer parent;
    private String name;

    public CatalogElement(Integer id, ElementType type, Integer parent, String name) {
        this.id = id;
        this.type = type;
        this.parent = parent;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public ElementType getType() {
        return type;
    }

    public Integer getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

}
