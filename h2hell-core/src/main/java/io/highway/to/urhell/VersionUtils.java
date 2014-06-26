package io.highway.to.urhell;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class VersionUtils {

    private static final String UNKNOWN_VERSION = "UNKNOWN";

    private VersionUtils() {
    }

    public static String getVersion(Class<?> clazz, String groupId,
            String artifactId) {
        String version = null;
        // try to load from maven properties first
        Properties p = new Properties();
        InputStream is = null;

        try {
            is = clazz.getResourceAsStream("/META-INF/maven/" + groupId + "/"
                    + artifactId + "/pom.properties");
            if (is != null) {
                p.load(is);
                version = p.getProperty("version", "");
            }
        } catch (Exception e) {
            // ignore
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
        // fallback to using Java API
        if (version == null) {
            Package aPackage = clazz.getPackage();
            if (aPackage != null) {
                version = aPackage.getImplementationVersion();
                if (version == null) {
                    version = aPackage.getSpecificationVersion();
                }
            }
        }
        if (version == null) {
            // we could not compute the version so use a blank
            version = UNKNOWN_VERSION;
        }

        return version;
    }

}
