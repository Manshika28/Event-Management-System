
package com.example.userservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// simple Feign client to event-service; fallback handled via circuit breaker in service layer
@FeignClient(name = "event-service", url = "http://localhost:8081")
public interface EventClient {
    @GetMapping("/events/{id}")
    Object getEvent(@PathVariable("id") Long id);
}
