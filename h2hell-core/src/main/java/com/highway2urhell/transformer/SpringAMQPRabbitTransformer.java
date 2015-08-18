package com.highway2urhell.transformer;

import javassist.*;

public class SpringAMQPRabbitTransformer extends AbstractLeechTransformer {
    public SpringAMQPRabbitTransformer() {
        super("org/springframework/amqp/rabbit/listener/SimpleMessageListenerContainer");
        addImportPackage("java.util",
                    "java.util.Map",
                    "com.highway2urhell.service.impl",
                    "java.lang.reflect",
                    "org.springframework.amqp.rabbit.listener.adapter",
                    "org.springframework.amqp.remoting.service.AmqpInvokerServiceExporter",
                    "org.springframework.messaging.handler.invocation.InvocableHandlerMethod"
                );
    }

    @Override
    protected void doTransform(CtClass cc) throws Exception {
        CtMethod m = cc.getMethod("doStart",
                "()V");

        String h2hHookCode = "List listEntryPath = new ArrayList();" +
                "EntryPathData entry = new EntryPathData();" +
                "entry.setTypePath(TypePath.LISTENER);" +
                "Object ml = getMessageListener();" +
                "Class targetClass = null;" +
                "Method targetMethod = null;" +
                "String methodName = null;" +
                "if(ml instanceof MessageListenerAdapter)" +
                "{" +
                    "MessageListenerAdapter mla = (MessageListenerAdapter) ml;" +
                    "Field fd = mla.getClass().getDeclaredField(\"delegate\");" +
                    "fd.setAccessible(true);" +
                    "Object delegate = fd.get(mla);" +
                    "targetClass = delegate.getClass();" +
                    "Field f = mla.getClass().getDeclaredField(\"defaultListenerMethod\");" +
                    "f.setAccessible(true);" +
                    "methodName = (String)f.get(mla);" +
                "} else if(ml instanceof MessagingMessageListenerAdapter) {" +
                    "MessagingMessageListenerAdapter mmla = (MessagingMessageListenerAdapter) ml;" +
                    "Field fd = mmla.getClass().getDeclaredField(\"handlerMethod\");" +
                    "fd.setAccessible(true);" +
                    "InvocableHandlerMethod handlerMethod = (InvocableHandlerMethod)(fd.get(mmla));" +
                    "targetClass = handlerMethod.getBeanType();" +
                    "targetMethod = handlerMethod.getMethod();" +
                    "methodName = targetMethod.getName();" +
                "} else if(ml instanceof AmqpInvokerServiceExporter) {" +
                    "AmqpInvokerServiceExporter aise = (AmqpInvokerServiceExporter) ml;" +
                    "targetClass = aise.getMessageConverter().getClass();" +
                    "methodName = \"fromMessage\";" +
                "}" +
                "String className = targetClass.toString();" +
                "if (className.contains(\"class \")) {" +
                    "className = className.replace(\"class \", \"\");" +
                "}" +
                "entry.setClassName(className);" +
                "entry.setMethodName(methodName);" +
                "if(targetMethod == null) {" +
                    "Method[] tabMethod = targetClass.getDeclaredMethods();" +
                    "for (int i = 0; i < tabMethod.length; i++) {" +
                        "if (tabMethod[i].getName().equals(methodName)) {" +
                          "targetMethod = tabMethod[i];" +
                        "}" +
                    "}" +
                "}" +
                "String internalSignature = \"\";" +
                "if(targetMethod != null)" +
                    "internalSignature = org.objectweb.asm.Type.getMethodDescriptor(targetMethod);" +
                "entry.setSignatureName(internalSignature);" +
                "listEntryPath.add(entry);" +
                "CoreEngine.getInstance().getFramework(\"SPRING_AMQP\").receiveData(listEntryPath);";
        m.insertAfter(h2hHookCode);
    }

}
