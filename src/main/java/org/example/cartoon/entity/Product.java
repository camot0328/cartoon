package org.example.cartoon.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Product {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, length = 255)
  private String title;

  private String author;
  private String publisher;

  @Column(name = "publish_date")
  private LocalDate publishDate;

  @Column(nullable = false)
  private int price;

  @Column(name = "thumbnail_img", columnDefinition = "TEXT")
  private String thumbnailImg;

  @Column(name = "detail_img", columnDefinition = "TEXT")
  private String detailImg;

  @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private Stock stock;

  @ManyToMany
  @JoinTable(name = "product_category",
      joinColumns = @JoinColumn(name = "product_id"),
      inverseJoinColumns = @JoinColumn(name = "category_id"))
  private Set<Category> categories = new HashSet<>();
}

