<?php

namespace 'insert_path'\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;

// these import the "@Route" and "@Template" annotations
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Route;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Template;
//H2H !
use H2h\service\H2hservice;

class H2hController extends Controller
{
	
	/**
     * @Route("/h2h/launch", name="_h2h_transformer")
     * @Template()
     */
    public function launchAction(Request $request)
    {
    	$h2service = new H2hservice();
		return new Response($h2service->initH2HServer());
	}

    /**
     * @Route("/h2h/checkPath", name="_h2h")
     * @Template()
     */
    public function checkPathAction(Request $request)
    {
        $h2service = new H2hservice();
        return new Response($h2service->checkPath());
     }
}
