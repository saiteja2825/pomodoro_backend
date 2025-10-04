//package com.example.pomo.repository;
//
//import com.example.pomo.model.Timer;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface timerRepo extends JpaRepository<Timer,Long> {
//
//    List<Timer> findBytaskName(String taskName) ;
//
//
//
//
//
//
//}
package com.example.pomo.repository;

import com.example.pomo.model.Timer;
import com.example.pomo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface timerRepo extends JpaRepository<Timer, Long> {
    List<Timer> findByUser(User user);
    List<Timer> findByTaskNameAndUser(String taskName, User user);
}
