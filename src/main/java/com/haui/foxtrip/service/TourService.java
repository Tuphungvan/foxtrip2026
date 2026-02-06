package com.haui.foxtrip.service;

import com.github.slugify.Slugify;
import com.haui.foxtrip.dto.tour.TourRequestDTO;
import com.haui.foxtrip.dto.tour.TourResponseDTO;
import com.haui.foxtrip.dto.tour.TourShortDTO;
import com.haui.foxtrip.dto.tour.TourUpdateDTO;
import com.haui.foxtrip.entity.Tour;
import com.haui.foxtrip.exception.BadRequestException;
import com.haui.foxtrip.exception.ResourceNotFoundException;
import com.haui.foxtrip.mapper.TourMapper;
import com.haui.foxtrip.repository.TourRepository;
import com.haui.foxtrip.util.SearchUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TourService {

    private final TourRepository tourRepository;
    private final TourMapper tourMapper;
    private final CloudinaryService cloudinaryService;
    private final Slugify slugify = Slugify.builder().build();
    private final Random random = new Random();

    /**
     * Tạo tour mới với ảnh
     */
    @Transactional
    public TourResponseDTO createTour(TourRequestDTO requestDTO, List<MultipartFile> imageFiles) {
        // Validate
        validateTourDates(requestDTO.getStartDate(), requestDTO.getEndDate());

        if (imageFiles == null || imageFiles.isEmpty()) {
            throw new BadRequestException("Tour phải có ít nhất 1 ảnh");
        }

        try {
            // Upload ảnh lên Cloudinary
            List<String> imageUrls = cloudinaryService.uploadImages(imageFiles, requestDTO.getName());

            // Tạo entity
            Tour tour = tourMapper.toEntity(requestDTO);
            tour.setImages(imageUrls.toArray(new String[0]));
            tour.setAvailableSlots(requestDTO.getSlots());

            // Generate slug
            tour.setSlug(slugify.slugify(requestDTO.getName()));

            if (tour.getIsBookable() == null) {
                tour.setIsBookable(true);
            }

            // Lưu vào DB
            Tour savedTour = tourRepository.save(tour);
            log.info("Created tour: {} with {} images", savedTour.getName(), imageUrls.size());
            return tourMapper.toResponseDTO(savedTour);
        } catch (Exception e) {
            log.error("❌ Error uploading images for tour: {}", requestDTO.getName(), e);
            throw new BadRequestException("Lỗi upload ảnh: " + e.getMessage());
        }
    }

    /**
     * Lấy tất cả tour (có phân trang)
     */
    @Transactional(readOnly = true)
    public Page<TourResponseDTO> getAllTours(Pageable pageable) {
        return tourRepository.findAllActive(pageable)
                .map(tourMapper::toResponseDTO);
    }

    /**
     * Lấy tour theo ID
     */
    @Transactional(readOnly = true)
    public TourResponseDTO getTourById(UUID id) {
        Tour tour = tourRepository.findByIdActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tour với ID: " + id));
        return tourMapper.toResponseDTO(tour);
    }

    /**
     * Lấy tour theo slug
     */
    @Transactional(readOnly = true)
    public TourResponseDTO getTourBySlug(String slug) {
        Tour tour = tourRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tour với slug: " + slug));
        return tourMapper.toResponseDTO(tour);
    }

    /**
     * Cập nhật tour
     */
    @Transactional
    public TourResponseDTO updateTour(UUID id, TourUpdateDTO updateDTO, List<MultipartFile> newImages) {
        Tour tour = tourRepository.findByIdActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tour với ID: " + id));

        // Validate dates nếu có update
        if (updateDTO.getStartDate() != null && updateDTO.getEndDate() != null) {
            validateTourDates(updateDTO.getStartDate(), updateDTO.getEndDate());
        }

        // Update thông tin cơ bản
        tourMapper.updateEntityFromDTO(updateDTO, tour);

        // Upload ảnh mới nếu có
        if (newImages != null && !newImages.isEmpty()) {
            // Xóa ảnh cũ
            if (tour.getImages() != null && tour.getImages().length > 0) {
                cloudinaryService.deleteImages(Arrays.asList(tour.getImages()));
            }

            // Upload ảnh mới
            List<String> imageUrls = cloudinaryService.uploadImages(newImages, tour.getName());
            tour.setImages(imageUrls.toArray(new String[0]));
        }

        Tour updatedTour = tourRepository.save(tour);
        log.info("Updated tour: {}", updatedTour.getName());

        return tourMapper.toResponseDTO(updatedTour);
    }

    /**
     * Xóa tour (soft delete)
     */
    @Transactional
    public void deleteTour(UUID id) {
        Tour tour = tourRepository.findByIdActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tour với ID: " + id));

        // Soft delete
        tour.setDeletedAt(LocalDateTime.now());
        tourRepository.save(tour);

        try {
            cloudinaryService.deleteAllTourImages(tour.getName());
            log.info("✅ Deleted tour: {} (ID: {})", tour.getName(), tour.getId());
        } catch (Exception e) {
            log.warn("⚠️ Failed to delete CloudinaryImages for tour: {} (ID: {}). Manual cleanup may be needed",
                    tour.getName(), tour.getId(), e);
            // Có thể ghi vào queue/log để admin cleanup sau
        }
    }

    /**
     * Lấy danh sách tour shorts
     */
    @Transactional(readOnly = true)
    public List<TourShortDTO> getTourShorts() {
        List<Tour> tours = tourRepository.findAllWithShortUrl();
        return tourMapper.toShortDTOList(tours);
    }

    /**
     * Lấy một tour short ngẫu nhiên
     */
    @Transactional(readOnly = true)
    public TourShortDTO getRandomTourShort() {
        long count = tourRepository.countToursWithShortUrl();

        if (count == 0) {
            return null;
        }

        List<Tour> tours = tourRepository.findAllWithShortUrl();
        int randomIndex = random.nextInt(tours.size());

        return tourMapper.toShortDTO(tours.get(randomIndex));
    }

    /**
     * Tìm tour theo vùng miền
     */
    @Transactional(readOnly = true)
    public Page<TourResponseDTO> getToursByRegion(String region, Pageable pageable) {
        return tourRepository.findByRegion(region, pageable)
                .map(tourMapper::toResponseDTO);
    }

    /**
     * Tìm tour theo tỉnh
     */
    @Transactional(readOnly = true)
    public Page<TourResponseDTO> getToursByProvince(String province, Pageable pageable) {
        return tourRepository.findByProvince(province, pageable)
                .map(tourMapper::toResponseDTO);
    }

    /**
     * Tìm tour theo danh mục
     */
    @Transactional(readOnly = true)
    public Page<TourResponseDTO> getToursByCategory(String category, Pageable pageable) {
        return tourRepository.findByCategory(category, pageable)
                .map(tourMapper::toResponseDTO);
    }

    /**
     * Tìm tour có thể đặt
     */
    @Transactional(readOnly = true)
    public Page<TourResponseDTO> getBookableTours(Pageable pageable) {
        return tourRepository.findBookableTours(pageable)
                .map(tourMapper::toResponseDTO);
    }

    /**
     * Tìm kiếm tour theo tên
     * - Hỗ trợ tìm kiếm không dấu (Đà Nẵng = Da Nang)
     * - Hỗ trợ tìm kiếm gần đúng (fuzzy matching với độ tương tự >= 60%)
     * - Nếu keyword rỗng, trả danh sách tất cả tour
     * - Sắp xếp theo độ phù hợp
     */
    @Transactional(readOnly = true)
    public Page<TourResponseDTO> searchTours(String keyword, Pageable pageable) {
        // Nếu keyword rỗng, trả tất cả tour
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllTours(pageable);
        }

        String normalizedKeyword = SearchUtil.normalizeKeyword(keyword);
        String[] tokens = normalizedKeyword.split("\\s+");

        List<Tour> candidates = tourRepository.findAllActiveList();

        List<Map.Entry<Integer, TourResponseDTO>> scored = new ArrayList<>();

        for (Tour t : candidates) {
            int totalScore = 0;
            boolean allMatched = true;

            // Tìm kiếm chỉ theo tên (bỏ description)
            for (String token : tokens) {
                int nameScore = SearchUtil.scoreMatch(t.getName(), token);
                if (nameScore == 0) {
                    allMatched = false;
                    break;
                }
                totalScore += nameScore;
            }

            if (allMatched) {
                scored.add(new AbstractMap.SimpleEntry<>(totalScore, tourMapper.toResponseDTO(t)));
            }
        }

        // sort giảm dần theo điểm
        scored.sort((a, b)
                -> Integer.compare(b.getKey(), a.getKey()));

        // pagination
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), scored.size());
        if (start >= scored.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, scored.size());
        }

        List<TourResponseDTO> pageContent = scored.subList(start, end)
                .stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        return new PageImpl<>(pageContent, pageable, scored.size());
    }

    // Helper methods
    private void validateTourDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new BadRequestException("Ngày bắt đầu và kết thúc không được để trống");
        }
        if (endDate.isBefore(startDate)) {
            throw new BadRequestException("Ngày kết thúc phải sau ngày bắt đầu");
        }
        if (startDate.isBefore(LocalDate.now())) {
            throw new BadRequestException("Ngày bắt đầu phải là ngày trong tương lai");
        }
    }
}
