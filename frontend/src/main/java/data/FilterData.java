package data;

public class FilterData {
    private int id;
    private String name;

    public FilterData(){

    }
    public FilterData(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
