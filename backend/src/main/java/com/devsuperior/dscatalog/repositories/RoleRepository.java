package com.devsuperior.dscatalog.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dscatalog.entities.Rule;

public interface RoleRepository extends JpaRepository<Rule, Long> {

}
