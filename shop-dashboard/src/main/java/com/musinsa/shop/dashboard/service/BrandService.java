package com.musinsa.shop.dashboard.service;

import com.musinsa.shop.dashboard.controller.dto.BrandDto.CreateBrandRequest;
import com.musinsa.shop.dashboard.controller.dto.BrandDto.UpdateBrandRequest;
import com.musinsa.shop.dashboard.service.exception.BrandNotFoundException;
import com.musinsa.shop.dashboard.service.exception.DuplicateAliasException;
import com.musinsa.shop.dashboard.service.persistence.CommandBrandPort;
import com.musinsa.shop.dashboard.service.persistence.LoadBrandPort;
import com.musinsa.shop.domain.model.Brand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
@Transactional
public class BrandService {
    private final LoadBrandPort loadBrandPort;
    private final CommandBrandPort commandBrandPort;

    public BrandService(LoadBrandPort loadBrandPort, CommandBrandPort commandBrandPort) {
        this.loadBrandPort = loadBrandPort;
        this.commandBrandPort = commandBrandPort;
    }

    @Transactional(readOnly = true)
    public Brand getBrandById(Long brandId) {
        return findBrandByIdOrThrow(brandId);
    }

    @Transactional(readOnly = true)
    public Brand getBrandByAlias(String brandAlias) {
        return loadBrandPort.findBrandByAlias(brandAlias)
                .orElseThrow(BrandNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Brand> getAllBrands() {
        return loadBrandPort.findAllBrands();
    }

    public Brand createBrand(CreateBrandRequest request) {
        validateBrandRequest(request.alias(), request.name());
        validateAliasUniqueness(request.alias());

        Brand brand = Brand.builder()
                .alias(request.alias())
                .name(request.name())
                .build();

        return commandBrandPort.createBrand(brand);
    }

    public Brand modifyBrand(UpdateBrandRequest request) {
        Brand brand = findBrandByIdOrThrow(request.id());

        brand.update(request.alias(), request.name());

        return commandBrandPort.modifyBrand(brand);
    }

    public void deleteBrand(Long brandId) {
        findBrandByIdOrThrow(brandId);

        commandBrandPort.deleteBrand(brandId);
    }

    private Brand findBrandByIdOrThrow(Long brandId) {
        return loadBrandPort.findBrandById(brandId)
                .orElseThrow(BrandNotFoundException::new);
    }

    private void validateBrandRequest(String alias, String name) {
        Assert.hasLength(alias, "브랜드 별칭은 비워둘 수 없습니다");
        Assert.hasLength(name, "브랜드 이름은 비워둘 수 없습니다");
    }

    private void validateAliasUniqueness(String alias) {
        if (loadBrandPort.findBrandByAlias(alias).isPresent()) {
            throw new DuplicateAliasException();
        }
    }
}
