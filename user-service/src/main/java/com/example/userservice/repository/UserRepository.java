package com.example.userservice.repository;

import com.example.userservice.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userMapper = (rs, rowNum) -> {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        return u;
    };

    // ✅ Create user
    public void create(User u) {
        jdbcTemplate.update("INSERT INTO users(name, email, password) VALUES (?, ?, ?)",
                u.getName(), u.getEmail(), u.getPassword());
    }

    // ✅ Find by email
    public User findByEmail(String email) {
        List<User> result = jdbcTemplate.query("SELECT * FROM users WHERE email = ?", userMapper, email);
        return result.isEmpty() ? null : result.get(0);
    }

    // ✅ Find by ID
    public User findById(Long id) {
        List<User> result = jdbcTemplate.query("SELECT * FROM users WHERE id = ?", userMapper, id);
        return result.isEmpty() ? null : result.get(0);
    }

    // ✅ Update user
    public void save(User user) {
        jdbcTemplate.update("UPDATE users SET name=?, email=?, password=? WHERE id=?",
                user.getName(), user.getEmail(), user.getPassword(), user.getId());
    }
}
