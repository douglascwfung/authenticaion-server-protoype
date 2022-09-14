package net.icestone.authserver.io.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name="authorities")
public class AuthorityEntity implements Serializable{
	
	public AuthorityEntity(EAuthority name) {
		this.name = name;
	}

	private static final long serialVersionUID = -8423588523514818023L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false, length = 20)
	private EAuthority name;	
	
	@ManyToMany(mappedBy="authorities")
	private Collection<RoleEntity> roles;
	
}
