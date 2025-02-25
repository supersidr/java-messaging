package ru.netology.creditapplicationservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.netology.creditapplicationservice.dto.CreditApplicationListResponse;
import ru.netology.creditapplicationservice.dto.CreditApplicationRequest;
import ru.netology.creditapplicationservice.dto.CreditApplicationResponse;
import ru.netology.creditapplicationservice.model.ApplicationStatus;
import ru.netology.creditapplicationservice.service.CreditApplicationService;

import java.util.List;

@RestController
@RequestMapping("/api/credit-applications")
@RequiredArgsConstructor
public class CreditApplicationController {
    private final CreditApplicationService service;

    @PostMapping
    public CreditApplicationResponse createApplication(@RequestBody CreditApplicationRequest request) {
        Long id = service.createApplication(request);
        return new CreditApplicationResponse(id);
    }

    @GetMapping("/{id}/status")
    public ApplicationStatus getApplicationStatus(@PathVariable Long id) {
        return service.getApplicationStatus(id);
    }

    @GetMapping
    public List<CreditApplicationListResponse> getAllApplications() {
        return service.getAllApplications();
    }
}

