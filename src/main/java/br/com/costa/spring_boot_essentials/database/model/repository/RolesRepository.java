package br.com.costa.spring_boot_essentials.database.model.repository;

import br.com.costa.spring_boot_essentials.database.model.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<RolesEntity, Integer> {
}
