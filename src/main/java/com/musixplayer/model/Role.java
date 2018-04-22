package com.musixplayer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    @Getter
    @Setter
    private Long id;

    @Column(name = "name", unique = true)
    @Getter
    @Setter
    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "role_privilege", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "privilege_id"))
    @JsonIgnore
    @Getter
    @Setter
    private Collection<Privilege> privileges;

    @OneToMany(mappedBy = "role",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Getter @Setter private Collection<Person> people;

}
