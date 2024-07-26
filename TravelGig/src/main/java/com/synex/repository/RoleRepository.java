package com.synex.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.synex.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
	Optional<Role> findByRoleName(String roleName);
}
