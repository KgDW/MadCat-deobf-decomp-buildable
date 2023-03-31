package me.madcat.event.events;

import me.madcat.event.EventStage;

public class RenderItemEvent
extends EventStage {
    double mainX;
    double mainY;
    double mainZ;
    double offX;
    double offY;
    double offZ;
    double mainRAngel;
    double mainRx;
    double mainRy;
    double mainRz;
    double offRAngel;
    double offRx;
    double offRy;
    double offRz;
    double mainHandScaleX;
    double mainHandScaleY;
    double mainHandScaleZ;
    double offHandScaleX;
    double offHandScaleY;
    double offHandScaleZ;

    public RenderItemEvent(double d, double d2, double d3, double d4, double d5, double d6) {
        this.mainX = d;
        this.mainY = d2;
        this.mainZ = d3;
        this.offX = d4;
        this.offY = d5;
        this.offZ = d6;
    }

    public double getMainX() {
        return this.mainX;
    }

    public void setMainX(double d) {
        this.mainX = d;
    }

    public double getMainY() {
        return this.mainY;
    }

    public void setMainY(double d) {
        this.mainY = d;
    }

    public double getMainZ() {
        return this.mainZ;
    }

    public void setMainZ(double d) {
        this.mainZ = d;
    }

    public double getOffX() {
        return this.offX;
    }

    public void setOffX(double d) {
        this.offX = d;
    }

    public double getOffY() {
        return this.offY;
    }

    public void setOffY(double d) {
        this.offY = d;
    }

    public double getOffZ() {
        return this.offZ;
    }

    public void setOffZ(double d) {
        this.offZ = d;
    }

    public double getMainRAngel() {
        return this.mainRAngel;
    }

    public void setMainRAngel(double d) {
        this.mainRAngel = d;
    }

    public double getMainRx() {
        return this.mainRx;
    }

    public void setMainRx(double d) {
        this.mainRx = d;
    }

    public double getMainRy() {
        return this.mainRy;
    }

    public void setMainRy(double d) {
        this.mainRy = d;
    }

    public double getMainRz() {
        return this.mainRz;
    }

    public void setMainRz(double d) {
        this.mainRz = d;
    }

    public double getOffRAngel() {
        return this.offRAngel;
    }

    public void setOffRAngel(double d) {
        this.offRAngel = d;
    }

    public double getOffRx() {
        return this.offRx;
    }

    public void setOffRx(double d) {
        this.offRx = d;
    }

    public double getOffRy() {
        return this.offRy;
    }

    public void setOffRy(double d) {
        this.offRy = d;
    }

    public double getOffRz() {
        return this.offRz;
    }

    public void setOffRz(double d) {
        this.offRz = d;
    }

    public double getMainHandScaleX() {
        return this.mainHandScaleX;
    }

    public void setMainHandScaleX(double d) {
        this.mainHandScaleX = d;
    }

    public double getMainHandScaleY() {
        return this.mainHandScaleY;
    }

    public void setMainHandScaleY(double d) {
        this.mainHandScaleY = d;
    }

    public double getMainHandScaleZ() {
        return this.mainHandScaleZ;
    }

    public void setMainHandScaleZ(double d) {
        this.mainHandScaleZ = d;
    }

    public double getOffHandScaleX() {
        return this.offHandScaleX;
    }

    public void setOffHandScaleX(double d) {
        this.offHandScaleX = d;
    }

    public double getOffHandScaleY() {
        return this.offHandScaleY;
    }

    public void setOffHandScaleY(double d) {
        this.offHandScaleY = d;
    }

    public double getOffHandScaleZ() {
        return this.offHandScaleZ;
    }

    public void setOffHandScaleZ(double d) {
        this.offHandScaleZ = d;
    }
}

