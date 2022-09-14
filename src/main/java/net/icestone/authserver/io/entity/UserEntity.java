package net.icestone.authserver.io.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
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
@Entity
@Log
@Table(name="is_prototype_users")
public class UserEntity implements Serializable {

	private static final long serialVersionUID = -7113027622575714159L;

	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable=false)
	private String userId;

	@Column(nullable=false, length=50)
	private String firstName;
	
	@Column(nullable=false, length=50)
	private String lastName;
	
	@Column(nullable=false, length=120)
	private String email;
	
	@Column(nullable=false)
	private String encryptedPassword;
	
	private String emailVerificationToken;
	
	@Column(nullable=false)
	private Boolean emailVerificationStatus = false;
	
//	@OneToMany(mappedBy="userDetails", cascade=CascadeType.ALL)
//	private List<AddressEntity> addresses;

	@ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
	@JoinTable(name="user_roles",
			joinColumns=@JoinColumn(name="user_id", referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="role_id", referencedColumnName="id")
			)
	private Collection<RoleEntity> roles;	
	
}
