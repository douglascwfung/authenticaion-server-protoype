package net.icestone.authserver.io.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.icestone.authserver.io.entity.ERole;
import net.icestone.authserver.io.entity.RoleEntity;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

	RoleEntity findByName(ERole name);
	
}