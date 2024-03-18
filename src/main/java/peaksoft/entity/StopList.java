package peaksoft.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "stopLists")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StopList {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stop_seq")
    @SequenceGenerator(name = "stop_seq", allocationSize = 1)
    private Long id;
    private String reason;
    private LocalDate date;

    @OneToOne(cascade = CascadeType.DETACH)
    private MenuItem menuItem;

    private Boolean isActive;

   @PrePersist
    private void prePersist(){
       this.date = LocalDate.now();
   }
    @PreUpdate
    private void preUpdate() {
        this.date = LocalDate.now();
    }

}