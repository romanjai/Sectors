package info.sectorsinfo.dto;

import info.sectorsinfo.model.Borrower;
import info.sectorsinfo.model.Sector;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;

@Data
@Builder
public class SectorsInputFormDTO {
  @Valid
  private final Sector sector;
  @Valid
  private final Borrower borrower;

  public SectorsInputFormDTO() {
    this.sector = Sector.empty();
    this.borrower = Borrower.empty();
  }

  public SectorsInputFormDTO(Sector sector, Borrower borrower) {
    this.sector = sector;
    this.borrower = borrower;
  }

  public static SectorsInputFormDTO empty() {
    SectorsInputFormDTO dto = SectorsInputFormDTO.builder()
      .sector(Sector.empty())
      .borrower(Borrower.empty())
      .build();
    return dto;
  }
}
