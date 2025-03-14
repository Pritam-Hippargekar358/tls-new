package com.relationship.entity.oneToOne;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
public class Locker {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "locker_id")
    @ToString.Exclude   // to prevent stackoverflow error (infinite loop) on using toString method
    //@JsonBackReference("memberLockerManagedReference") // to prevent stackoverflow error (infinite loop) on preparing json
    private Member member;

}
