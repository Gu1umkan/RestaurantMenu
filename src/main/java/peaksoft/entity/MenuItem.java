package peaksoft.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "menuItems")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "items_seq")
    @SequenceGenerator(name = "items_seq", allocationSize = 1)
    private Long id;
    private String name;
    private String image;
    private BigDecimal price;
    private String description;
    private boolean isVegetarian;
    private int quantity;
    @ManyToOne
    private Restaurant restaurant;

    @ManyToOne(cascade = {DETACH})
    private SubCategory subCategory;

    @OneToOne( cascade = {REMOVE, MERGE})
    private StopList stopList;

    @ManyToMany(mappedBy = "menuItems",cascade = REMOVE)
    private List<Cheque> cheques;
}