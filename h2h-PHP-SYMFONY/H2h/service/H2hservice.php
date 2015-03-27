<?php
namespace H2h\service;

use \RecursiveIteratorIterator; 
use \RecursiveDirectoryIterator;
use \DateTime;
use H2h\domain\H2hConfig;
use H2h\domain\BreakerData;
use H2h\domain\MessageThunderApp;
use H2h\domain\MessageBreaker;

class H2hservice {
	
	public static $tokenKey = "";


	public function initH2HServer(){
		$res="<html><body>";
		$msgConfig=$this->initAppz();
		$msgRes="Configuration H2Hconfig".json_encode($msgConfig);
		$res=$res.$msgRes."</br>";
		$msgAllPath=$this->initAllPathData($msgConfig->token);
		$res=$res.$msgAllPath."</br>";
		$msgProbe=$this->addProbe();
		$res=$res.$msgProbe."</br>";
		$res=$res."</body></html>";
		return $res;
	}

	private function initAppz(){
		$h2conf  = new H2hConfig();
		$h2conf->nameApplication="namePhpTest";
		$h2conf->description="namePhpTest Desc";
		$h2conf->urlApplication="http://www.google.fr";
		$h2conf->version="namePhpTest version";
		$h2conf->pathSource="namePhpTest pathSource";
		$context = stream_context_create(array(
    		'http' => array(
       		'method' => 'POST',
       		'header' => "Content-Type: application/json\r\n",
      	    'content' => json_encode($h2conf))
		));
 
		//Utilisation du contexte dans l'appel
		$responseH2h = file_get_contents('http://localhost:8090/core/api/ThunderApp/createThunderApp/', false, $context);
		$h2conf->token=$responseH2h;
		self::$tokenKey=$h2conf->token;
		return $h2conf;
	}

	private function initAllPathData(){
		//dirty global $kernel bit it's works !
		global $kernel;
		$msg = new MessageThunderApp();
		$msg->token=self::$tokenKey;
		$availableApiRoutes = array();
    	foreach ($kernel->getContainer()->get('router')->getRouteCollection()->all() as $name => $route) {
            foreach ($route->getDefaults() as $keyDefaults => $valueDefaults) {
            	$classNameArray = explode(":", $valueDefaults);
            	$classNameElem;
            	$methodNameElem= $classNameArray[0];
            	for ($i=0; $i <count($classNameArray) ; $i++) { 
            		$tmpVal = $classNameArray[$i];
            		if($tmpVal !="" && $tmpVal!=null){
            			$classNameElem=$tmpVal;
            		}
            	}
            	$breaker = new BreakerData();
            	$breaker->className = $methodNameElem;
            	$breaker->methodName =$classNameElem;
            	$breaker->uri = $route->getPath();
            	$breaker->httpMethod= "unknow";
            	array_push($availableApiRoutes,$breaker);
            }
         }
 		 $msg->listBreakerData=$availableApiRoutes;
 		 $context = stream_context_create(array(
	    	'http' => array(
	        'method' => 'POST',
	        'header' => "Content-Type: application/json\r\n",
	        'content' => json_encode($msg))
		));
 
		//Utilisation du contexte dans l'appel
		$responseH2h = file_get_contents('http://localhost:8090/core/api/ThunderApp/initThunderApp/', false, $context);
		$msgRes=count($availableApiRoutes)." items sent to H2hServer";
		return $msgRes;
 		 
	}

	public function extractClassNameFromFileName($fileName){
		$arrayFile = explode("/", $fileName);
		$classNameExtension = $arrayFile[count($arrayFile)-1];
		$className = explode(".",$classNameExtension)[0];
		return $className;
	}

	public function extractMethodName($lineMetod){
		$arrayFunction = explode("(", $lineMetod)[0];
		$methodName = explode("function ",$arrayFunction)[1];
		return $methodName;
	}

	private function addProbe(){
		$resArray = $this->searchPathForClassName();
    	$resHtml="";
    	foreach($resArray as $element){
    		$res="";
    		$fileName = $element;
    		//print_r($element);
			if ( !file_exists($fileName)) {
   			     $resHtml=$resHtml."</br>"."File not found".$fileName;
    		}elseif ( strpos($fileName,'H2hController') !== false) {
    			 $resHtml=$resHtml."</br>"."File not autorized".$fileName;
    		}
			else {
				$handle = fopen($fileName, "r");
				$funct = false;
				$brace = false;
				$import = false;
				$methodsearch;
			    while (($line = fgets($handle)) !== false) {
			    	$oldLine = $line;
			    	if(strpos($line,'public function ') !== false){
			    		$funct =true;
			    		$methodsearch=$this->extractMethodName($oldLine);
			    	}
			    	if(strpos($line,'{') !== false && $funct ==true){
			    		$brace = true;
			    	}
			    	if($brace == true && $funct == true){
			    		$res=$res.$line." \$h2breaker = new H2hBreakerservice(); ";
			    		
			    		$res=$res."\$h2breaker->execCmdData(\"".$this->extractClassNameFromFileName($fileName)."\",\"".$methodsearch."\",\"".self::$tokenKey."\");\n";
			    		$funct = false;
						$brace = false;
			    	}else{
			    		if(strpos($line,'namespace') !== false){
			    			$res=$res.$line."\n use H2h\service\H2hBreakerservice ; \n";
			    		}else{
			    			$res=$res.$line;
			    		}
			    	}
			    }

			    fclose($handle);
				$fileDest = fopen($fileName, "w");
				fwrite($fileDest, $res);
				fclose($fileDest);
				$resHtml=$resHtml."Add Probe -> Update File".$fileName."</br>";
			}
    	}
    	return $resHtml;
	}
	/**
	 *Check Path -> HTML version
	 */
	public function checkPath(){
		//dirty global $kernel bit it's works !
		global $kernel;
		$availableApiRoutes = array();
    	$res="<html><head><meta charset=\"utf-8\"></head><style type=\"text/css\">body{ font-size:18px; color:#FFF; }";
        $res=$res.".classh2h {text-shadow:5px 1px 11px #1a45f0} ";
        $res=$res.".classname { border:solid 1px #2d2d2d;  text-align:center; background:#575757; padding:50px 10px 20px 20px;  -moz-border-radius: 5px;  -webkit-border-radius: 5px; border-radius: 5px;}";
        $res=$res."table.gridtable {	font-family: verdana,arial,sans-serif;	font-size:11px;	color:#333333;	border-width: 1px;	border-color: #666666;	border-collapse: collapse;} ";
        $res=$res."table.gridtable tr{	border-width: 1px;	padding: 8px;	border-style: solid;	border-color: #666666;	background-color: #dedede ;} ";
        $res=$res."table.gridtable td {	border-width: 1px;	padding: 8px;	border-style: solid;	border-color: #666666;	background-color: #ffffff;}";
        $res=$res."</style><body>)";
		$res=$res."<div id=\"gibson\" class=\"classname\">";
       	$res=$res."<table class=\"gridtable\">";
       	$res=$res."<tr><td>Class Name</td><td>Method Entry</td><td>Uri Path</td></tr>";

        foreach ($kernel->getContainer()->get('router')->getRouteCollection()->all() as $name => $route) {
            foreach ($route->getDefaults() as $keyDefaults => $valueDefaults) {
            	$classNameArray = explode(":", $valueDefaults);
            	$className ;
            	$methodName = $classNameArray[0];
            	for ($i=0; $i <count($classNameArray) ; $i++) { 
            		$tmpVal = $classNameArray[$i];
            		if($tmpVal !="" && $tmpVal!=null){
            			$className=$tmpVal;
            		}
            	}
            	$res=$res."<tr><td>".$methodName."</td><td>".$className."</td><td>".$route->getPath()."</td></tr>";
            }
           
           
 		 }
 		  $res=$res.'</table></body></html>';
 		  return $res;
	}


	private function searchPathForClassName(){
		//dirty global $kernel bit it's works !
		global $kernel;
		$iter = new RecursiveIteratorIterator(
   			 new RecursiveDirectoryIterator($kernel->getRootDir()."/../", RecursiveDirectoryIterator::SKIP_DOTS),
    				RecursiveIteratorIterator::SELF_FIRST,
    				RecursiveIteratorIterator::CATCH_GET_CHILD // Ignore "Permission denied"
			);
		$availableFiles = array();
		foreach (new RecursiveIteratorIterator(new RecursiveDirectoryIterator($kernel->getRootDir()."/../")) as $filename){
        	array_push($availableFiles, $filename);
		}	
		
		$resultArray = array();
		$controllerArray = $this->findAllNameController();
		foreach ($availableFiles as $itemFile) {
			foreach ($controllerArray as $itemCtrl) {
				//print_r($itemFile."-".$itemCtrl."</br>");
				if(strpos($itemFile,$itemCtrl) !==false){
					array_push($resultArray, $itemFile);
				
				}
			}
		}
		return array_unique($resultArray);
	}

	private function findAllNameController(){
		//dirty global $kernel bit it's works !
		global $kernel;
		$availableApiRoutes = array();
		foreach ($kernel->getContainer()->get('router')->getRouteCollection()->all() as $name => $route) {
            foreach ($route->getDefaults() as $keyDefaults => $valueDefaults) {
            	$fullclassNameArray = explode(":", $valueDefaults);
            	$className ;
            	$methodName = $fullclassNameArray[0];
            	for ($i=0; $i <count($fullclassNameArray)-1 ; $i++) { 
            		$tmpVal = $fullclassNameArray[$i];
            		if($tmpVal !="" && $tmpVal!=null &&strpos($tmpVal,'ontroller') !== false){
            			$classNameArray = explode("\\",$tmpVal);
            			$className = $classNameArray[count($classNameArray)-1];
            			array_push($availableApiRoutes, $className);
            		}
            	}
            }
         }
         $result = array_unique($availableApiRoutes);
 		 return $result;
	}

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

}