package com.builtinmenlo.selfieclub.dataSources;

/**
 * Created by Leonardo on 6/19/14.
 */
public class Friend extends User {
    private int state;
    private boolean selected;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
