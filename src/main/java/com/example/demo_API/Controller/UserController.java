package com.example.demo_API.Controller;
import com.example.demo_API.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    //User先給五個值
    /*private List<User> users = Arrays.asList(
            new User("Jason", 20),
            new User("Alan", 22),
            new User("David", 21),
            new User("Monika", 20),
            new User("Angela", 22)
    );*/
//上面那種寫法會沒辦法post,因為它是一個內容不可修改的List
    //要改成下面這種ArrayList,才可以修改內容
    private List<User> users = new ArrayList<>(Arrays.asList(
            new User("Jason", 20),
            new User("Jason", 30),
            new User("Alan", 22),
            new User("David", 21),
            new User("Monika", 20),
            new User("Monika3", 22)
    ));

    //Get所有的User,依據傳入的條件,沒有給條件就是全部,有的話就是依據年齡獲取user
    @GetMapping
    public List<User> getUsers(@RequestParam(required = false, defaultValue = "0") int age) {
        if (age == 0) {
            return users;
        } else {
            return filterUsersByAge(age);
        }
    }

    //依據傳入的姓名獲取User
    @GetMapping("/{name}")
    public User getUserBy(@PathVariable String name) {
        return users.stream()
                .filter(user -> user.getName().equals(name))
                .findFirst() //name如果相同的話,只取第一筆
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + name + " not found"));
    }

    //依據傳入的年紀獲取User
    private List<User> filterUsersByAge(int age) {
        return users.stream()
                .filter(user -> user.getAge() == age)
                .toList();
    }

    //新增user
    @PostMapping
    public User addUser(@RequestBody User user) {
        if (users.stream().anyMatch(u -> u.getName().equals(user.getName()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Duplicated user " + user.getName());
        }
        users.add(user);
        return user;
    }

    //修改user
    @PutMapping("/{name}")
    public User modifyUser(@PathVariable String name, @RequestBody User user) {
        User found = users.stream()
                .filter(u -> u.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + name + " not found"));

        //found.setName(user.getName()); //如果連名稱都要修改就把這行打開唄
        found.setAge(user.getAge());
        return found;
    }

    //20230919 加上Patch方法 By Zhi Xuan You
    //修改user
    @PatchMapping("/{name}")
    public User modifyUser2(@PathVariable String name, @RequestBody User user) {
        User found = users.stream()
                .filter(u -> u.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + name + " not found"));
        //found.setName(user.getName()); //如果連名稱都要修改就把這行打開唄
        found.setAge(user.getAge());
        return found;
    }

    //刪除user
    /*@DeleteMapping("/{name}")
    public List<User> removeUser(@PathVariable String name, @RequestBody User Xuser) {
        User found = users.stream()
                .filter(user -> user.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + name + " not found"));

        users.remove(found);
        return users; //這邊列出刪除user後剩下的所有user資料,比較好確認此user是否真的有被remove
    }*/
    //20230922 刪除方法改成可以傳入body By Zhi Xuan You
    @DeleteMapping
    public List<User> removeUser(@RequestBody User xUser) {
        String name = xUser.getName(); // 假設您從 User 對象中獲取要刪除的用戶名稱

        User found = users.stream()
                .filter(user -> user.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + name + " not found"));

        users.remove(found);
        return users;
    }
}

