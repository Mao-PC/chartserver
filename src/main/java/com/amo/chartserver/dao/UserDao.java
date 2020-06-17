package com.amo.chartserver.dao;

import com.amo.chartserver.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

    long countByUsername(String username);
}
