<?php
namespace H2h\domain;
use \JsonSerializable;
class MessageBreaker implements JsonSerializable{


	private $pathClassMethodName;
	private $token;
    private $dateIncoming;

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