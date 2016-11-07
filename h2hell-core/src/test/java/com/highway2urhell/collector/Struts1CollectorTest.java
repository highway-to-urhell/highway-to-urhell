package com.highway2urhell.collector;

import com.github.javaparser.ParseException;
import com.highway2urhell.transformer.Struts1Transformer;
import org.junit.Test;

import java.io.FileNotFoundException;

import static com.highway2urhell.utils.ParsingUtil.extractBody;
import static com.highway2urhell.utils.ParsingUtil.extractPackages;
import static org.fest.assertions.Assertions.assertThat;

public class Struts1CollectorTest {
    @Test
    public void checkScript() throws FileNotFoundException, ParseException {
        assertThat(Struts1Transformer.collectBody())
                .isEqualTo(extractBody("./src/test/java/com/highway2urhell/collector/Struts1Collector.java", "collectBody"));
        assertThat(Struts1Transformer.collectPackages())
                .isEqualTo(extractPackages("./src/test/java/com/highway2urhell/collector/Struts1Collector.java"));
    }
}
