package com.highway2urhell.agent;

import java.lang.instrument.Instrumentation;

public class InstrumentationHolder {

    private static InstrumentationHolder instance = null;
    private Instrumentation inst = null;

    private InstrumentationHolder() {
    }

    public static InstrumentationHolder getInstance() {
        if (instance == null) {
            synchronized (InstrumentationHolder.class) {
                if (instance == null) {
                    instance = new InstrumentationHolder();
                }
            }
        }
        return instance;
    }

    public void persistInMemory(Instrumentation instrumentation) {
        inst = instrumentation;
    }

    public Instrumentation getInst() {
        return inst;
    }
}
