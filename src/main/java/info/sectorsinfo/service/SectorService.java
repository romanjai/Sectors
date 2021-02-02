package info.sectorsinfo.service;

import info.sectorsinfo.dto.SectorsInputFormDTO;
import info.sectorsinfo.model.Borrower;
import info.sectorsinfo.model.Sector;
import info.sectorsinfo.repository.BorrowerRepository;
import info.sectorsinfo.repository.SectorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class SectorService {

  private final SectorRepository sectorRepository;
  private final BorrowerRepository borrowerRepository;

  @Autowired
  public SectorService(SectorRepository repoSector, BorrowerRepository repoBorrower) {
    this.sectorRepository = repoSector;
    this.borrowerRepository = repoBorrower;
  }

  @Transactional
  public SectorsInputFormDTO getrepoSector(Long id, String owner) {
    Sector sector = sectorRepository.read(id, owner);
    Borrower borrower = borrowerRepository.read(sector.getBorrower());

    return SectorsInputFormDTO.builder().sector(sector).borrower(borrower).build();
  }

  @Transactional
  public List<Sector> list(String username) {
    return sectorRepository.list(username);
  }

  @Transactional
  public Long create(SectorsInputFormDTO dto) {
    Long borrower = borrowerRepository.create(dto.getBorrower());
    dto.getSector().setBorrower(borrower);
    Long sector = sectorRepository.create(dto.getSector());
    log.debug("created sector {}", sector);
    return sector;
  }

  @Transactional
  public void save(SectorsInputFormDTO dto) {
    borrowerRepository.update(dto.getBorrower());
    sectorRepository.update(dto.getSector());
  }

  /*@Transactional
  public void deleteAll() {
    sectorRepository.deleteAll();
    borrowerRepository.deleteAll();
  }*/
}
