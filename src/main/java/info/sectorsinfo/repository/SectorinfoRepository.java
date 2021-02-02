package info.sectorsinfo.repository;

import info.sectorsinfo.model.Sectorinfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
public class SectorinfoRepository {

    private final JdbcTemplate template;

    @Autowired
    public SectorinfoRepository(JdbcTemplate jdbcTemplate) {
        template = jdbcTemplate;
    }

    public List<Sectorinfo> list() {
        String query = "select code, name from sectorinfo";
        return template.query(query, this::mapRowToSectorInfo);
    }

    private Sectorinfo mapRowToSectorInfo(ResultSet rs, int rowNum) throws SQLException {
        return Sectorinfo.builder()
                .code(rs.getLong("code"))
                .name(rs.getString("name"))
                .build();
    }
}

