package com.highway2urhell.collector;

import com.github.javaparser.ParseException;
import com.highway2urhell.transformer.Struts2Transformer;
import org.junit.Test;

import java.io.FileNotFoundException;

import static com.highway2urhell.utils.ParsingUtil.extractBody;
import static com.highway2urhell.utils.ParsingUtil.extractPackages;
import static org.fest.assertions.Assertions.assertThat;

public class Struts2CollectorTest {
    @Test
    public void checkScript() throws FileNotFoundException, ParseException {
        assertThat(Struts2Transformer.collectBody())
                .isEqualTo(extractBody("./src/test/java/com/highway2urhell/collector/Struts2Collector.java", "collectBody"));
        assertThat(Struts2Transformer.collectPackages())
                .isEqualTo(extractPackages("./src/test/java/com/highway2urhell/collector/Struts2Collector.java"));
    }
}
