package peaksoft.entity;

import jakarta.persistence.*;
import lombok.*;
import peaksoft.enums.Role;

import java.time.LocalDate;

@Entity
@Table(name = "claims")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "claim_seq")
    @SequenceGenerator(name = "claim_seq", allocationSize = 1)
    private Long id;
    private String lastName;
    private String firstName;
    private LocalDate dateOfBirth;
    private String email;
    private String password;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Role role;
    private int experience;
    @ManyToOne(cascade = {CascadeType.DETACH})
    private Restaurant restaurant;
}