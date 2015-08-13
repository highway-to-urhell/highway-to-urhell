package com.highway2urhell.transformer;


import javassist.CtClass;
import javassist.CtMethod;

public class JmsQueueTransformer extends AbstractLeechTransformer {

    public JmsQueueTransformer() {
        super("javax/naming/InitialContexty");
        addImportPackage( "java.util",
                "java.util.Map",
                "javax.jms.Queue",
                "javax.jms.JMSException");
    }

    @Override
    protected void doTransform(CtClass cc) throws Exception {
        CtMethod c = cc.getMethod("init", "(Ljava.util.Hashtable;)V");
        String h2hHookCode = "" +
                "List listEntryPath = new ArrayList();"+
                "Iterator iter = myProps.entrySet().iterator();"+
                "while (iter.hasNext()) {"+
                "   java.util.Map.Entry element = (java.util.Map.Entry) iter.next();"+
                "   String key = (String) element.getValue();"+
                "   Object obj = lookup(key);"+
                "   if (obj instanceof javax.jms.Queue) {"+
                "       javax.jms.Queue queueTmp = (Queue) obj;"+
                "       EntryPathData entry = new EntryPathData();"+
                "       entry.setMethodName(\"no-method\");"+
                "       entry.setClassName(\"queue-jms\");"+
                "       entry.setTypePath(TypePath.DYNAMIC);"+
                "       entry.setAudit(false);"+
                "       try {"+
                "           entry.setUri(queueTmp.getQueueName());"+
                "       } catch (JMSException e) {"+
                "           entry.setUri(\"queue\");"+
                "           e.printStackTrace();"+
                "       }"+
                "       listEntryPath.add(entry);"+
                "    }"+
                "}"+
                "CoreEngine.getInstance().getFramework(\"JMS_11_CTX\").receiveData(listEntryPath);";
        c.insertAfter(h2hHookCode);
    }

}