package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getById(int id) {
        String qs = "SELECT * FROM MPA_rating WHERE MPA_id = ?";
        return jdbcTemplate.queryForObject(qs, this::makeMpa, id);
    }

    @Override
    public List<Mpa> getAll() {
        String qs = "SELECT * FROM MPA_rating";
        return jdbcTemplate.query(qs, this::makeMpa);
    }

    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        int mpaId = rs.getInt("MPA_id");
        String mpaName = rs.getString("MPA_name");
        return new Mpa(mpaId, mpaName);
    }
}
