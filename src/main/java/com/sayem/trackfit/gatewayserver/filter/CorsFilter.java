//package com.sayem.trackfit.gatewayserver.filter;
//
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.WebFilterChain;
//
//import lombok.extern.slf4j.Slf4j;
//import reactor.core.publisher.Mono;
//
//@Component
//@Slf4j
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class CorsFilter implements WebFilter{
//	
//
//	@Override
//	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//		
//		log.info("Started executing Cors");
//		
//		// doing cors part temporarility
//		exchange.getResponse().getHeaders().set("Access-Control-Allow-Origin", "http://localhost:4200");
//        exchange.getResponse().getHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//        exchange.getResponse().getHeaders().set("Access-Control-Max-Age", "3600");
//		
//
//        // Handle preflight (OPTIONS) request immediately
//        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
//            
//            exchange.getResponse().setStatusCode(HttpStatus.OK);
//            return exchange.getResponse().setComplete();
//        }
//		
//		return chain.filter(exchange);
//	}
//
//}
