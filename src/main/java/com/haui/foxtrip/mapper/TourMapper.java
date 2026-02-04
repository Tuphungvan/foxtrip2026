package com.haui.foxtrip.mapper;

import com.haui.foxtrip.dto.tour.TourRequestDTO;
import com.haui.foxtrip.dto.tour.TourResponseDTO;
import com.haui.foxtrip.dto.tour.TourShortDTO;
import com.haui.foxtrip.dto.tour.TourUpdateDTO;
import com.haui.foxtrip.entity.Tour;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TourMapper {
    
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "availableSlots", source = "slots")
    Tour toEntity(TourRequestDTO dto);
    
    @Mapping(target = "images", expression = "java(java.util.Arrays.asList(tour.getImages()))")
    @Mapping(target = "finalPrice", expression = "java(calculateFinalPrice(tour.getPrice(), tour.getDiscount()))")
    TourResponseDTO toResponseDTO(Tour tour);
    
    @Mapping(target = "thumbnail", expression = "java(tour.getImages() != null && tour.getImages().length > 0 ? tour.getImages()[0] : null)")
    TourShortDTO toShortDTO(Tour tour);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "availableSlots", ignore = true)
    void updateEntityFromDTO(TourUpdateDTO dto, @MappingTarget Tour tour);
    
    List<TourResponseDTO> toResponseDTOList(List<Tour> tours);
    
    List<TourShortDTO> toShortDTOList(List<Tour> tours);
    
    // Helper method để tính giá sau giảm
    default BigDecimal calculateFinalPrice(BigDecimal price, BigDecimal discount) {
        if (price == null || discount == null || discount.compareTo(BigDecimal.ZERO) == 0) {
            return price;
        }
        
        BigDecimal discountAmount = price.multiply(discount).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return price.subtract(discountAmount);
    }
}
