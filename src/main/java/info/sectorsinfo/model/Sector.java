package info.sectorsinfo.model;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Sector {
  private Long id;

  private Long borrower;
  private String owner;



  public static Sector empty() {
    Sector sector = Sector.builder()
            .owner("")
            .borrower(0L)
            .build();

    return sector;
  }
}
