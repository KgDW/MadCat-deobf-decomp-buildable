package me.madcat.features.gui.components.items;

import me.madcat.features.Feature;

public class Item
extends Feature {
    protected float x;
    protected float y;
    protected int width;
    protected int height;
    private boolean hidden;

    public Item(String string) {
        super(string);
    }

    public void setLocation(float f, float f2) {
        this.x = f;
        this.y = f2;
    }

    public void drawScreen(int n, int n2, float f) {
    }

    public void mouseClicked(int n, int n2, int n3) {
    }

    public void mouseReleased(int n, int n2, int n3) {
    }

    public void update() {
    }

    public void onKeyTyped(char c, int n) {
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int n) {
        this.width = n;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int n) {
        this.height = n;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean bl) {
        this.hidden = bl;
    }
}

