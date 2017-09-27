package com.fleury.metrics.agent.transformer.asm.injectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static com.fleury.metrics.agent.reporter.TestMetricReader.TimerResult;

import com.fleury.metrics.agent.annotation.Timed;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 *
 * @author Will Fleury
 */
public class TimerInjectorTest extends BaseMetricTest {

    @Test
    public void shouldTimeConstructorInvocation() throws Exception {
        Class<TimedConstructorClass> clazz = execute(TimedConstructorClass.class);

        Object obj = clazz.newInstance();

        TimerResult value = metrics.getTimes("constructor");
        assertEquals(1, value.count);
        assertTrue(value.sum >= TimeUnit.NANOSECONDS.toMillis(10L));
    }

    @Test
    public void shouldTimeMethodInvocation() throws Exception {
        Class<TimedMethodClass> clazz = execute(TimedMethodClass.class);

        Object obj = clazz.newInstance();

        obj.getClass().getMethod("timed").invoke(obj);

        TimerResult value = metrics.getTimes("timed");
        assertEquals(1, value.count);
        assertTrue(value.sum >= TimeUnit.NANOSECONDS.toMillis(10L));
    }

    @Test
    public void shouldTimeMethodWithLabelsInvocation() throws Exception {
        Class<TimedMethodWithLabelsClass> clazz = execute(TimedMethodWithLabelsClass.class);

        Object obj = clazz.newInstance();

        obj.getClass().getMethod("timed").invoke(obj);

        TimerResult value = metrics.getTimes("timed", new String[] {"name1"}, new String[]{"value1"});
        assertEquals(1, value.count);
        assertTrue(value.sum >= TimeUnit.NANOSECONDS.toMillis(10L));
    }

    @Test
    public void shouldTimeMethodInvocationWhenExceptionThrown() throws Exception {
        Class<TimedMethodClassWithException> clazz = execute(TimedMethodClassWithException.class);

        Object obj = clazz.newInstance();

        boolean exceptionOccured = false;
        try {
            obj.getClass().getMethod("timed").invoke(obj);
        }
        catch (InvocationTargetException e) {
            exceptionOccured = true;
        }

        assertTrue(exceptionOccured);

        TimerResult value = metrics.getTimes("timed");
        assertEquals(1, value.count);
        assertTrue(value.sum >= TimeUnit.NANOSECONDS.toMillis(10L));
    }

    public static class TimedConstructorClass {

        @Timed(name = "constructor")
        public TimedConstructorClass() {
            try {
                Thread.sleep(10L);
            }
            catch (InterruptedException e) {
            }
        }
    }

    public static class TimedMethodClass {

        @Timed(name = "timed")
        public void timed() {
            try {
                Thread.sleep(10L);
            }
            catch (InterruptedException e) {
            }
        }
    }

    public static class TimedMethodWithLabelsClass {

        @Timed(name = "timed", labels = {"name1:value1"})
        public void timed() {
            try {
                Thread.sleep(10L);
            }
            catch (InterruptedException e) {
            }
        }
    }

    public static class TimedMethodClassWithException {

        @Timed(name = "timed")
        public void timed() {
            try {
                Thread.sleep(10L);
                callService();
            }
            catch (InterruptedException e) {
            } 
        }
        
        private void callService() {
            BaseMetricTest.performBasicTask();
            throw new RuntimeException();
        }
    }
}
