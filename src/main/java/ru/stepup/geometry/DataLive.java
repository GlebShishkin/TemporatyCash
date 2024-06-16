package ru.stepup.geometry;

// класс для кэширования значения дроби: значение/время последнего обращения
public class DataLive {
    private Double doubleValCash;   // кэшируемая на уровне экземпляра класса величина дроби
    private long lastTime;    // время послед. обращения - определеят время жизни

    public DataLive(Double doubleValCash, long lastTime) {
        this.doubleValCash = doubleValCash; // кэшируемое значение дроби
        this.lastTime = lastTime;       // время последнего обращения
    }

    public Double getDoubleValCash() {
        return doubleValCash;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public boolean isLive(long currentTimeMillis, long default_timeout) {
         if (currentTimeMillis < (this.lastTime + default_timeout))
            return false;
         return true;
    }

}
