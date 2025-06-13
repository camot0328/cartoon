package org.example.cartoon.dto;

import lombok.Builder;
import lombok.Data;
import org.example.cartoon.entity.Product;

import java.time.LocalDate;

@Data
public class ProductFormDto {
  private Integer id;
  private String title;
  private String author;
  private String publisher;
  private LocalDate publishDate;
  private int price;
  private String thumbnailImg;
  private String detailImg;

  public Product toEntity() {
    return Product.builder()
        .id(this.id)
        .title(this.title)
        .author(this.author)
        .publisher(this.publisher)
        .publishDate(this.publishDate)
        .price(this.price)
        .thumbnailImg(this.thumbnailImg)
        .detailImg(this.detailImg)
        .build();
  }

  public static ProductFormDto from(Product product) {
    ProductFormDto dto = new ProductFormDto();
    dto.setId(product.getId());
    dto.setTitle(product.getTitle());
    dto.setAuthor(product.getAuthor());
    dto.setPublisher(product.getPublisher());
    dto.setPublishDate(product.getPublishDate());
    dto.setPrice(product.getPrice());
    dto.setThumbnailImg(product.getThumbnailImg());
    dto.setDetailImg(product.getDetailImg());
    return dto;
  }
}
