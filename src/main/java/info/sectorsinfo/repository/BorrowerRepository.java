package info.sectorsinfo.repository;

import info.sectorsinfo.model.Borrower;
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
import java.util.Map;

@Slf4j
@Repository
public class BorrowerRepository {
  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public BorrowerRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public Long create(Borrower borrower) {
    SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
    simpleJdbcInsert.withTableName("borrower");
    simpleJdbcInsert.usingGeneratedKeyColumns("id");

    return simpleJdbcInsert.executeAndReturnKey(toMap(borrower)).longValue();
  }

  private Map<String, Object> toMap(Borrower borrower) {
    Map<String, Object> ret = new HashMap<>();
    ret.put("name", borrower.getName());
    ret.put("fk_sectorinfo", borrower.getSectorinfo());
    ret.put("terms", borrower.getTerms());
    return ret;
  }

  public Borrower read(Long id) {
    String query = "select id, name," +
            "fk_sectorinfo,terms from borrower where id = :id";
    return namedParameterJdbcTemplate.queryForObject(query, new MapSqlParameterSource("id", id), this::mapRowToBorrower);
  }

  public void update(Borrower borrower) {
    String query = "update borrower set name = :name,fk_sectorinfo = :fk_sectorinfo,terms= :terms where id = :id";

    namedParameterJdbcTemplate.update(query,
      new MapSqlParameterSource("id", borrower.getId())

              .addValue("name", borrower.getName())
              .addValue("fk_sectorinfo", borrower.getSectorinfo())
              .addValue("terms",borrower.getTerms())
    );
  }

  private Borrower mapRowToBorrower(ResultSet rs, int rowNum) throws SQLException {
    return Borrower.builder()

            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .sectorinfo(rs.getLong("fk_sectorinfo"))
            .terms(rs.getBoolean("terms"))
            .build();
  }
}
