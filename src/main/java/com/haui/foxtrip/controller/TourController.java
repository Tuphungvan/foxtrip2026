package com.haui.foxtrip.controller;

import com.haui.foxtrip.dto.common.ApiResponse;
import com.haui.foxtrip.dto.common.PaginationResponse;
import com.haui.foxtrip.dto.tour.TourRequestDTO;
import com.haui.foxtrip.dto.tour.TourResponseDTO;
import com.haui.foxtrip.dto.tour.TourShortDTO;
import com.haui.foxtrip.dto.tour.TourUpdateDTO;
import com.haui.foxtrip.service.TourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tours")
@RequiredArgsConstructor
@Tag(name = "Tour Management", description = "APIs quản lý tour du lịch")
public class TourController {
    
    private final TourService tourService;
    
    /**
     * Tạo tour mới (Admin only)
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Tạo tour mới", description = "Tạo tour mới với ảnh (chỉ Admin)")
    public ApiResponse<TourResponseDTO> createTour(
            @Valid @RequestPart("tour") TourRequestDTO requestDTO,
            @RequestPart("images") List<MultipartFile> images) {
        
        TourResponseDTO tour = tourService.createTour(requestDTO, images);
        return ApiResponse.success(tour, "Tạo tour thành công", HttpStatus.CREATED.value());
    }
    
    /**
     * Lấy tất cả tour (có phân trang)
     */
    @GetMapping
    @Operation(summary = "Lấy danh sách tour", description = "Lấy tất cả tour với phân trang")
    public ApiResponse<PaginationResponse<TourResponseDTO>> getAllTours(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("ASC") 
                ? Sort.by(sortBy).ascending() 
                : Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<TourResponseDTO> tourPage = tourService.getAllTours(pageable);
        
        PaginationResponse<TourResponseDTO> response = PaginationResponse.from(tourPage);
        
        return ApiResponse.success(response, "Lấy danh sách tour thành công");
    }
    
    /**
     * Lấy tour theo ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Lấy tour theo ID", description = "Lấy thông tin chi tiết tour theo ID")
    public ApiResponse<TourResponseDTO> getTourById(@PathVariable UUID id) {
        TourResponseDTO tour = tourService.getTourById(id);
        return ApiResponse.success(tour, "Lấy thông tin tour thành công");
    }
    
    /**
     * Lấy tour theo slug
     */
    @GetMapping("/slug/{slug}")
    @Operation(summary = "Lấy tour theo slug", description = "Lấy thông tin chi tiết tour theo slug")
    public ApiResponse<TourResponseDTO> getTourBySlug(@PathVariable String slug) {
        TourResponseDTO tour = tourService.getTourBySlug(slug);
        return ApiResponse.success(tour, "Lấy thông tin tour thành công");
    }
    
    /**
     * Cập nhật tour (Admin only)
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Cập nhật tour", description = "Cập nhật thông tin tour (chỉ Admin)")
    public ApiResponse<TourResponseDTO> updateTour(
            @PathVariable UUID id,
            @Valid @RequestPart("tour") TourUpdateDTO updateDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        
        TourResponseDTO tour = tourService.updateTour(id, updateDTO, images);
        return ApiResponse.success(tour, "Cập nhật tour thành công");
    }
    
    /**
     * Xóa tour (Admin only)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Xóa tour", description = "Xóa tour (soft delete, chỉ Super Admin)")
    public ApiResponse<Void> deleteTour(@PathVariable UUID id) {
        tourService.deleteTour(id);
        return ApiResponse.success(null, "Xóa tour thành công");
    }
    
    /**
     * Lấy danh sách tour shorts
     */
    @GetMapping("/shorts")
    @Operation(summary = "Lấy danh sách tour shorts", description = "Lấy danh sách tour có video short")
    public ApiResponse<List<TourShortDTO>> getTourShorts() {
        List<TourShortDTO> shorts = tourService.getTourShorts();
        return ApiResponse.success(shorts, "Lấy danh sách video short thành công");
    }
    
    /**
     * Lấy một tour short ngẫu nhiên
     */
    @GetMapping("/shorts/random")
    @Operation(summary = "Lấy tour short ngẫu nhiên", description = "Lấy một video short ngẫu nhiên")
    public ApiResponse<TourShortDTO> getRandomTourShort() {
        TourShortDTO shortDTO = tourService.getRandomTourShort();
        
        if (shortDTO == null) {
            return ApiResponse.success(null, "Không có video nào");
        }
        
        return ApiResponse.success(shortDTO, "Lấy video ngẫu nhiên thành công");
    }
    
    /**
     * Tìm tour theo vùng miền
     */
    @GetMapping("/region/{region}")
    @Operation(summary = "Tìm tour theo vùng", description = "Tìm tour theo vùng miền (Bắc, Trung, Nam)")
    public ApiResponse<PaginationResponse<TourResponseDTO>> getToursByRegion(
            @PathVariable String region,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<TourResponseDTO> tourPage = tourService.getToursByRegion(region, pageable);
        
        return ApiResponse.success(PaginationResponse.from(tourPage), "Tìm tour theo vùng thành công");
    }
    
    /**
     * Tìm tour theo tỉnh
     */
    @GetMapping("/province/{province}")
    @Operation(summary = "Tìm tour theo tỉnh", description = "Tìm tour theo tỉnh/thành phố")
    public ApiResponse<PaginationResponse<TourResponseDTO>> getToursByProvince(
            @PathVariable String province,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<TourResponseDTO> tourPage = tourService.getToursByProvince(province, pageable);
        
        return ApiResponse.success(PaginationResponse.from(tourPage), "Tìm tour theo tỉnh thành công");
    }
    
    /**
     * Tìm tour theo danh mục
     */
    @GetMapping("/category/{category}")
    @Operation(summary = "Tìm tour theo danh mục", description = "Tìm tour theo danh mục (Biển, Văn hóa, ...)")
    public ApiResponse<PaginationResponse<TourResponseDTO>> getToursByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<TourResponseDTO> tourPage = tourService.getToursByCategory(category, pageable);
        
        return ApiResponse.success(PaginationResponse.from(tourPage), "Tìm tour theo danh mục thành công");
    }
    
    /**
     * Lấy tour có thể đặt
     */
    @GetMapping("/bookable")
    @Operation(summary = "Lấy tour có thể đặt", description = "Lấy danh sách tour còn chỗ và có thể đặt")
    public ApiResponse<PaginationResponse<TourResponseDTO>> getBookableTours(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<TourResponseDTO> tourPage = tourService.getBookableTours(pageable);
        
        return ApiResponse.success(PaginationResponse.from(tourPage), "Lấy tour có thể đặt thành công");
    }
    
    /**
     * Tìm kiếm tour
     */
    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm tour", description = "Tìm kiếm tour theo tên hoặc mô tả")
    public ApiResponse<PaginationResponse<TourResponseDTO>> searchTours(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<TourResponseDTO> tourPage = tourService.searchTours(keyword, pageable);
        
        return ApiResponse.success(PaginationResponse.from(tourPage), "Tìm kiếm tour thành công");
    }
}
