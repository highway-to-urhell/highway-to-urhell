package io.highway.to.urhell.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class ThunderStat implements Comparable<ThunderStat>{
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;
	@NotNull
	private Long count;
	@NotNull
	@ManyToOne
	private ThunderApp thunderApp;
	@NotNull
	private String pathClassMethodName;
	@NotNull
	private Boolean falsePositive = false;
	private String uri;
	private String httpmethod;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getHttpmethod() {
		return httpmethod;
	}

	public void setHttpmethod(String httpmethod) {
		this.httpmethod = httpmethod;
	}

	public Boolean getFalsePositive() {
		return falsePositive;
	}

	public void setFalsePositive(Boolean falsePositive) {
		this.falsePositive = falsePositive;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	
	public ThunderApp getThunderApp() {
		return thunderApp;
	}

	public void setThunderApp(ThunderApp thunderApp) {
		this.thunderApp = thunderApp;
	}

	public String getPathClassMethodName() {
		return pathClassMethodName;
	}

	public void setPathClassMethodName(String pathClassMethodName) {
		this.pathClassMethodName = pathClassMethodName;
	}

	@Override
	public int compareTo(ThunderStat obj) {
		return Long.compare( obj.getCount().longValue(),count.longValue());
	}

}
