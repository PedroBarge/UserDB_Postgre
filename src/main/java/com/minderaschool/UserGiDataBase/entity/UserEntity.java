package com.minderaschool.UserGiDataBase.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "user_table_complete")
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(unique = true)
    private Integer id;
    @Column
    private String username;
    @Column
    private String email;
    @Column
    private String password;
}
