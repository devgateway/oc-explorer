package org.devgateway.toolkit.web.rest.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.devgateway.toolkit.web.rest.entity.Dummy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author mihai
 *
 */
@RestController
public class DummyController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@RequestMapping("/dummy")
	public Dummy greeting(
			@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Dummy(counter.incrementAndGet(), String.format(template,
				name));
	}
}