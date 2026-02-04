package com.haui.foxtrip.repository;

import com.haui.foxtrip.entity.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TourRepository extends JpaRepository<Tour, UUID> {
    
    // Tìm tour theo slug (không bị xóa)
    @Query("SELECT t FROM Tour t WHERE t.slug = :slug AND t.deletedAt IS NULL")
    Optional<Tour> findBySlug(@Param("slug") String slug);
    
    // Tìm tất cả tour có short URL (không bị xóa)
    @Query("SELECT t FROM Tour t WHERE t.shortUrl IS NOT NULL AND t.shortUrl != '' AND t.deletedAt IS NULL")
    List<Tour> findAllWithShortUrl();
    
    // Đếm số tour có short URL
    @Query("SELECT COUNT(t) FROM Tour t WHERE t.shortUrl IS NOT NULL AND t.shortUrl != '' AND t.deletedAt IS NULL")
    long countToursWithShortUrl();
    
    // Tìm tour theo vùng miền
    @Query("SELECT t FROM Tour t WHERE t.region = :region AND t.isBookable = true AND t.deletedAt IS NULL")
    Page<Tour> findByRegion(@Param("region") String region, Pageable pageable);
    
    // Tìm tour theo tỉnh
    @Query("SELECT t FROM Tour t WHERE t.province = :province AND t.isBookable = true AND t.deletedAt IS NULL")
    Page<Tour> findByProvince(@Param("province") String province, Pageable pageable);
    
    // Tìm tour theo danh mục
    @Query("SELECT t FROM Tour t WHERE t.category = :category AND t.isBookable = true AND t.deletedAt IS NULL")
    Page<Tour> findByCategory(@Param("category") String category, Pageable pageable);
    
    // Tìm tour theo khoảng ngày
    @Query("SELECT t FROM Tour t WHERE t.startDate >= :startDate AND t.endDate <= :endDate AND t.isBookable = true AND t.deletedAt IS NULL")
    Page<Tour> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
    
    // Tìm tour có thể đặt
    @Query("SELECT t FROM Tour t WHERE t.isBookable = true AND t.availableSlots > 0 AND t.deletedAt IS NULL")
    Page<Tour> findBookableTours(Pageable pageable);
    
    // Tìm tất cả tour (không bị xóa)
    @Query("SELECT t FROM Tour t WHERE t.deletedAt IS NULL")
    Page<Tour> findAllActive(Pageable pageable);
    
    // Tìm tour theo ID (không bị xóa)
    @Query("SELECT t FROM Tour t WHERE t.id = :id AND t.deletedAt IS NULL")
    Optional<Tour> findByIdActive(@Param("id") UUID id);
    
    // Tìm kiếm tour theo tên hoặc mô tả
    @Query("SELECT t FROM Tour t WHERE (LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND t.deletedAt IS NULL")
    Page<Tour> searchTours(@Param("keyword") String keyword, Pageable pageable);
}
