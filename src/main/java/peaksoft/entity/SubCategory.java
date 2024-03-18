package peaksoft.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.DETACH;
import static jakarta.persistence.CascadeType.REMOVE;

@Entity
@Table(name = "sub_categories")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sub_cat_seq")
    @SequenceGenerator(name = "sub_cat_seq", allocationSize = 1)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "subCategory", cascade = {REMOVE})
    private List<MenuItem> menuItems = new ArrayList<>();

    @ManyToOne(cascade = {DETACH})
    private Category category;

}