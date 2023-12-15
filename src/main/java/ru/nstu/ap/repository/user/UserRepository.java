package ru.nstu.ap.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nstu.ap.model.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	User findUserByEmail(String email);
	User findUserByUsername(String username);
}
