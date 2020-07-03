package hr.java.web.radanovic.recordkeeper.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hr.java.web.radanovic.recordkeeper.model.AppUser;
import hr.java.web.radanovic.recordkeeper.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) {
		Optional<AppUser> userOptional = userRepository.findByUsername(username);
		AppUser user = userOptional
				.orElseThrow(() -> new UsernameNotFoundException("No user " + "Found with username : " + username));

		return new User(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true,
				getAuthorities(user.getUsername()));
	}

	private Collection<? extends GrantedAuthority> getAuthorities(String username) {
		Collection<GrantedAuthority> collection = new ArrayList<GrantedAuthority>();
		userRepository.findByUsername(username).get().getRoles()
				.forEach(e -> collection.add(new SimpleGrantedAuthority(e.getAuthority().toString())));
		return collection;
	}
}
