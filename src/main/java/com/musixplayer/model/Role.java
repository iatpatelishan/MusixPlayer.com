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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    @Getter
    @Setter
    private Long id;

    @Column(name = "name", unique = true)
    @Getter
    @Setter
    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "role_privilege", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "privilege_id"))
    @JsonIgnore
    @Getter
    @Setter
    private Collection<Privilege> privileges;

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    @Getter @Setter private Collection<Person> people;

}
