package me.madcat.util;

public class Timer {
    private long time = -1L;

    public boolean passedS(double d) {
        return this.passedMs((long)d * 1000L);
    }

    public boolean passedDms(double d) {
        return this.passedMs((long)d * 10L);
    }

    public boolean sleep(long l) {
        if (System.nanoTime() / 1000000L - l >= l) {
            this.reset();
            return true;
        }
        return false;
    }

    public long getMs(long l) {
        return l / 1000000L;
    }

    public boolean passedNS(long l) {
        return System.nanoTime() - this.time >= l;
    }

    public long getPassedTimeMs() {
        return this.getMs(System.nanoTime() - this.time);
    }

    public long convertToNS(long l) {
        return l * 1000000L;
    }

    public boolean passedMs(long l) {
        return this.passedNS(this.convertToNS(l));
    }

    public boolean passedDs(double d) {
        return this.passedMs((long)d * 100L);
    }

    public void reset() {
        this.time = System.nanoTime();
    }

    public void setMs(long l) {
        this.time = System.nanoTime() - this.convertToNS(l);
    }
}

