package core.model.db;

public class Express {
    String name;
    String function;

    public Express(String name, String function) {
        this.name = name;
        this.function = function;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getName() {
        return name;
    }

    public String getFunction() {
        return function;
    }
}
