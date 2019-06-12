package hierarchicalcatalog;

public enum SortOrder {

    NO_ORDER(0), TO_UP(1), TO_DOWN(-1);

    private int mul;

    SortOrder(int mul) {
        this.mul = mul;
    }

    public int getMul() {
        return mul;
    }

}
