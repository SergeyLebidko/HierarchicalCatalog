package hierarchicalcatalog;

public enum ElementType{

    DIR(0), ELEMENT(1);

    private int intType;

    ElementType(int intType) {
        this.intType = intType;
    }

    public int getIntType() {
        return intType;
    }

}
