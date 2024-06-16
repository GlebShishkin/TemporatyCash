package ru.stepup.geometry;

import org.junit.jupiter.api.*;

import java.text.DecimalFormat;

public class FractionTest {
    Fraction fr;
    FractionInvocationHandler h;
    Fractionable fraction;
    DecimalFormat df = new DecimalFormat("#.###");

    @BeforeEach
    void setUp() {
        fr = new Fraction(1, 3);
        h = new FractionInvocationHandler((Fractionable) fr);
        fraction = Utils.cashProxy(fr, h);
    }

    @AfterEach
    public void cleanUpEach(){
        h.stopClearThread();
    }

    // Пояснения к тестам:
    // a) Fraction.setNum - сброс кэша и сохранение значений дроби в истории
    // b) после сброса кэша, первый вызов Fraction.doubleValue() - кэширование дроби, второй вызов Fraction.doubleValue() - взятие дроби из кэша
    // c) printtCash печатает содержимое кжша и возврашает количество записей в истории кэша

    // тест обновления кэша и сохранение истории в кэше:
    @Test
    @DisplayName("Test cash updating and saving")
    void saveCashInHistory() {

        Assertions.assertEquals(df.format(0.333), df.format(fraction.doubleValue())); // кэшируем значение при первом вызове: вызываем Fraction..doubleValue()
        Assertions.assertEquals(df.format(0.333), df.format(fraction.doubleValue())); // проверяем значение взятое из кэша
        Assertions.assertEquals(0, h.printtCash()); // проверяем, что история кэша пуста, т.к. не было изменений

        fraction.setNum(5); // при смене данных: сбрасываем кэш + заносим старые данные в кэшируемую историю

        Assertions.assertEquals(df.format(1.667), df.format(fraction.doubleValue())); // кэшируем значение при первом вызове после смены значения: вызываем Fraction..doubleValue()
        Assertions.assertEquals(df.format(1.667), df.format(fraction.doubleValue()));   // проверяем значение взятое из кэша
        // проверяем, что история кэша заполнена, т.к. была смена + печатаем кэш
        Assertions.assertEquals(1, h.printtCash());

        fraction.setNum(10); // при второй смене данных: сбрасываем кэш + заносим старые данные в кэшируемую историю

        Assertions.assertEquals(df.format(3.333), df.format(fraction.doubleValue())); // кэшируем значение при первом вызове после смены значения: вызываем Fraction..doubleValue()
        Assertions.assertEquals(df.format(3.333), df.format(fraction.doubleValue()));   // проверяем значение взятое из кэша
        // проверяем, что история кэша заполнена 2 записями, т.к. дважды была смена (вызов Mutator) + печатаем кэш
        Assertions.assertEquals(2, h.printtCash());
    }

    // тест очищения истории в кэше через промежуток времени (определенный в @Cache(timeout=1000))
    @Test
    @DisplayName("Test clearing the story at the end of time")
    void clearCashInHistory() throws InterruptedException {

        fraction.doubleValue(); // кэшируем значение

        fraction.setNum(5); // при смене данных: сбрасываем кэш + заносим старые данные в кэшируемую историю
        fraction.doubleValue(); // кэшируем значение

        fraction.setNum(10); // при второй смене данных: сбрасываем кэш + заносим старые данные в кэшируемую историю
        fraction.doubleValue(); // кэшируем значение

        // проверяем, что история кэша заполнена 2 записями, т.к. дважды была смена + печатаем кэш
        Assertions.assertEquals(2, h.printtCash());

        Thread.sleep(2000); // делаем задержку больше, чем время хранения истории в кэше

        Assertions.assertEquals(0, h.printtCash()); // проверяем, что история кэша пуста
    }

    // тест обновления "срока жизни" истории кэша
    @Test
    @DisplayName("Test life time of cash history")
    void updateCashInHistory() throws InterruptedException {

        fraction.doubleValue(); // кэшируем значение

        fraction.setNum(5); // при смене данных: сбрасываем кэш + заносим старые данные в кэшируемую историю
        fraction.doubleValue(); // кэшируем значение

        fraction.setNum(10); // при второй смене данных: сбрасываем кэш + заносим старые данные в кэшируемую историю
        fraction.doubleValue(); // кэшируем значение

        // проверяем, что кэш истории заполнен 2 записями, т.к. дважды была смена + печатаем кэш
        Assertions.assertEquals(2, h.printtCash());

        Thread.sleep(700);  // засыпаем на 0.7 секунды

        h.cacheReclaiming();    // продлим время жизни всех элементов кэша истории текущим временем (иммитируем обращения к кэшу)
        Thread.sleep(700);  // после обновления жизни кэша, засыпаем еще на 0.7 секунды
        Assertions.assertEquals(2, h.printtCash()); // проверяем, что кэш не очистился

        // дожидаемся, когда продленный кэш истории тоже очистится
        Thread.sleep(1000);
        Assertions.assertEquals(0, h.printtCash());
    }
}
