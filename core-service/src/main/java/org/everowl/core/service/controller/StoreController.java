package org.everowl.core.service.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.store.response.StoreRes;
import org.everowl.core.service.dto.store.response.StoresRes;
import org.everowl.core.service.service.StoreDomain;
import org.everowl.shared.service.annotation.ValidInteger;
import org.everowl.shared.service.dto.BaseSuccessResponseBodyModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class StoreController {
    private final StoreDomain storeDomain;

    @GetMapping("/public/stores")
    public ResponseEntity<BaseSuccessResponseBodyModel> getAllStores() {
        List<StoresRes> response = storeDomain.getStores();

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping(path = "/public/store", produces = {
            MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity<BaseSuccessResponseBodyModel> getStoreDetails(@RequestParam(value = "storeId")
                                                                        @ValidInteger(message = "Please ensure a valid store ID is provided")
                                                                        @NotBlank(message = "Please ensure the store ID is not blank") String storeId) {
        StoreRes response = storeDomain.getStoreDetails(Integer.parseInt(storeId));

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}