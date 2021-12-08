package de.varsamis.labs.rest2kafka;


import de.varsamis.labs.rest2kafka.routes.RestRouteBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.main.Main;
import org.apache.camel.main.MainListenerSupport;

@Slf4j
public class Bootstrap {

    public static void main(String[] args) {

        Main main = new Main();
        main.addMainListener(new MainListenerSupport() {
            public void configure(CamelContext context) {
                try {
                    context.addRoutes(new RestRouteBuilder());
                } catch (Exception e) {
                    log.error("Error while creating / adding routes. ", e.toString());
                }

            }
        });

        try {
            main.run();
        } catch (Exception e) {
            log.error("General error thrown. Exiting. ", e.toString());
        }
    }
}
