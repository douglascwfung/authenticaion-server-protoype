package net.icestone.authserver.io.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.icestone.authserver.io.entity.AuthorityEntity;
import net.icestone.authserver.io.entity.EAuthority;

@Repository
public interface AuthorityRepository extends CrudRepository<AuthorityEntity, Long>{
	
	AuthorityEntity findByName(EAuthority name);

}
