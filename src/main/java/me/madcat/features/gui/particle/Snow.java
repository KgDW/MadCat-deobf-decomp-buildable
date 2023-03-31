package me.madcat.features.gui.particle;

import java.util.Random;
import me.madcat.util.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;

public class Snow {
    private int _x;
    private int _y;
    private int _fallingSpeed;
    private int _size;

    public Snow(int n, int n2, int n3, int n4) {
        this._x = n;
        this._y = n2;
        this._fallingSpeed = n3;
        this._size = n4;
    }

    public int getX() {
        return this._x;
    }

    public void setX(int n) {
        this._x = n;
    }

    public int getY() {
        return this._y;
    }

    public void setY(int n) {
        this._y = n;
    }

    public void Update(ScaledResolution scaledResolution) {
        RenderUtil.drawRect(this.getX(), this.getY(), this.getX() + this._size, this.getY() + this._size, -1714829883);
        this.setY(this.getY() + this._fallingSpeed);
        if (this.getY() > scaledResolution.getScaledHeight() + 10 || this.getY() < -10) {
            this.setY(-10);
            Random random = new Random();
            this._fallingSpeed = random.nextInt(10) + 1;
            this._size = random.nextInt(4) + 1;
        }
    }
}

