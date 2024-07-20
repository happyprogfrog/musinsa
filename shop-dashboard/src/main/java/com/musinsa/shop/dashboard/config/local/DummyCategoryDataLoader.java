package com.musinsa.shop.dashboard.config.local;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Profile("local")
@Order(1)
public class DummyCategoryDataLoader implements CommandLineRunner {
    private final JdbcTemplate jdbcTemplate;

    public DummyCategoryDataLoader(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void run(String... args) throws Exception {
        // createCategoryTable();
        loadCategories();
    }

    private void createCategoryTable() {
        String sql = """
        CREATE TABLE IF NOT EXISTS category (
            id BIGINT AUTO_INCREMENT,
            code VARCHAR(9) NOT NULL,
            PRIMARY KEY (id),
            UNIQUE (code)
        )
        """;

        jdbcTemplate.execute(sql);
    }

    private void loadCategories() {
        String countQuery = "SELECT COUNT(*) FROM category";
        Integer count = jdbcTemplate.queryForObject(countQuery, Integer.class);
        if (count == null) {
            count = 0;
        }

        if (count == 0) {
            String[] categories = {"001", "002", "003", "004", "005", "006", "007", "008"};
            for (String code : categories) {
                jdbcTemplate.update("INSERT INTO category (code) VALUES (?)", code);
            }
        }
    }
}
