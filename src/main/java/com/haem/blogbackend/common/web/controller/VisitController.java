package com.haem.blogbackend.common.web.controller;

import com.haem.blogbackend.domain.visit.service.VisitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/visits")
public class VisitController {
    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping("/today")
    public long getTodayUniqueVisitors() {
        return visitService.getTodayUniqueVisitors(LocalDate.now());
    }
}
