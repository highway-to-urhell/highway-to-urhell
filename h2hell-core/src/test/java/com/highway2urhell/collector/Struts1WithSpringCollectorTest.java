package com.highway2urhell.collector;

import com.github.javaparser.ParseException;
import com.highway2urhell.transformer.Struts1WithSpringTransformer;
import org.junit.Test;

import java.io.FileNotFoundException;

import static com.highway2urhell.utils.ParsingUtil.extractBody;
import static com.highway2urhell.utils.ParsingUtil.extractPackages;
import static org.fest.assertions.Assertions.assertThat;

public class Struts1WithSpringCollectorTest {
    @Test
    public void checkScript() throws FileNotFoundException, ParseException {
        assertThat(Struts1WithSpringTransformer.collectBody())
                .isEqualTo(extractBody("./src/test/java/com/highway2urhell/collector/Struts1WithSpringCollector.java", "collectBody"));
        assertThat(Struts1WithSpringTransformer.collectPackages())
                .isEqualTo(extractPackages("./src/test/java/com/highway2urhell/collector/Struts1WithSpringCollector.java"));
    }
}
