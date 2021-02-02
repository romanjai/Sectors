package info.sectorsinfo.repository;

import info.sectorsinfo.model.Sector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class SectorRepository {
  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public SectorRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public Long create(Sector sector) {
    SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
    simpleJdbcInsert.withTableName("sector");
    simpleJdbcInsert.usingGeneratedKeyColumns("id");

    return simpleJdbcInsert.executeAndReturnKey(toMap(sector)).longValue();
  }

  private Map<String, Object> toMap(Sector sector) {
    Map<String, Object> ret = new HashMap<>();
    ret.put("fk_owner", sector.getOwner());
    ret.put("fk_borrower_id", sector.getBorrower());
    return ret;
  }

  public Sector read(Long id, String owner) {
    String query = "select id, fk_owner, fk_borrower_id from sector where id = :id and fk_owner = :owner";
    return namedParameterJdbcTemplate.queryForObject(query, new MapSqlParameterSource("id", id).addValue("owner", owner), this::mapRowToSector);
  }

  public void update(Sector sector) {
    String query = "update sector set fk_borrower_id = :borrower where id = :id and fk_owner = :owner";

    namedParameterJdbcTemplate.update(query,
      new MapSqlParameterSource("id", sector.getId())
        .addValue("owner", sector.getOwner())
        .addValue("borrower", sector.getBorrower())
    );
  }

  public List<Sector> list(String owner) {
    String query = "select id, fk_owner, fk_borrower_id from sector where fk_owner = :owner";
    return namedParameterJdbcTemplate.query(query, new MapSqlParameterSource("owner", owner), this::mapRowToSector);
  }

  private Sector mapRowToSector(ResultSet rs, int rowNum) throws SQLException {
    return Sector.builder()
      .id(rs.getLong("id"))
      .borrower(rs.getLong("fk_borrower_id"))
      .owner(rs.getString("fk_owner"))
      .build();
  }
}
