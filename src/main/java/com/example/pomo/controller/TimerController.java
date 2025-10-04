//package com.example.pomo.controller;
//
//import com.example.pomo.model.Timer;
//import com.example.pomo.model.User;
//import com.example.pomo.service.TimerService;
//import com.example.pomo.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//@CrossOrigin(origins = "http://localhost:5173")
//@RestController
//@RequestMapping("/timer")
//public class TimerController {
//
//    private final TimerService timerService;
//    private final UserService userService; // To get user from username (or token)
//
//    @Autowired
//    public TimerController(TimerService timerService, UserService userService) {
//        this.timerService = timerService;
//        this.userService = userService;
//    }
//
//    // ------------------- CREATE TIMER -------------------
//    @PostMapping("/createTimer")
//    public ResponseEntity<?> createTimer(@RequestParam int durationMin,
//                                         @RequestParam String taskName,
//                                         @RequestParam String username) { // get user
//        try {
//            User user = userService.findByUsername(username)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//            Timer createdTimer = timerService.createTimer(durationMin, taskName, username);
//            return ResponseEntity.ok(createdTimer);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    // ------------------- EXTEND TIMER -------------------
//    @PutMapping("/extendTime")
//    public ResponseEntity<?> extendTimer(@RequestParam Long id,
//                                         @RequestParam int extraMin) {
//        try {
//            Timer updatedTimer = timerService.extendTimer(id, extraMin);
//            return ResponseEntity.ok(updatedTimer);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    // ------------------- GET TIMER BY ID -------------------
//    @GetMapping("/getTimer/{id}")
//    public ResponseEntity<?> getTimerById(@PathVariable Long id) {
//        try {
//            return ResponseEntity.ok(timerService.getTimerById(id));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    // ------------------- GET ALL TIMERS -------------------
//    @GetMapping("/getAllTimers")
//    public ResponseEntity<?> getAllTimers() {
//        try {
//            List<Timer> timers = timerService.getAllTimers();
//            return ResponseEntity.ok(timers);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    // ------------------- DELETE TIMER -------------------
//    @DeleteMapping("/deleteTimer/{id}")
//    public ResponseEntity<?> deleteTimer(@PathVariable Long id) {
//        try {
//            Timer deletedTimer = timerService.deleteTimer(id);
//            return ResponseEntity.ok(deletedTimer);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    // ------------------- COMPLETE TIMER -------------------
//    @PutMapping("/completeTimer/{id}")
//    public ResponseEntity<?> completeTimer(@PathVariable Long id) {
//        try {
//            Timer completedTimer = timerService.completeTimer(id);
//            return ResponseEntity.ok(completedTimer);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//}
package com.example.pomo.controller;

import com.example.pomo.model.Timer;
import com.example.pomo.service.TimerService;
import com.example.pomo.JWT.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

//@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/timer")
public class TimerController {

    private final TimerService timerService;
    private final JwtUtil jwtUtil;

    @Autowired
    public TimerController(TimerService timerService, JwtUtil jwtUtil) {
        this.timerService = timerService;
        this.jwtUtil = jwtUtil;
    }

    // ------------------- HELPER TO GET USERNAME FROM JWT -------------------
    private String getUsernameFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        if(username == null) throw new RuntimeException("Invalid JWT token");
        return username;
    }

    // ------------------- CREATE TIMER -------------------
    @PostMapping("/createTimer")
    public ResponseEntity<?> createTimer(@RequestParam int durationMin,
                                         @RequestParam String taskName,
                                         HttpServletRequest request) {
        String username = getUsernameFromRequest(request);
        Timer createdTimer = timerService.createTimer(durationMin, taskName, username);
        return ResponseEntity.ok(createdTimer);
    }

    // ------------------- EXTEND TIMER -------------------
    @PutMapping("/extendTime")
    public ResponseEntity<?> extendTimer(@RequestParam Long id,
                                         @RequestParam int extraMin,
                                         HttpServletRequest request) {
        String username = getUsernameFromRequest(request);
        Timer updatedTimer = timerService.extendTimer(id, extraMin, username);
        return ResponseEntity.ok(updatedTimer);
    }

    // ------------------- GET TIMER BY ID -------------------
    @GetMapping("/getTimer/{id}")
    public ResponseEntity<?> getTimerById(@PathVariable Long id,
                                          HttpServletRequest request) {
        String username = getUsernameFromRequest(request);
        Timer timer = timerService.getTimerById(id, username);
        return ResponseEntity.ok(timer);
    }

    // ------------------- GET ALL TIMERS -------------------
    @GetMapping("/getAllTimers")
    public ResponseEntity<?> getAllTimers(HttpServletRequest request) {
        String username = getUsernameFromRequest(request);
        List<Timer> timers = timerService.getAllTimers(username);
        return ResponseEntity.ok(timers);
    }

    // ------------------- DELETE TIMER -------------------
    @DeleteMapping("/deleteTimer/{id}")
    public ResponseEntity<?> deleteTimer(@PathVariable Long id,
                                         HttpServletRequest request) {
        String username = getUsernameFromRequest(request);
        Timer timer = timerService.deleteTimer(id, username);
        return ResponseEntity.ok(timer);
    }

    // ------------------- COMPLETE TIMER -------------------
    @PutMapping("/completeTimer/{id}")
    public ResponseEntity<?> completeTimer(@PathVariable Long id,
                                           HttpServletRequest request) {
        String username = getUsernameFromRequest(request);
        Timer timer = timerService.completeTimer(id, username);
        return ResponseEntity.ok(timer);
    }

    // ------------------- SEARCH BY TASK NAME -------------------
    @GetMapping("/search")
    public ResponseEntity<?> searchByTaskName(@RequestParam String taskName,
                                              HttpServletRequest request) {
        String username = getUsernameFromRequest(request);
        List<Timer> timers = timerService.getTimersByTaskName(taskName, username);
        return ResponseEntity.ok(timers);
    }
}
