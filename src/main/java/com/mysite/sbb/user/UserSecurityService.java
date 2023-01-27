package com.mysite.sbb.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService{
	
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<SiteUser> _siteUser = this.userRepository.findByUsername(username);
		if(_siteUser.isEmpty()) {
			//security패키지에 있는 UsernameNotFoundException
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		SiteUser siteUser = _siteUser.get();
		List<GrantedAuthority> authorities = new ArrayList<>();
		if("admin".equals(username)) {
			//ROLE_ADMIN
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
		}else {
			//ROLE_USER
			authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));			
		}
		return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
	}
	
}
