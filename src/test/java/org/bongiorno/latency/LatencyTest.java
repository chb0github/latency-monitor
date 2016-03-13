package org.bongiorno.latency;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPStatement;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.stream;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,classes = {LatencyConfig.class, LatencyTest.ContextConfiguration.class})
@ComponentScan
public class LatencyTest {

    @Autowired
    private SomeClass someClass;

    @Autowired
    private EPAdministrator runtime;

    @After
    public void tearDown() throws Exception {
        runtime.destroyAllStatements();
    }

    @Test
    public void testSimple() throws Exception {
        EPStatement statement = runtime.createEPL("select * from Latency");
        final List<Latency> count = new LinkedList<>();
        statement.addListener((o, n) -> stream(o).forEach(e -> count.add((Latency) e.getUnderlying())));
        someClass.doSomething("foo");
        Method[] methods = someClass.getClass().getMethods();

        assertEquals(1,count.size());
        Latency expected = new Latency(SomeClass.class,"doSomething",count.get(0).getDuration(),null);
        assertEquals(expected,count.get(0));


    }

    @Test
    public void testLogging() throws Exception {
        Logger log = mock(Logger.class);
        EPStatement statement = runtime.createEPL("select * from Latency");
        statement.addListener(new LatencyLogger(log));
        someClass.doSomething("foo");
        verify(log).info(eq("%s"),any(Latency.class));

    }
    @Configuration
    static class ContextConfiguration {
        @Bean
        public SomeClass properties() {
            return new SomeClass();
        }
    }


    public static class SomeClass {

        @Monitor
        public String doSomething(String whatever) {
            return "done";
        }
    }
}