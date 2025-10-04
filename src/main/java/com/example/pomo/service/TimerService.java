//package com.example.pomo.service;
//import com.example.pomo.model.User;
//import com.example.pomo.model.Timer;
//import com.example.pomo.repository.timerRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//public class TimerService {
//
//    private final timerRepo timerRepo;
//    private final UserService service;
//
//    @Autowired
//    public TimerService(timerRepo timerRepo, UserService service){
//        this.timerRepo = timerRepo;
//        this.service=service;
//    }
//
//    public Timer createTimer(int durationMin, String taskName, String username){
//
//        User user = service.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        LocalDateTime startTime = LocalDateTime.now();
//        LocalDateTime endTime = startTime.plusMinutes(durationMin);
//
//        Timer time = new Timer();
//        time.setStartTime(startTime);
//        time.setEndTime(endTime);
//        time.setTaskName(taskName);
//        time.setDuration(durationMin);
//        time.setExtendedTime(0);
//        time.setWorkCompleted(false);
//        time.setUser(user);  // link with user
//
//        return timerRepo.save(time);
//    }
//
//    public Timer extendTimer(Long id, int extraMin){
//        Timer findTime = timerRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Timer not found with id: " + id));
//
//        findTime.setEndTime(findTime.getEndTime().plusMinutes(extraMin));
//        findTime.setExtendedTime(findTime.getExtendedTime() + extraMin);
//
//        return timerRepo.save(findTime);
//    }
//
//    public Timer getTimerById(Long id){
//        return timerRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Timer not found with id: " + id));
//    }
//
//    public List<Timer> getAllTimers(){
//        return timerRepo.findAll();
//    }
//
//    public Timer deleteTimer(Long id){
//        Timer timerToDelete = timerRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Timer not found with id: " + id));
//
//        timerRepo.delete(timerToDelete);
//        return timerToDelete;
//    }
//
//    public Timer completeTimer(Long id){
//        Timer findTime = timerRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Timer not found with id: " + id));
//
//        findTime.setWorkCompleted(true);
//        int totalMin = findTime.getDuration() + findTime.getExtendedTime();
//        findTime.setFocusedTime(totalMin);
//
//        return timerRepo.save(findTime);
//    }
//
//    public List<Timer> getTimersByTaskName(String taskName){
//        return timerRepo.findBytaskName(taskName);
//    }
//
//
//
//
//
//}
package com.example.pomo.service;

import com.example.pomo.model.User;
import com.example.pomo.model.Timer;
import com.example.pomo.repository.timerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TimerService {

    private final timerRepo timerRepo;
    private final UserService userService;

    @Autowired
    public TimerService(timerRepo timerRepo, UserService userService){
        this.timerRepo = timerRepo;
        this.userService = userService;
    }

    public Timer createTimer(int durationMin, String taskName, String username){
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusMinutes(durationMin);

        Timer timer = new Timer();
        timer.setStartTime(startTime);
        timer.setEndTime(endTime);
        timer.setTaskName(taskName);
        timer.setDuration(durationMin);
        timer.setExtendedTime(0);
        timer.setWorkCompleted(false);
        timer.setUser(user);

        return timerRepo.save(timer);
    }

    public Timer extendTimer(Long id, int extraMin, String username){
        Timer timer = getTimerById(id, username);

        timer.setEndTime(timer.getEndTime().plusMinutes(extraMin));
        timer.setExtendedTime(timer.getExtendedTime() + extraMin);

        return timerRepo.save(timer);
    }

    public Timer getTimerById(Long id, String username){
        Timer timer = timerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Timer not found with id: " + id));

        if(!timer.getUser().getUsername().equals(username)){
            throw new RuntimeException("Access denied for this timer");
        }

        return timer;
    }

    public List<Timer> getAllTimers(String username){
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return timerRepo.findByUser(user);
    }

    public Timer deleteTimer(Long id, String username){
        Timer timer = getTimerById(id, username);
        timerRepo.delete(timer);
        return timer;
    }

    public Timer completeTimer(Long id, String username){
        Timer timer = getTimerById(id, username);
        timer.setWorkCompleted(true);
        int totalMin = timer.getDuration() + timer.getExtendedTime();
        timer.setFocusedTime(totalMin);
        return timerRepo.save(timer);
    }

    public List<Timer> getTimersByTaskName(String taskName, String username){
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return timerRepo.findByTaskNameAndUser(taskName, user);
    }
}
