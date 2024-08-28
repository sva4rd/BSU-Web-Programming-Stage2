package com.app.weblab5.repository;

import com.app.weblab5.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends CrudRepository<User, Long> {
    @Transactional
    User findByUsername(String username);

    @Transactional
    default User saveAndReturnUser(User user) {
        return save(user);
    }
}

