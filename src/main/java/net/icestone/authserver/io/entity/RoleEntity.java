package net.icestone.authserver.io.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.java.Log;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Log
@Entity
@Table(name = "roles")
public class RoleEntity implements Serializable {

	public RoleEntity(ERole name) {
		this.name = name;
	}

	private static final long serialVersionUID = -5659296482879107120L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable=false, length = 20)
	private ERole name;

	@ManyToMany(mappedBy = "roles")
	private Collection<UserEntity> users;

	@ManyToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.EAGER)
	@JoinTable(name = "roles_authorities", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
	private Collection<AuthorityEntity> authorities;

}
