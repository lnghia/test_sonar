package com.example.demo.controllers.admin;

import com.example.demo.dto.requests.technology.TechnologyCreateRequestDto;
import com.example.demo.dto.responses.ResponseBodyDto;
import com.example.demo.dto.responses.technology.TechnologyResponseDto;
import com.example.demo.entities.factories.responsebodydto.ResponseBodyDtoFactory;
import com.example.demo.services.interfaces.technology.TechnologyCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/admin/technology")
@RequiredArgsConstructor
public class TechnologyAdminController {
  private final TechnologyCrudService technologyCrudService;

  private final ResponseBodyDtoFactory responseBodyDtoFactory;

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<ResponseBodyDto<TechnologyResponseDto>> createTechnology(
      @Valid @RequestBody TechnologyCreateRequestDto createRequestDto) {
    TechnologyResponseDto createdTechnology =
        technologyCrudService.createTechnology(createRequestDto);
    ResponseBodyDto<TechnologyResponseDto> responseBodyDto =
        responseBodyDtoFactory.buildResponseBody(createdTechnology, "200");

    return ResponseEntity.ok(responseBodyDto);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping
  public ResponseEntity<ResponseBodyDto<TechnologyResponseDto>> updateTechnology(
      @RequestParam(name = "id") Long technologyId,
      @Valid @RequestBody TechnologyCreateRequestDto requestDto) {
    TechnologyResponseDto updatedTechnology =
        technologyCrudService.updateTechnology(technologyId, requestDto);
    ResponseBodyDto<TechnologyResponseDto> responseBodyDto =
        responseBodyDtoFactory.buildResponseBody(updatedTechnology, "200");

    return ResponseEntity.ok(responseBodyDto);
  }
}
