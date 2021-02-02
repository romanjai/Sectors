package info.sectorsinfo.service;


import info.sectorsinfo.model.Sectorinfo;
import info.sectorsinfo.repository.SectorinfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class SectorinfoService {

    private final SectorinfoRepository sectorinfoRepository;

    public SectorinfoService(SectorinfoRepository sectorinfoRepository) {
        this.sectorinfoRepository = sectorinfoRepository;
    }

    @Transactional
    public List<Sectorinfo> list() {
        return sectorinfoRepository.list();
    }
}
