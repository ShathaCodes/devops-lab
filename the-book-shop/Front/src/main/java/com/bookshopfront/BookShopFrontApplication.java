package com.bookshopfront;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SpringBootApplication
public class BookShopFrontApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookShopFrontApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Data
	@EqualsAndHashCode(callSuper = false)
	@Component
	public class WebFilter implements Filter {

		private String responseHeader = "Response_Token";

		private String extractToken(final HttpServletRequest request) {
			final String token;
			if (request.getHeader(responseHeader) != null) {
				token = request.getHeader(responseHeader);
			} else {
				
				token = UUID.randomUUID().toString().toUpperCase().replace("-", "");
			}
			return token;
		}

		private String extractClientIP(final HttpServletRequest request) {
			final String clientIP;
			if (request.getHeader("X-Forwarded-For") != null) {
				clientIP = request.getHeader("X-Forwarded-For").split(",")[0];
			} else {
				clientIP = request.getRemoteAddr();
			}
			return clientIP;
		}

		@Override
		public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
				throws IOException, ServletException {

			try {
				final String token = extractToken((HttpServletRequest) request);
				final String clientIP = extractClientIP((HttpServletRequest) request);
				MDC.put("ipAddr", clientIP);
				MDC.put("requestId", token);
				((HttpServletResponse) response).addHeader(responseHeader, token);

				chain.doFilter(request, response);

			} finally {
				MDC.clear();
			}
		}
	}

}