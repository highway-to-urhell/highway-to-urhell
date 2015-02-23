package io.highway.to.urhell.configuration;

import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jersey.JerseyApiReader;
import com.wordnik.swagger.reader.ClassReaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;
import javax.inject.Named;

@Named
@Lazy(false)
public class SwaggerConfiguration {

    @Value("${h2h.api.baseurl:http://localhost:8080/api}")
    private String apiBaseURL;

    @PostConstruct
    public void init() {
        SwaggerConfig config = new SwaggerConfig();
        config.setApiVersion("1.0");
        config.setBasePath(apiBaseURL);
        ConfigFactory.setConfig(config);
        ScannerFactory.setScanner(new DefaultJaxrsScanner());
        ClassReaders.setReader(new JerseyApiReader());

    }
}
