package com.anz.circuitbreaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableCircuitBreaker
@RestController
@SpringBootApplication
public class SpringApp {
  @Autowired
  private AlbumService albumService;

  @RequestMapping("/albums")
  public String albums() {
    return albumService.doSomething();
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringApp.class, args);
  }
}
