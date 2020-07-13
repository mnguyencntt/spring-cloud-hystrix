package com.anz.circuitbreaker;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URI;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

// https://github.com/Netflix/Hystrix/wiki/Configuration
@Service
public class AlbumService {
  private RestTemplate restTemplate = createRestTemplate();

  @HystrixCommand(fallbackMethod = "defaultDoSomething", commandProperties = {
      //
      @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "20000"),
      //
      @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"),
      //
      @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "500"),
      //
      @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "1"),
      //
      @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "1000")})
  public String doSomething() {
    URI uri = URI.create("https://jsonplaceholder.typicode.com/albums");
    return this.restTemplate.getForObject(uri, String.class);
  }

  public String defaultDoSomething() {
    return "{ \"userId\": 1, \"id\": 1, \"title\": \"mock test\" }";
  }

  // @HystrixCommand(
  // //
  // fallbackMethod = "reliable",
  // //
  // threadPoolKey = "getProductThreadPool",
  // //
  // commandKey = "getProductServiceCommand")
  // public String readingList() {
  // URI uri = URI.create("https://jsonplaceholder.typicode.com/albums");
  // return this.restTemplate.getForObject(uri, String.class);
  // }
  //
  // public String reliable() {
  // return "{ \"userId\": 1, \"id\": 1, \"title\": \"mock test\" }";
  // }

  public static RestTemplate createRestTemplate() {
    final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    final Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress("singtelproxy.net.vic", 80));
    requestFactory.setProxy(proxy);
    return new RestTemplate(requestFactory);
  }
}
