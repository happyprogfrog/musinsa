package com.musinsa.shop.dashboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.shop.dashboard.common.controller.GlobalExceptionHandler;
import com.musinsa.shop.dashboard.config.TestFilterConfig;
import com.musinsa.shop.dashboard.controller.consts.ErrorCodes;
import com.musinsa.shop.dashboard.controller.dto.BrandDto.CreateBrandRequest;
import com.musinsa.shop.dashboard.service.BrandService;
import com.musinsa.shop.dashboard.service.exception.BrandNotFoundException;
import com.musinsa.shop.dashboard.service.exception.DuplicateAliasException;
import com.musinsa.shop.domain.model.BrandFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.musinsa.shop.dashboard.controller.dto.BrandDto.UpdateBrandRequest;
import static com.musinsa.shop.dashboard.test.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {BrandController.class, GlobalExceptionHandler.class})
@Import(TestFilterConfig.class)
class BrandControllerTest {
    private static final String URL_PREFIX_V1 = "/api/v1/brands";
    private static final Long BRAND_ID = 5L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BrandService brandService;

    @Nested
    @DisplayName("GET /api/v1/brands/{brandId}")
    class GetBrand {
        @Test
        @DisplayName("brandId에 해당하는 Brand가 있으면 200 OK")
        void returnOkResponse() throws Exception {
            // given
            final String url = URL_PREFIX_V1 + "/{brandId}";

            final var brand = BrandFixtures.brand();
            given(brandService.getBrandById(any()))
                    .willReturn(brand);

            // when
            final var result = mockMvc.perform(get(url, BRAND_ID))
                    .andDo(print())
                    .andReturn();

            // then
            assertMvcDataEquals(result, dataField -> {
                assertEquals(brand.getId(), dataField.get("id").asLong());
                assertEquals(brand.getAlias(), dataField.get("alias").asText());
                assertEquals(brand.getName(), dataField.get("name").asText());
            });

            verify(brandService).getBrandById(BRAND_ID);
        }

        @Test
        @DisplayName("brandId에 해당하는 Brand가 없으면 400 NOT FOUND")
        void returnNotFoundResponse() throws Exception {
            // given
            final String url = URL_PREFIX_V1 + "/{brandId}";

            given(brandService.getBrandById(any()))
                    .willThrow(new BrandNotFoundException());

            // when
            final var result = mockMvc.perform(get(url, BRAND_ID))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andReturn();

            // then
            assertMvcErrorEquals(result, ErrorCodes.BRAND_NOT_FOUND);

            verify(brandService).getBrandById(BRAND_ID);
        }
    }

    @Test
    @DisplayName("GET /api/v1/brands")
    void getAllBrands() throws Exception {
        // given
        given(brandService.getAllBrands())
                .willReturn(List.of(BrandFixtures.brand(1L), BrandFixtures.brand(2L)));

        // when
        final var result = mockMvc.perform(get(URL_PREFIX_V1))
                .andDo(print())
                .andExpectAll(
                    status().isOk(),
                    jsonPath("$.data.size()").value(2)
                )
                .andReturn();

        // then
        assertMvcJsonEquals(result, json -> {
            assertEquals(1L, json.get(0).get("id").asLong());
            assertEquals("alias1", json.get(0).get("alias").asText());
            assertEquals("name1", json.get(0).get("name").asText());

            assertEquals(2L, json.get(1).get("id").asLong());
            assertEquals("alias2", json.get(1).get("alias").asText());
            assertEquals("name2", json.get(1).get("name").asText());
        });

        verify(brandService).getAllBrands();
    }

    @Nested
    @DisplayName("POST /api/v1/brands")
    class PostBrand {
        @Test
        @DisplayName("생성된 Brand 반환")
        void returnBrand() throws Exception {
            // given
            final var createdBrand = BrandFixtures.brand();
            given(brandService.createBrand(any()))
                    .willReturn(createdBrand);

            // when
            final var request = new CreateBrandRequest("musinsastandard", "무신사 스탠다드");
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
                final var actualBrandAlias = dataField.get("alias").asText();
                assertEquals(createdBrand.getAlias(), actualBrandAlias);

                final var actualBrandName = dataField.get("name").asText();
                assertEquals(createdBrand.getName(), actualBrandName);
            });

            verify(brandService).createBrand(request);
        }

        @ParameterizedTest(name = "{0}")
        @DisplayName("비정상 param이면 400 BAD REQUEST")
        @CsvSource(
                value = {
                        "alias is null, , name",
                        "alias is empty, '', name",
                        "name is null, alias, ",
                        "name is empty, alias, ''"
                }
        )
        void throwInvalidParamBadRequest(String desc, String alias, String name) throws Exception {
            // given
            final var request = new CreateBrandRequest(alias, name);

            // when & then
            final var body = objectMapper.writeValueAsString(request);
            mockMvc.perform(post(URL_PREFIX_V1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                    )
                    .andDo(print())
                    .andExpectAll(status().isBadRequest());
        }

        @Test
        @DisplayName("이미 존재하는 alias이면 409 CONFLICT")
        void returnDuplicateAliasResponse() throws Exception {
            // given
            given(brandService.createBrand(any()))
                    .willThrow(new DuplicateAliasException());

            // when
            final var request = new CreateBrandRequest("musinsastandard", "무신사 스탠다드");
            final var body = objectMapper.writeValueAsString(request);
            final var result = mockMvc.perform(post(URL_PREFIX_V1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                    )
                    .andDo(print())
                    .andExpect(status().isConflict())
                    .andReturn();

            // then
            assertMvcErrorEquals(result, ErrorCodes.DUPLICATE_ALIAS);

            verify(brandService).createBrand(request);
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/brands")
    class PutBrand {
        @Test
        @DisplayName("Brand 변경 성공시 200 OK")
        void returnModifiedBrand() throws Exception {
            // given
            var modifiedBrand = BrandFixtures.brand();
            given(brandService.modifyBrand(any()))
                    .willReturn(modifiedBrand);

            // when
            final var request = new UpdateBrandRequest(5L, "musinsastandard" , "무신사 스탠다드");
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
                assertEquals(modifiedBrand.getId(), dataField.get("id").asLong());
                assertEquals(modifiedBrand.getAlias(), dataField.get("alias").asText());
                assertEquals(modifiedBrand.getName(), dataField.get("name").asText());
            });
        }

        @ParameterizedTest(name = "{0}")
        @DisplayName("비정상 param이면 400 BAD REQUEST")
        @CsvSource(
                value = {
                        "alias is null, , name",
                        "alias is empty, '', name",
                        "name is null, alias, ",
                        "name is empty, alias, ''"
                }
        )
        void throwInvalidParamBadRequest(String desc, String alias, String name) throws Exception {
            // given
            final var request = new CreateBrandRequest(alias, name);

            // when & then
            final var body = objectMapper.writeValueAsString(request);
            mockMvc.perform(put(URL_PREFIX_V1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    @DisplayName("DELETE /api/v1/brands/{brandId}")
    void deleteBrand() throws Exception {
        // given
        final var brand = BrandFixtures.brand();
        given(brandService.getBrandById(any()))
                .willReturn(brand);
        willDoNothing()
                .given(brandService).deleteBrand(any());

        // when
        mockMvc.perform(delete(URL_PREFIX_V1 + "/{brandId}", BRAND_ID))
                .andDo(print())
                .andExpect(status().isOk());

        // then
        verify(brandService).deleteBrand(BRAND_ID);
    }
}