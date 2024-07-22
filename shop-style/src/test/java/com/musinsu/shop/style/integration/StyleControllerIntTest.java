package com.musinsu.shop.style.integration;

import com.musinsa.shop.style.ShopStyleApplication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@SpringBootTest(classes = ShopStyleApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("StyleControllerIntTest.sql")
public class StyleControllerIntTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("전체 카테고리 조회")
    void test_1() throws Exception {
        mockMvc
                .perform(get("/api/v1/styles/categories"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.isSuccess").value(true),
                        jsonPath("$.data.size()").value(2),
                        jsonPath("$.data.[0].key").value("001"),
                        jsonPath("$.data.[0].value").value("상의"),
                        jsonPath("$.data.[1].key").value("002"),
                        jsonPath("$.data.[1].value").value("아우터")
                );
    }

    @Test
    @DisplayName("카테고리 별 최저가격 브랜드와 상품 가격, 총액 조회")
    void test_2() throws Exception {
        mockMvc
                .perform(get("/api/v1/styles/lowest-price-category"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.size()").value(2),
                        jsonPath("$.제품.size()").value(2),
                        jsonPath("$.제품.[0].카테고리").value("상의"),
                        jsonPath("$.제품.[0].브랜드").value("B"),
                        jsonPath("$.제품.[0].가격").value("10,500"),
                        jsonPath("$.제품.[1].카테고리").value("아우터"),
                        jsonPath("$.제품.[1].브랜드").value("A"),
                        jsonPath("$.제품.[1].가격").value("5,500")
                );
    }

    @Test
    @DisplayName("단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회")
    void test_3() throws Exception {
        mockMvc
                .perform(get("/api/v1/styles/lowest-total-price-brand"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.size()").value(1),
                        jsonPath("$.최저가.size()").value(3),
                        jsonPath("$.최저가.브랜드").value("B"),
                        jsonPath("$.최저가.카테고리.[0].카테고리").value("상의"),
                        jsonPath("$.최저가.카테고리.[0].가격").value("10,500"),
                        jsonPath("$.최저가.카테고리.[1].카테고리").value("아우터"),
                        jsonPath("$.최저가.카테고리.[1].가격").value("5,900"),
                        jsonPath("$.최저가.총액").value("16,400")
                );
    }

    @Nested
    @DisplayName("카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회")
    class CategoryTest {
        @Test
        @DisplayName("상의")
        void test_1() throws Exception {
            mockMvc
                    .perform(get("/api/v1/styles/categories/categoryCode={categoryCode}", "001"))
                    .andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.size()").value(3),
                            jsonPath("$.카테고리").value("상의"),
                            jsonPath("$.최저가.[0].브랜드").value("B"),
                            jsonPath("$.최저가.[0].가격").value("10,500"),
                            jsonPath("$.최고가.[0].브랜드").value("A"),
                            jsonPath("$.최고가.[0].가격").value("11,200")
                    );
        }

        @Test
        @DisplayName("아우터")
        void test_2() throws Exception {
            mockMvc
                    .perform(get("/api/v1/styles/categories/categoryCode={categoryCode}", "002"))
                    .andDo(print())
                    .andExpectAll(
                            jsonPath("$.size()").value(3),
                            jsonPath("$.카테고리").value("아우터"),
                            jsonPath("$.최저가.[0].브랜드").value("A"),
                            jsonPath("$.최저가.[0].가격").value("5,500"),
                            jsonPath("$.최고가.[0].브랜드").value("B"),
                            jsonPath("$.최고가.[0].가격").value("5,900")
                    );
        }

        @Test
        @DisplayName("상품이 존재하지 않을 때")
        void test_3() throws Exception {
            mockMvc
                    .perform(get("/api/v1/styles/categories/categoryCode={categoryCode}", "003"))
                    .andDo(print())
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.error.code").value("ERR-PRODUCT-001")
                    );
        }

        @Test
        @DisplayName("카테고리가 존재하지 않을 때")
        void test_4() throws Exception {
            mockMvc
                    .perform(get("/api/v1/styles/categories/categoryCode={categoryCode}", "009"))
                    .andDo(print())
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.error.code").value("ERR-CATEGORY-001")
                    );
        }
    }
}
