package com.haui.foxtrip.document;

import com.haui.foxtrip.enums.ItemType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Document(collection = "service_contents")
@CompoundIndex(name = "item_type_id_idx", def = "{'serviceType': 1, 'serviceId': 1}", unique = true)
public class ServiceContent {
    
    @Id
    private String id;
    
    @Indexed
    private ItemType serviceType;
    
    private String serviceId;
    
    // Rich content
    private String descriptionHtml;
    private List<String> highlights;
    
    // Tour specific
    private List<DayItinerary> detailedItinerary;
    private List<String> included;
    private List<String> excluded;
    
    // Transportation specific
    private VehicleInfo vehicleInfo;
    private RouteDetails routeDetails;
    
    // Restaurant specific
    private List<MenuCategory> menu;
    private ChefInfo chefInfo;
    private List<String> atmosphere;
    
    // Hotel specific
    private List<String> amenities;
    private RoomDetails roomDetails;
    private PolicyInfo policies;
    private List<String> nearbyPlaces;
    
    // SEO
    private SeoInfo seo;
    
    @Indexed
    private LocalDateTime createdAt;
    
    @Indexed
    private LocalDateTime updatedAt;
    
    @Data
    @Builder
    public static class DayItinerary {
        private Integer day;
        private String title;
        private List<Activity> activities;
        private List<String> meals;
        private String accommodation;
    }
    
    @Data
    @Builder
    public static class Activity {
        private String time;
        private String description;
    }
    
    @Data
    @Builder
    public static class VehicleInfo {
        private String seatLayout;
        private Integer totalSeats;
        private List<String> amenities;
        private List<String> vehicleImages;
    }
    
    @Data
    @Builder
    public static class RouteDetails {
        private List<Stop> stops;
    }
    
    @Data
    @Builder
    public static class Stop {
        private String name;
        private String arrivalTime;
        private String departureTime;
    }
    
    @Data
    @Builder
    public static class MenuCategory {
        private String category;
        private List<MenuItem> items;
    }
    
    @Data
    @Builder
    public static class MenuItem {
        private String name;
        private Double price;
        private String description;
    }
    
    @Data
    @Builder
    public static class ChefInfo {
        private String name;
        private String biography;
    }
    
    @Data
    @Builder
    public static class RoomDetails {
        private List<String> amenities;
        private String bedType;
        private String area;
    }
    
    @Data
    @Builder
    public static class PolicyInfo {
        private String checkInTime;
        private String checkOutTime;
        private String cancellationPolicy;
        private Boolean petsAllowed;
        private Boolean smokingAllowed;
    }
    
    @Data
    @Builder
    public static class SeoInfo {
        private String title;
        private String description;
        private List<String> keywords;
    }
}
