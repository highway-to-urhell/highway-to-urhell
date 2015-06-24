package io.highway.to.urhell.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ThunderData {

    @Override
	public String toString() {
		return "ThunderData [counter=" + counter + ", listDate=" + listDate
				+ ", methodName=" + methodName + "]";
	}

	private final AtomicLong counter;
    private final List<Date> listDate;
    private final String methodName;
  
	public ThunderData(String methodName) {
        this.methodName = methodName;
        counter = new AtomicLong();
        listDate = new ArrayList<Date>();
    }

    public String getMethodName() {
        return methodName;
    }

    public Long getCount() {
        return counter.get();
    }

    public long incrementCounter() {
        listDate.add(new Date());
        return counter.incrementAndGet();
    }

    public List<Date> getListDate() {
        return listDate;
    }

}
