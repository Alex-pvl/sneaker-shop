package ru.nstu.ap.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nstu.ap.model.user.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	Role findRoleByName(String name);
}
