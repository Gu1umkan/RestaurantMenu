package peaksoft.entity;

import jakarta.persistence.*;
import lombok.*;
import peaksoft.enums.RestType;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurant_seq")
    @SequenceGenerator(name = "restaurant_seq", allocationSize = 1)
    private Long id;
    private String name;
    private String location;
    @Enumerated(EnumType.STRING)
    private RestType restType;
    private int numberOfEmployees;
    private int service;

    @OneToMany(cascade = {PERSIST, REMOVE, MERGE}, mappedBy = "restaurant")
    private List<User> users;

    @OneToMany(mappedBy = "restaurant",cascade = {REMOVE,MERGE})
    private List<MenuItem> menuItems = new ArrayList<>();

    @OneToMany(cascade = {REMOVE}, mappedBy = "restaurant")
    private List<Claim> claims  = new ArrayList<>();

    @OneToMany(cascade = {REMOVE}, mappedBy = "restaurant")
    private List<Category> categories;
    public void addUser(User user){
        if (this.users == null) this.users = new ArrayList<>();
        this.users.add(user);
    }


}