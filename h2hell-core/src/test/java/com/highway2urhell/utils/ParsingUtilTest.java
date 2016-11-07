package com.highway2urhell.utils;

import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.fest.assertions.Assertions.assertThat;

public class ParsingUtilTest {

    @Test
    public void should_extract_method() throws Exception {
        String source = "import org.junit.Test;\n" +
                "\n" +
                "public class JPTest {\n" +
                "\n" +
                "    @Test\n" +
                "    public void should_extract_method() {\n" +
                "        System.out.println(\"toto\");\n" +
                "    }\n" +
                "}\n";

        String should_extract_method = ParsingUtil.extractBody(new ByteArrayInputStream(source.getBytes()), "should_extract_method");
        assertThat(should_extract_method)
                .isEqualTo("{\n" +
                        "    System.out.println(\"toto\");\n" +
                        "}");
    }

    @Test
    public void should_extract_imports() throws Exception {
        assertThat(ParsingUtil.extractImports("src/test/java/com/highway2urhell/utils/ParsingUtilTest.java")).contains(
                "org.junit.Test",
                "java.io.ByteArrayInputStream",
                "org.fest.assertions.Assertions.assertThat"
        );
    }

    @Test
    public void should_extract_packages() throws Exception {
        assertThat(ParsingUtil.extractPackages("src/test/java/com/highway2urhell/utils/ParsingUtilTest.java")).contains(
                "org.junit",
                "java.io",
                "org.fest.assertions.Assertions"
        );
    }
}
