package com.url_shortener_sb.entity;

import com.url_shortener_sb.utils.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private String name;

//    @ManyToMany(mappedBy = "roles")
//    private Set<User> users;


//    public Role(Roles name) {
//        this.name = name;
//    }
//
//    public Roles getName() {
//        return name;
//    }
//
//    public void setName(Roles name) {
//        this.name = name;
//    }
}
