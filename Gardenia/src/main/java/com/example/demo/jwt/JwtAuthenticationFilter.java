package com.example.demo.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.entity.TokenBlacklist;
import com.example.demo.myUserDetailsService.MyUserDetailsService;
import com.example.demo.repository.TokenBlacklistRepository;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private MyUserDetailsService myUserDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	TokenBlacklistRepository tokenBlacklistRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String requestTokenHeader = request.getHeader("Authorization");
		String username = null;
		String jwtToken = null;
		if ("OPTIONS".equals(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);

			filterChain.doFilter(request, response);
		} else {

			if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
				jwtToken = requestTokenHeader.substring(7);
				TokenBlacklist tokenBlacklist = tokenBlacklistRepository.findByToken(jwtToken);
				if (tokenBlacklist == null) {
					try {
						username = this.jwtUtil.extractUsername(jwtToken);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					UserDetails userDetails = this.myUserDetailsService.loadUserByUsername(username);
					// security
					if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
						UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						System.out.println("Role: " + userDetails.getAuthorities());
						usernamePasswordAuthenticationToken
								.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					}
				} else {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
				}
			}
			filterChain.doFilter(request, response);
		}
	}

}
