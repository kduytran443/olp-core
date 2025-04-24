package com.kduytran.olpcore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @Version
    @Column
    private Long version;

    @Column(nullable = false, unique = true)
    private String id;

    @PrePersist
    private void makeId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}
