package com.example.demo.controllers.user;

import com.example.demo.dto.responses.ResponseBodyDto;
import com.example.demo.dto.responses.size.SizeResponseDto;
import com.example.demo.entities.factories.responsebodydto.ResponseBodyDtoFactory;
import com.example.demo.services.interfaces.size.SizeDatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/size")
@RequiredArgsConstructor
public class SizeUserController {
  private final SizeDatabaseService sizeDatabaseService;

  private final ResponseBodyDtoFactory responseBodyDtoFactory;

  @GetMapping
  public ResponseEntity<ResponseBodyDto<List<SizeResponseDto>>> getAll() {
    List<SizeResponseDto> data = sizeDatabaseService.getAll();
    ResponseBodyDto<List<SizeResponseDto>> response =
        responseBodyDtoFactory.buildResponseBody(data, "200");

    return ResponseEntity.ok(response);
  }
}
