package com.talentstream.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.talentstream.service.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;

	public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String authorizationHeader = request.getHeader("Authorization");
		String requestPath = request.getServletPath();

		// Skip token validation for public CodeLab endpoints to prevent any loading issues
		boolean isPublicEndpoint = requestPath.startsWith("/api/questions") || 
		                           requestPath.startsWith("/api/submissions") || 
		                           requestPath.startsWith("/api/codelab") || 
		                           requestPath.equals("/codelab-status");

		if (isPublicEndpoint && (authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))) {
			chain.doFilter(request, response);
			return;
		}

		String username = null;
		String jwt = null;

		try {
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwt = authorizationHeader.substring(7);
				// If it's a public endpoint but has a token, we try to parse it but don't fail if it's invalid
				try {
					username = jwtUtil.extractUsername(jwt);
				} catch (Exception e) {
					if (isPublicEndpoint) {
						logger.debug("Ignoring invalid token for public endpoint: " + requestPath);
						chain.doFilter(request, response);
						return;
					}
					throw e; // Rethrow to be caught by the outer catch block for non-public endpoints
				}
			}

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

				if (jwtUtil.validateToken(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}
		} catch (ExpiredJwtException e) {
			logger.warn("Token expired: " + e.getMessage());
		} catch (JwtException e) {
			logger.warn("Invalid token: " + e.getMessage());
		} catch (Exception e) {
			logger.error("Authentication error for path " + requestPath + ": " + e.getMessage());
		}

		chain.doFilter(request, response);
	}
}