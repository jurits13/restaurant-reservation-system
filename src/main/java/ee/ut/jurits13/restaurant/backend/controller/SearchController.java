package ee.ut.jurits13.restaurant.backend.controller;

import ee.ut.jurits13.restaurant.backend.dto.SearchRequest;
import ee.ut.jurits13.restaurant.backend.dto.SearchResult;
import ee.ut.jurits13.restaurant.backend.service.SearchService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping
    public SearchResult search(@Valid @RequestBody SearchRequest request) {
        return searchService.search(request);
    }
}