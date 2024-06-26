package peaksoft.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.DETACH;

@Entity
@Table(name = "categories")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categories_seq")
    @SequenceGenerator(name = "categories_seq", allocationSize = 1)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "category",cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<SubCategory> subCategories = new ArrayList<>();

    @ManyToOne(cascade = {DETACH})
    private Restaurant restaurant;

}