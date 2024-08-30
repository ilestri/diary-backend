package org.diary.diarybackend.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.diary.diarybackend.services.PartnerRequestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class ApiController {
    private final PartnerRequestService partnerRequestService;

    //@PostMapping(value = "partner", produces = MediaType.APPLICATION_JSON_VALUE)
}
