package com.musinsa.shop.dashboard.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.shop.dashboard.common.controller.GlobalExceptionHandler;
import com.musinsa.shop.dashboard.config.TestFilterConfig;
import com.musinsa.shop.dashboard.controller.consts.ErrorCodes;
import com.musinsa.shop.dashboard.controller.dto.ProductDto.CreateProductRequest;
import com.musinsa.shop.dashboard.controller.dto.ProductDto.UpdateProductRequest;
import com.musinsa.shop.dashboard.service.ProductService;
import com.musinsa.shop.dashboard.service.exception.BrandNotFoundException;
import com.musinsa.shop.dashboard.service.exception.CategoryNotFoundException;
import com.musinsa.shop.dashboard.service.exception.InvalidPriceException;
import com.musinsa.shop.dashboard.service.exception.ProductNotFoundException;
import com.musinsa.shop.domain.model.ProductFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

import static com.musinsa.shop.dashboard.test.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ProductController.class, GlobalExceptionHandler.class})
@Import(TestFilterConfig.class)
class ProductControllerTest {
    private static final String URL_PREFIX_V1 = "/api/v1/products";
    private static final Long PRODUCT_ID = 10L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Nested
    @DisplayName("GET /api/v1/products/{productId}")
    class GetProduct {
        private static final String URL = URL_PREFIX_V1 + "/{productId}";

        @Test
        @DisplayName("productId에 해당하는 Product가 있으면 200 OK")
        void returnOkResponse() throws Exception {
            // given
            final var product = ProductFixtures.product();
            given(productService.getProductById(any()))
                    .willReturn(product);

            // when
            final var result = mockMvc.perform(get(URL, PRODUCT_ID))
                    .andDo(print())
                    .andReturn();

            // then
            assertMvcDataEquals(result, dataField -> {
                assertEquals(product.getId(), dataField.get("id").asLong());
                assertEquals(product.getName(), dataField.get("name").asText());
                assertEquals(product.getPrice(), dataField.get("price").asLong());

                // brand 필드 검증
                JsonNode brandNode = dataField.get("brand");
                assertNotNull(brandNode);
                assertEquals(product.getBrand().getId(), brandNode.get("id").asLong());
                assertEquals(product.getBrand().getAlias(), brandNode.get("alias").asText());
                assertEquals(product.getBrand().getName(), brandNode.get("name").asText());

                // category 필드 검증
                JsonNode categoryNode = dataField.get("category");
                assertNotNull(categoryNode);
                assertEquals(product.getCategory().getCode().getKey(), categoryNode.get("key").asText());
                assertEquals(product.getCategory().getCode().getValue(), categoryNode.get("value").asText());
            });
        }

        @Test
        @DisplayName("productId에 해당하는 Product가 없으면 400 NOT FOUND")
        void returnNotFoundResponse() throws Exception {
            // given
            given(productService.getProductById(any()))
                    .willThrow(new ProductNotFoundException());

            // when
            final var result = mockMvc.perform(get(URL, PRODUCT_ID))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andReturn();

            // then
            assertMvcErrorEquals(result, ErrorCodes.PRODUCT_NOT_FOUND);

            verify(productService).getProductById(PRODUCT_ID);
        }
    }

    @Nested
    @DisplayName("브랜드 별칭 또는 카테고리 코드로 제품 목록 가져오기")
    class GetProducts {
        @Test
        @DisplayName("GET /api/v1/products?brandAlias={brandAlias}")
        void test_1() throws Exception {
            // given
            final String url = URL_PREFIX_V1 + "?brandAlias={brandAlias}";

            given(productService.getProductsByBrandAlias(any()))
                    .willReturn(List.of(ProductFixtures.product(1L), ProductFixtures.product(2L)));

            // when
            final var result = mockMvc.perform(get(url, "alias"))
                    .andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.data.size()").value(2)
                    )
                    .andReturn();

            // then
            assertMvcJsonEquals(result, json -> {
                assertEquals(1L, json.get(0).get("id").asLong());
                assertEquals("name1", json.get(0).get("name").asText());
                assertEquals(1000L, json.get(0).get("price").asLong());

                assertEquals(2L, json.get(1).get("id").asLong());
                assertEquals("name2", json.get(1).get("name").asText());
                assertEquals(2000L, json.get(1).get("price").asLong());
            });
        }

        @Test
        @DisplayName("GET /api/v1/products?categoryCode={categoryCode}")
        void test_2() throws Exception {
            // given
            final String url = URL_PREFIX_V1 + "?categoryCode={categoryCode}";

            given(productService.getProductsByCategoryCode(any()))
                    .willReturn(List.of(ProductFixtures.product(1L), ProductFixtures.product(2L)));

            // when
            final var result = mockMvc.perform(get(url, "001"))
                    .andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.data.size()").value(2)
                    )
                    .andReturn();

            // then
            assertMvcJsonEquals(result, json -> {
                assertEquals(1L, json.get(0).get("id").asLong());
                assertEquals("name1", json.get(0).get("name").asText());
                assertEquals(1000L, json.get(0).get("price").asLong());

                assertEquals(2L, json.get(1).get("id").asLong());
                assertEquals("name2", json.get(1).get("name").asText());
                assertEquals(2000L, json.get(1).get("price").asLong());
            });
        }

        @Test
        @DisplayName("브랜드 별칭이 존재하지 않으면 예외")
        void throwsException_1() throws Exception {
            // given
            final String url = URL_PREFIX_V1 + "?brandAlias={brandAlias}";

            given(productService.getProductsByBrandAlias(any()))
                    .willThrow(new BrandNotFoundException());

            // when
            final var result = mockMvc.perform(get(url, "alias"))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andReturn();

            // then
            assertMvcErrorEquals(result, ErrorCodes.BRAND_NOT_FOUND);

            verify(productService).getProductsByBrandAlias("alias");
        }

        @Test
        @DisplayName("카테고리 코드가 존재하지 않으면 예외")
        void throwsException_2() throws Exception {
            // given
            final String url = URL_PREFIX_V1 + "?categoryCode={categoryCode}";

            given(productService.getProductsByCategoryCode(any()))
                    .willThrow(new CategoryNotFoundException());

            // when
            final var result = mockMvc.perform(get(url, "001"))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andReturn();

            // then
            assertMvcErrorEquals(result, ErrorCodes.CATEGORY_NOT_FOUND);

            verify(productService).getProductsByCategoryCode("001");
        }
    }

    @Nested
    @DisplayName("POST /api/v1/products")
    class PostProduct {
        @Test
        @DisplayName("생성된 Product 반환")
        void returnProduct() throws Exception {
            // given
            final var createdProduct = ProductFixtures.product();
            given(productService.createProduct(any()))
                    .willReturn(createdProduct);

            // when
            final var request = new CreateProductRequest(1L, "001", "name", 1000L);
            final var body = objectMapper.writeValueAsString(request);
            final var result = mockMvc.perform(post(URL_PREFIX_V1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            // then
            assertMvcDataEquals(result, dataField -> {
                assertEquals(createdProduct.getId(), dataField.get("id").asLong());
                assertEquals(createdProduct.getName(), dataField.get("name").asText());
                assertEquals(createdProduct.getPrice(), dataField.get("price").asLong());

                // brand 필드 검증
                JsonNode brandNode = dataField.get("brand");
                assertNotNull(brandNode);
                assertEquals(createdProduct.getBrand().getId(), brandNode.get("id").asLong());
                assertEquals(createdProduct.getBrand().getAlias(), brandNode.get("alias").asText());
                assertEquals(createdProduct.getBrand().getName(), brandNode.get("name").asText());

                // category 필드 검증
                JsonNode categoryNode = dataField.get("category");
                assertNotNull(categoryNode);
                assertEquals(createdProduct.getCategory().getCode().getKey(), categoryNode.get("key").asText());
                assertEquals(createdProduct.getCategory().getCode().getValue(), categoryNode.get("value").asText());
            });

            verify(productService).createProduct(request);
        }

        @Test
        @DisplayName("가격이 음수라면 예외 발생")
        void throwException_1() throws Exception {
            // given
            given(productService.createProduct(any()))
                    .willThrow(new InvalidPriceException());

            // when
            final var request = new CreateProductRequest(1L, "001", "name", -1000L);
            final var body = objectMapper.writeValueAsString(request);
            final var result = mockMvc.perform(post(URL_PREFIX_V1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andReturn();

            // then
            assertMvcErrorEquals(result, ErrorCodes.INVALID_PRICE);

            verify(productService).createProduct(request);
        }

        @Test
        @DisplayName("Brand가 존재하지 않으면 404 NOT FOUND")
        void throwException_2() throws Exception {
            // given
            given(productService.createProduct(any()))
                    .willThrow(new BrandNotFoundException());

            // when
            final var request = new CreateProductRequest(10L, "001", "name", 1000L);
            final var body = objectMapper.writeValueAsString(request);
            final var result = mockMvc.perform(post(URL_PREFIX_V1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body)
                    )
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andReturn();

            // then
            assertMvcErrorEquals(result, ErrorCodes.BRAND_NOT_FOUND);

            verify(productService).createProduct(request);
        }

        @Test
        @DisplayName("Category가 존재하지 않으면 404 NOT FOUND")
        void throwException_3() throws Exception {
            // given
            given(productService.createProduct(any()))
                    .willThrow(new CategoryNotFoundException());

            // when
            final var request = new CreateProductRequest(10L, "001", "name", 1000L);
            final var body = objectMapper.writeValueAsString(request);
            final var result = mockMvc.perform(post(URL_PREFIX_V1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body)
                    )
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andReturn();

            // then
            assertMvcErrorEquals(result, ErrorCodes.CATEGORY_NOT_FOUND);

            verify(productService).createProduct(request);
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidParameters")
        @DisplayName("비정상 param이면 400 BAD REQUEST")
        void throwBadRequest(
                String message, Long brandId, String categoryCodeKey, String name, Long price
        ) throws Exception {
            // given
            final var request = new CreateProductRequest(brandId, categoryCodeKey, name, price);

            // when & then
            final var body = objectMapper.writeValueAsString(request);
            mockMvc.perform(post(URL_PREFIX_V1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        static Stream<Arguments> invalidParameters() {
            return Stream.of(
                    Arguments.of("brandId is null", null, "001", "name", 1000L),
                    Arguments.of("categoryCodeKey is null", 1L, null, "name", 1000L),
                    Arguments.of("categoryCodeKey is empty", 1L, "", "name", 1000L),
                    Arguments.of("name is null", 1L, "001", null, 1000L),
                    Arguments.of("name is empty", 1L, "001", "", 1000L)
            );
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/products")
    class PutProduct {
        @Test
        @DisplayName("Product 변경 성공 시 200 OK")
        void returnModifiedProduct() throws Exception {
            // given
            final var modifiedProduct = ProductFixtures.product();
            given(productService.modifyProduct(any()))
                    .willReturn(modifiedProduct);

            // when
            final var request = new UpdateProductRequest(1L, 10L, "001", "name", 1000L);
            final var body = objectMapper.writeValueAsString(request);
            final var result = mockMvc.perform(put(URL_PREFIX_V1)
                    .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            // then
            assertMvcDataEquals(result, dataField -> {
                assertEquals(modifiedProduct.getId(), dataField.get("id").asLong());
                assertEquals(modifiedProduct.getName(), dataField.get("name").asText());
                assertEquals(modifiedProduct.getPrice(), dataField.get("price").asLong());

                // brand 필드 검증
                JsonNode brandNode = dataField.get("brand");
                assertNotNull(brandNode);
                assertEquals(modifiedProduct.getBrand().getId(), brandNode.get("id").asLong());
                assertEquals(modifiedProduct.getBrand().getAlias(), brandNode.get("alias").asText());
                assertEquals(modifiedProduct.getBrand().getName(), brandNode.get("name").asText());

                // category 필드 검증
                JsonNode categoryNode = dataField.get("category");
                assertNotNull(categoryNode);
                assertEquals(modifiedProduct.getCategory().getCode().getKey(), categoryNode.get("key").asText());
                assertEquals(modifiedProduct.getCategory().getCode().getValue(), categoryNode.get("value").asText());
            });
        }

        @Test
        @DisplayName("가격이 음수라면 예외 발생")
        void throwException_1() throws Exception {
            // given
            given(productService.modifyProduct(any()))
                    .willThrow(new InvalidPriceException());

            // when
            final var request = new UpdateProductRequest(1L, 10L, "001", "name", -1000L);
            final var body = objectMapper.writeValueAsString(request);
            final var result = mockMvc.perform(put(URL_PREFIX_V1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andReturn();

            // then
            assertMvcErrorEquals(result, ErrorCodes.INVALID_PRICE);

            verify(productService).modifyProduct(request);
        }

        @Test
        @DisplayName("Product가 존재하지 않으면 404 NOT FOUND")
        void throwException_2() throws Exception {
            // given
            given(productService.modifyProduct(any()))
                    .willThrow(new ProductNotFoundException());

            // when
            final var request = new UpdateProductRequest(1L, 10L, "001", "name", 1000L);
            final var body = objectMapper.writeValueAsString(request);
            final var result = mockMvc.perform(put(URL_PREFIX_V1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body)
                    )
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andReturn();

            // then
            assertMvcErrorEquals(result, ErrorCodes.PRODUCT_NOT_FOUND);

            verify(productService).modifyProduct(request);
        }

        @Test
        @DisplayName("Brand가 존재하지 않으면 404 NOT FOUND")
        void throwException_3() throws Exception {
            // given
            given(productService.modifyProduct(any()))
                    .willThrow(new BrandNotFoundException());

            // when
            final var request = new UpdateProductRequest(1L, 10L, "001", "name", 1000L);
            final var body = objectMapper.writeValueAsString(request);
            final var result = mockMvc.perform(put(URL_PREFIX_V1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body)
                    )
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andReturn();

            // then
            assertMvcErrorEquals(result, ErrorCodes.BRAND_NOT_FOUND);

            verify(productService).modifyProduct(request);
        }

        @Test
        @DisplayName("Category가 존재하지 않으면 404 NOT FOUND")
        void throwException_4() throws Exception {
            // given
            given(productService.modifyProduct(any()))
                    .willThrow(new CategoryNotFoundException());

            // when
            final var request = new UpdateProductRequest(1L, 10L, "001", "name", 1000L);
            final var body = objectMapper.writeValueAsString(request);
            final var result = mockMvc.perform(put(URL_PREFIX_V1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body)
                    )
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andReturn();

            // then
            assertMvcErrorEquals(result, ErrorCodes.CATEGORY_NOT_FOUND);

            verify(productService).modifyProduct(request);
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidParameters")
        @DisplayName("비정상 param이면 400 BAD REQUEST")
        void throwBadRequest(
                String message, Long id, Long brandId, String categoryCodeKey, String name, Long price
        ) throws Exception {
            // given
            final var request = new UpdateProductRequest(id, brandId, categoryCodeKey, name, price);

            // when & then
            final var body = objectMapper.writeValueAsString(request);
            mockMvc.perform(put(URL_PREFIX_V1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        static Stream<Arguments> invalidParameters() {
            return Stream.of(
                    Arguments.of("id is null", null, 1L, "001", "name", 1000L),
                    Arguments.of("brandId is null", 10L, null, "001", "name", 1000L),
                    Arguments.of("categoryCodeKey is null", 10L, 1L, null, "name", 1000L),
                    Arguments.of("categoryCodeKey is empty", 10L, 1L, "", "name", 1000L),
                    Arguments.of("name is null", 10L, 1L, "001", null, 1000L),
                    Arguments.of("name is empty", 10L, 1L, "001", "", 1000L)
            );
        }
    }

    @Test
    @DisplayName("DELETE /api/v1/products/{productId}")
    void deleteProduct() throws Exception {
        // given
        final var product = ProductFixtures.product();
        given(productService.getProductById(any()))
                .willReturn(product);
        willDoNothing()
                .given(productService).deleteProduct(any());

        // when
        mockMvc.perform(delete(URL_PREFIX_V1 + "/{productId}", PRODUCT_ID))
                .andDo(print())
                .andExpect(status().isOk());

        // then
        verify(productService).deleteProduct(PRODUCT_ID);
    }
}