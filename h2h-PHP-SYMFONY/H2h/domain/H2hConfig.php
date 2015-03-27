<?php
namespace H2h\domain;
use \JsonSerializable;

class H2hConfig implements JsonSerializable{

	private $urlApplication;
	private $nameApplication;
	private $urlH2hWeb;
	private $token;
	private $description;
	private $pathSource;
	private $versionApp;
	

    public function __get($property) {
	    if (property_exists($this, $property)) {
	      return $this->$property;
	    }
  	}

  public function __set($property, $value) {
	  if (property_exists($this, $property)) {
	      $this->$property = $value;
	    }
    return $this;
  	}
  	public function jsonSerialize(){
        return get_object_vars($this);
    }
}