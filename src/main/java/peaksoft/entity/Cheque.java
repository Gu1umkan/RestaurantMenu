package peaksoft.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cheques")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cheque {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cheque_seq")
    @SequenceGenerator(name = "cheque_seq", allocationSize = 1)
    private Long id;
    private BigDecimal totalPrice;
    private int percent = 20;
    private BigDecimal service;
    private LocalDate createdAt;
    private BigDecimal allSumma;

    @ManyToOne(cascade = CascadeType.DETACH)
    private User user;

    @ManyToMany
    private List<MenuItem> menuItems = new ArrayList<>();


    @PrePersist
    private void prePersist(){
        this.createdAt = LocalDate.now();
    }
}