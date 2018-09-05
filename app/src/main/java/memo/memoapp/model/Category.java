package memo.memoapp.model;

import java.io.Serializable;

/**
 * Created by Angelo Faella
 */

public class Category implements Serializable{

    public static final int RED=0;
    public static final int BLUE=1;
    public static final int GREEN=2;
    public static final int LIGHT_BLUE=3;
    public static final int YELLOW=4;
    public static final int PINK=5;
    public static final int WHITE=6;

    public static final Category DEFAULT_CATEGORY = new Category("DEFAULT",Category.YELLOW);

    private String name;
    private int color;

    public Category(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


    @Override
    public boolean equals(Object o){
        if(! o.getClass().equals(this.getClass())) return false;

        Category c = (Category) o;
        return this.getName().equals(c.getName());
    }
}
