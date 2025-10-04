package com.example.pomo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "timers")
public class Timer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int duration;
    private int extendedTime;
    private boolean workCompleted;
    private int focusedTime;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
@ManyToOne
@JoinColumn(name = "user_id")
@JsonBackReference
    private User user;  // reference to User

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getExtendedTime() {
        return extendedTime;
    }

    public void setExtendedTime(int extendedTime) {
        this.extendedTime = extendedTime;
    }

    public boolean isWorkCompleted() {
        return workCompleted;
    }

    public void setWorkCompleted(boolean workCompleted) {
        this.workCompleted = workCompleted;
    }

    public int getFocusedTime() {
        return focusedTime;
    }

    public void setFocusedTime(int focusedTime) {
        this.focusedTime = focusedTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
