package com.highway2urhell.transformer;

import javassist.CtClass;
import javassist.CtMethod;

public class ActiveMQConnectionFactoryTransformer extends AbstractLeechTransformer {

    public ActiveMQConnectionFactoryTransformer() {
        super("org/apache/activemq/ActiveMQConnectionFactory");
        addImportPackage("java.util",
                "java.util.Map");
    }

    @Override
    protected void doTransform(CtClass cc) throws Exception {
        CtMethod c = cc.getMethod("setBrokerURL", "(Ljava/lang/String;)V");

        String h2hHookCode = "" +
                "List listEntryPath = new ArrayList();" +
                "EntryPathData entry = new EntryPathData();"+
                "entry.setClassName(\"org.apache.activemq.ActiveMQConnectionFactory\");" +
                "entry.setMethodName(\"broker\");" +
                "entry.setUri(brokerURL);" +
                "entry.setAudit(Boolean.FALSE);" +
                "entry.setTypePath(TypePath.DYNAMIC);" +
                "listEntryPath.add(entry);"+
                "CoreEngine.getInstance().getFramework(\"ACTIVEMQ_CONNECTION_FACTORY\").receiveData(listEntryPath);";
        c.insertBefore(h2hHookCode);

    }
}


