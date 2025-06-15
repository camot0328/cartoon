package org.example.cartoon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cart {
  @Id
  @Column(name = "user_id")
  private Integer userId;

  @OneToOne
  @MapsId
  @JoinColumn(name = "user_id")
  @EqualsAndHashCode.Exclude
  @JsonIgnore
  @ToString.Exclude
  private Users user;

  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CartItem> cartItems = new ArrayList<>();
}
