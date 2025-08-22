package data;

public class FilterData {
    private String id;
    private String name;

    public FilterData(){

    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
