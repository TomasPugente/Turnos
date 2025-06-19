package com.grupo12.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.grupo12.helpers.ViewRouteHelper;

@Controller
@RequestMapping("/")
public class HomeController {

	@GetMapping("")
	public String index() {
		return ViewRouteHelper.INDEX;
	}
	
	@GetMapping("/home")
	public RedirectView redirectToHomeIndex() {
		return new RedirectView(ViewRouteHelper.ROUTE_INDEX);
	}

	@GetMapping("/administration")
	public String redirectToAdministration() {
		return ViewRouteHelper.ROUTE_ADMINISTRATION;
	}
}
