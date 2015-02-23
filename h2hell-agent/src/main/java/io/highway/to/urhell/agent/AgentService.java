package io.highway.to.urhell.agent;

import java.lang.instrument.Instrumentation;

public class AgentService {
	
	private static AgentService instance = null;
	private Instrumentation inst = null;
	private AgentService(){
		
	}
	
	public static AgentService getInstance(){
		if(instance ==null){
			synchronized (AgentService.class) {
				if(instance == null){
					instance = new AgentService();
				}
			}
		}
		return instance;
	}
	
	public void persistInMemory(Instrumentation instrumentation){
    	inst = instrumentation;
    }

	public Instrumentation getInst() {
		return inst;
	}

	
}
