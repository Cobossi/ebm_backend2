package com.expert.beaute.mobile_backend.expert.search;


import com.expert.beaute.mobile_backend.expert.Expert;
import com.expert.beaute.mobile_backend.expert.ExpertResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("expert")
public class SearchExpertController {

    private final SearchExpertService expertService;

    @Autowired
    public SearchExpertController(SearchExpertService expertService) {
        this.expertService = expertService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<ExpertResponse>> searchExperts(@RequestParam(required = false) String nom,
                                                              @RequestParam(required = false) String prenom,
                                                              @RequestParam(required = false) String serviceName,
                                                              @RequestParam(required = false) BigDecimal minPrice,
                                                              @RequestParam(required = false) BigDecimal maxPrice,
                                                              @RequestParam(required = false) Double minRating) {
        List<ExpertResponse> experts =expertService.searchExperts(nom, prenom, serviceName, minPrice, maxPrice, minRating);
    return  ResponseEntity.ok(experts);
    }
}
