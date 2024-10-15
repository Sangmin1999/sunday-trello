package com.sparta.sunday.domain.alarm.entity;

import com.sparta.sunday.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "alarm")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    private Type type;

    @Column(name = "itemId", nullable = false)
    private Long itemId;

    @Column(name = "message", nullable = false)
    private String message;

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(name = "`read`", nullable = false)
    private Boolean read;

    @ManyToOne
    @JoinColumn(name = "receive_user_id")
    private User receiveUser;

    @ManyToOne
    @JoinColumn(name = "send_user_id")
    private User sendUser;

    public static Alarm of(String type, Long itemId, User receiveUser, User sendUser, String message) {

        Alarm alarm = new Alarm();
        alarm.type = Type.of(type);
        alarm.itemId = itemId;
        alarm.message = message;
        alarm.read = false;
        alarm.receiveUser = receiveUser;
        alarm.sendUser = sendUser;
        return alarm;
    }

    public Long getIdRead() {
        this.read = true;
        return this.id;
    }
}
