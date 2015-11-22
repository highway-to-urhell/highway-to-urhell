package com.highway2urhell;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class VersionUtils {

    public static final String UNKNOWN_VERSION = "UNKNOWN";
    public static final String NO_FRAMEWORK = "NO_FRAMEWORK";

    private VersionUtils() {
    }

    public static String getVersion(String clazzName, String groupId, String artifactId) {
        //check
        Class<?> clazz;
        try {
            clazz = Class.forName(clazzName);
        } catch (ClassNotFoundException e1) {
            return NO_FRAMEWORK;
        }
        String version = null;

        // try to load from maven properties first
        Properties p = new Properties();
        InputStream is = null;

        try {
            is = clazz.getResourceAsStream("/META-INF/maven/" + groupId + "/" + artifactId + "/pom.properties");
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

        // try to load from manifest
        try {
            URL clazzRes = clazz.getResource('/' + clazz.getName().replace('.', '/') + ".class");
            String classResPath = clazzRes.getPath();
            Manifest manifest = null;
            if (classResPath.contains("!")) {
                classResPath = classResPath.split("!")[0];
                URL url = new URL("jar:" + classResPath + "!/");
                JarURLConnection jarConnection = (JarURLConnection) url.openConnection();
                manifest = jarConnection.getManifest();
            } else {
                is = clazz.getResourceAsStream(classResPath + "!/META-INF/MANIFEST.MF");
                if (is != null)
                    manifest = new Manifest(is);

            }
            if (manifest != null) {
                Attributes attr = manifest.getMainAttributes();
                version = attr.getValue("Implementation-Version");
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
