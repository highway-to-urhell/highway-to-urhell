<?php
namespace H2h\service;

use \RecursiveIteratorIterator; 
use \RecursiveDirectoryIterator;
use \DateTime;
use H2h\domain\H2hConfig;
use H2h\domain\BreakerData;
use H2h\domain\MessageThunderApp;
use H2h\domain\MessageBreaker;

class H2hBreakerservice {

	public static $arrayCount = array();
	
	
	public function execCmdData($className,$methodName,$tokenkey){
		$classMethodName = $className."\\".$methodName;
		$msg = new MessageBreaker();
		$msg->pathClassMethodName=$classMethodName;
		$msg->token=$tokenkey;
		$date = new DateTime();
		$msg->dateIncoming = $date->format('Y-m-d H:i:s');
		$context = stream_context_create(array(
    		'http' => array(
       		'method' => 'POST',
       		'header' => "Content-Type: application/json\r\n",
      	    'content' => json_encode($msg))
		));
		print_r(json_encode($msg));
 
		//Utilisation du contexte dans l'appel
		$responseH2h = file_get_contents('http://localhost:8090/core/api/ThunderApp/addBreaker/', false, $context);
	}

}