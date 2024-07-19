package com.musinsa.shop.dashboard.service;

import com.musinsa.shop.dashboard.controller.dto.BrandDto.CreateBrandRequest;
import com.musinsa.shop.dashboard.controller.dto.BrandDto.UpdateBrandRequest;
import com.musinsa.shop.dashboard.service.exception.BrandNotFoundException;
import com.musinsa.shop.dashboard.service.exception.DuplicateAliasException;
import com.musinsa.shop.dashboard.service.persistence.CommandBrandPort;
import com.musinsa.shop.dashboard.service.persistence.LoadBrandPort;
import com.musinsa.shop.domain.model.Brand;
import com.musinsa.shop.domain.model.BrandFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {
    private BrandService sut;

    @Mock
    private LoadBrandPort loadBrandPort;
    @Mock
    private CommandBrandPort commandBrandPort;

    @BeforeEach
    void setUp() {
        sut = new BrandService(loadBrandPort, commandBrandPort);
    }

    @Nested
    @DisplayName("Brand 조회")
    class GetBrand {
        @Test
        @DisplayName("brandId로 조회 시 Brand 반환")
        void returnBrandByBrandId() {
            // given
            final var brand = BrandFixtures.brand();
            given(loadBrandPort.findBrandById(any()))
                    .willReturn(Optional.of(brand));

            // when
            final var result = sut.getBrandById(5L);

            // then
            then(result)
                    .isNotNull()
                    .hasNoNullFieldsOrProperties()
                    .hasFieldOrPropertyWithValue("id", brand.getId())
                    .hasFieldOrPropertyWithValue("alias", brand.getAlias())
                    .hasFieldOrPropertyWithValue("name", brand.getName());
        }

        @Test
        @DisplayName("alias로 조회 시 Brand 반환")
        void returnBrandByAlias() {
            // given
            final var brand = BrandFixtures.brand();
            given(loadBrandPort.findBrandByAlias(any()))
                    .willReturn(Optional.of(brand));

            // when
            final var result = sut.getBrandByAlias("musinsastandard");

            // then
            then(result)
                    .isNotNull()
                    .hasNoNullFieldsOrProperties()
                    .hasFieldOrPropertyWithValue("id", brand.getId())
                    .hasFieldOrPropertyWithValue("alias", brand.getAlias())
                    .hasFieldOrPropertyWithValue("name", brand.getName());
        }

        @Test
        @DisplayName("brandId 조회 시 Brand 존재하지 않을 경우 BrandNotFoundException throw")
        void throwBrandNotFoundException_1() {
            // given
            given(loadBrandPort.findBrandById(any()))
                    .willReturn(Optional.empty());

            // when & then
            thenThrownBy(() -> sut.getBrandById(5L))
                    .isInstanceOf(BrandNotFoundException.class);
        }

        @Test
        @DisplayName("alias 조회 시 Brand 존재하지 않을 경우 BrandNotFoundException throw")
        void throwBrandNotFoundException_2() {
            // given
            given(loadBrandPort.findBrandByAlias(any()))
                    .willReturn(Optional.empty());

            // when & then
            thenThrownBy(() -> sut.getBrandByAlias("musinsastandard"))
                    .isInstanceOf(BrandNotFoundException.class);
        }
    }

    @Test
    @DisplayName("Brand 전체 목록 조회")
    void getAllBrands() {
        // given
        final var brand1 = BrandFixtures.brand(1L);
        final var brand2 = BrandFixtures.brand(2L);
        given(loadBrandPort.findAllBrands())
                .willReturn(List.of(brand1, brand2));

        // when
        final var result = sut.getAllBrands();

        // then
        then(result)
                .hasSize(2);
    }

    @Nested
    @DisplayName("Brand 생성")
    class CreateBrand {
        @Test
        @DisplayName("생성된 Brand 확인")
        void returnCreatedBrand() {
            // given
            final var request = new CreateBrandRequest("musinsastandard", "무신사 스탠다드");

            final var createdBrand = BrandFixtures.brand();
            given(commandBrandPort.createBrand(any()))
                    .willReturn(createdBrand);

            // when
            final var result = sut.createBrand(request);

            // then
            then(result)
                    .isEqualTo(createdBrand);
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidParameters")
        @DisplayName("정상적이지 않은 param이면 IllegalArgumentException")
        void throwIllegalArgumentException(String message, String alias, String name) {
            // given
            final var request = new CreateBrandRequest(alias, name);

            // when & then
            thenThrownBy(() -> sut.createBrand(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        static Stream<Arguments> invalidParameters() {
            return Stream.of(
                    Arguments.of("alias null", null, "name"),
                    Arguments.of("alias empty", "", "name"),
                    Arguments.of("name null", "alias", null),
                    Arguments.of("alias empty", "alias", "")
            );
        }
    }

    @Nested
    @DisplayName("Brand 변경")
    class ModifyBrand {
        private UpdateBrandRequest request;

        @BeforeEach
        void setUp() {
            request = new UpdateBrandRequest(6L, "new alias", "new name");
        }

        @Test
        @DisplayName("변경된 Brand 확인")
        void returnModifiedBrand() {
            // given
            final var brand = BrandFixtures.brand();
            given(loadBrandPort.findBrandById(any()))
                    .willReturn(Optional.of(brand));

            final var modifiedBrand = Brand.builder()
                    .id(brand.getId())
                    .alias("new alias")
                    .name("new name")
                    .build();
            given(commandBrandPort.modifyBrand(any()))
                    .willReturn(modifiedBrand);

            // when
            final var result = sut.modifyBrand(request);

            // then
            then(result)
                    .isEqualTo(modifiedBrand);
        }

        @Test
        @DisplayName("중복된 alias로 수정하면 예외")
        void throwDuplicateAliasException() {
            // given
            final var brand = BrandFixtures.brand();
            given(loadBrandPort.findBrandById(any()))
                    .willReturn(Optional.of(brand));

            final var modifiedBrand = Brand.builder()
                    .id(brand.getId())
                    .alias("new alias")
                    .name("new name")
                    .build();
            given(loadBrandPort.findBrandByAlias(any()))
                    .willReturn(Optional.of(modifiedBrand));

            // when & then
            thenThrownBy(() -> sut.modifyBrand(request))
                    .isInstanceOf(DuplicateAliasException.class);
        }
    }

    @Test
    @DisplayName("Brand 삭제")
    void deleteBrand() {
        // given
        final var brand = BrandFixtures.brand();
        given(loadBrandPort.findBrandById(any()))
                .willReturn(Optional.of(brand));
        willDoNothing()
                .given(commandBrandPort).deleteBrand(any());

        // when
        sut.deleteBrand(5L);

        // then
        verify(commandBrandPort).deleteBrand(5L);
    }
}