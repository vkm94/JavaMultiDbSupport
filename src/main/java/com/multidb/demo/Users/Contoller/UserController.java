package com.multidb.demo.Users.Contoller;

import com.multidb.demo.Users.Service.UserService;
import com.multidb.demo.Users.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private  UserService userService;
    @PostMapping("/save")
    public Users saveUsers(@RequestBody Users users){
    userService.createUser(users);
    return users;

}
    @GetMapping("/getById/{id}")
    public Users getUsersById(@PathVariable Integer id){

        return  userService.getUser(id);

    }
    @GetMapping("/test-routing")
    public String testRouting() {
        // Manually set the transaction state to see if the log changes
        TransactionSynchronizationManager.setCurrentTransactionReadOnly(true);
        try {
            userService.getUser(1);
            return "Check your console logs!";
        } finally {
            TransactionSynchronizationManager.clear();
        }
    }
}
