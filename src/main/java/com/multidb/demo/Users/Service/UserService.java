package com.multidb.demo.Users.Service;

import com.multidb.demo.Users.Repocitory.UserRepository;
import com.multidb.demo.Users.entity.Users;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class UserService {


    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ---------------- READ (GOES TO REPLICA) ----------------
    @Transactional(readOnly = true)
    public Users getUser(Integer id) {

        System.out.println("---- READ OPERATION ----");
        System.out.println("Is ReadOnly: " + TransactionSynchronizationManager.isCurrentTransactionReadOnly());
        System.out.println("TX Active: " + TransactionSynchronizationManager.isActualTransactionActive());

        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("Fetched User: " + user);

        return user;
    }

    // ---------------- WRITE (GOES TO PRIMARY) ----------------
    @Transactional
    public Users createUser(Users user) {

        System.out.println("---- WRITE OPERATION ----");
        System.out.println("Is ReadOnly: " + TransactionSynchronizationManager.isCurrentTransactionReadOnly());
        System.out.println("TX Active: " + TransactionSynchronizationManager.isActualTransactionActive());

        // Save
        Users savedUser = userRepository.save(user);

        // Force flush (executes SQL immediately)
        userRepository.flush();

        System.out.println("Saved User ID: " + savedUser);

        // 🔥 CRITICAL TEST: Read immediately inside same transaction
        Users fetched = userRepository.findById(savedUser.getId()).orElse(null);
        System.out.println("Fetched inside TX: " + fetched);

        return savedUser;
    }
}
