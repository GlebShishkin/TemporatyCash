package ru.stepup.geometry;

public class Start
{
    public static void main(String[] args) throws InterruptedException {
        Fraction fr = new Fraction(1, 3);
        FractionInvocationHandler h = new FractionInvocationHandler((Fractionable) fr);
        Fractionable fraction = Utils.cashProxy(fr, h);

        System.out.println(h.printtCash());

        System.out.println(fraction.doubleValue());
        fraction.setNum(5);
        System.out.println(fraction.doubleValue());

        System.out.println(h.printtCash());

        Thread.sleep(500);
        fraction.setNum(10);
        System.out.println(fraction.doubleValue());
        System.out.println(h.printtCash());

        Thread.sleep(2000);
        System.out.println(h.printtCash());

        h.stopClearThread();
    }
}
