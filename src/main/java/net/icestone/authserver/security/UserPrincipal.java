package net.icestone.authserver.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import net.icestone.authserver.io.entity.AuthorityEntity;
import net.icestone.authserver.io.entity.RoleEntity;
import net.icestone.authserver.io.entity.UserEntity;


public class UserPrincipal implements UserDetails {

	private static final long serialVersionUID = -799803450909582083L;
	
	private UserEntity user;

    public UserPrincipal(UserEntity user) {
        super();
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	
		List<GrantedAuthority> authorities = new ArrayList<>();
		List<AuthorityEntity> authorityEntities = new ArrayList<>();
		
		Collection<RoleEntity> roles = user.getRoles();
		
		if ( roles == null ) return authorities;
		
		roles.forEach((role) -> {
			authorities.add(new SimpleGrantedAuthority(role.getName().toString()));
			authorityEntities.addAll(role.getAuthorities());
		});				
		
		authorityEntities.forEach((authorityEntity) -> {
			authorities.add(new SimpleGrantedAuthority(authorityEntity.getName().toString()));
		});
		
		return authorities;
    	
//        return Stream.of(new SimpleGrantedAuthority("USER")).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getEncryptedPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
    public long getId() {
    	return user.getId();
    };

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserPrincipal user = (UserPrincipal) o;
		return Objects.equals(this.user.getId(), user.getId());
	}
}
