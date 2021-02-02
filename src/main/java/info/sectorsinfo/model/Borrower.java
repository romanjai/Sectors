package info.sectorsinfo.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class Borrower {
  private Long id;

  @NotBlank(message = "Name is mandatory")
  private String name;

  @NotNull(message = "SectorInfo is mandatory")
  private Long sectorinfo;

  @AssertTrue (message = "Agree to terms must be checked")
  private Boolean terms;



  public static Borrower empty() {
    Borrower borrower = Borrower.builder()
            .name("")
            .sectorinfo(0L)
            .terms(Boolean.parseBoolean(""))
            .build();

    return borrower;
  }
}
