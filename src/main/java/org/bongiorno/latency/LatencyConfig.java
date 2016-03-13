package org.bongiorno.latency;

import com.espertech.esper.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
public class LatencyConfig {



    @Bean
    public EPServiceProvider getProvider() {
        Configuration config = new Configuration();
        config.addEventType(Latency.class);
        EPServiceProvider epService = EPServiceProviderManager.getProvider("LatencyEvent", config);
        epService.initialize();
        return epService;
    }

    @Bean
    public EPRuntime getRunTime(EPServiceProvider epService) {
        return epService.getEPRuntime();
    }

    @Bean
    public EPAdministrator getAdmin(EPServiceProvider epService) {
        return epService.getEPAdministrator();
    }

}
