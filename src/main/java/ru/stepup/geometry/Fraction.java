package ru.stepup.geometry;

import ru.stepup.geometry.annotation.Cache;
import ru.stepup.geometry.annotation.Mutator;

public class Fraction implements Fractionable {
    private int num;
    private int denum;

    public Fraction(int num, int denum) {
        this.num = num;
        setDenum(denum);
    }

    @Mutator
    public void setNum(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    @Mutator
    public void setDenum(int denum) {
        if (denum == 0) {
            throw new IllegalArgumentException("Делитель не может быть равным нулю");
        }
        this.denum = denum;
    }

    public int getDenum() {
        return denum;
    }

    @Override
    @Cache(timeout=1000)
    public double doubleValue() {
        System.out.println("    Вызов Fraction.doubleValue для обновления кэшированого значения");
        return (double) num/denum;
    }
}
