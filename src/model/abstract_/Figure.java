package model.abstract_;

/**
 * Created by kot on 28.03.18.
 */
public abstract class Figure {
    private boolean color;

    public Figure(boolean color) {
        this.color = color;
    }

    public boolean isColor() {
        return color;
    }

}
