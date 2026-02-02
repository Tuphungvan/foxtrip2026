package com.haui.foxtrip.document;

import com.haui.foxtrip.enums.ItemType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "service_contents")
@CompoundIndex(name = "item_type_id_idx", def = "{'itemType': 1, 'itemId': 1}", unique = true)
public class ServiceContent {
    
    @Id
    private String id;
    
    @Indexed
    private ItemType itemType;
    
    private String itemId;
    
    // Rich content
    private String descriptionHtml;
    private List<String> highlights;
    
    // Tour specific
    private List<ItineraryDay> itineraryDetailed;
    private List<String> includes;
    private List<String> excludes;
    
    // Transportation specific
    private VehicleInfo vehicleInfo;
    private RouteDetails routeDetails;
    
    // Restaurant specific
    private List<MenuCategory> menu;
    private ChefInfo chefInfo;
    private List<String> ambiance;
    
    // Hotel specific
    private List<String> facilities;
    private RoomDetails roomDetails;
    private Policies policies;
    private List<String> nearbyAttractions;
    
    // SEO
    private SeoInfo seo;
    
    @Indexed
    private LocalDateTime createdAt;
    
    @Indexed
    private LocalDateTime updatedAt;
    
    // Nested classes
    @Data
    public static class ItineraryDay {
        private Integer day;
        private String title;
        private List<Activity> activities;
        private List<String> meals;
        private String accommodation;
    }
    
    @Data
    public static class Activity {
        private String time;
        private String description;
    }
    
    @Data
    public static class VehicleInfo {
        private String seatsLayout;
        private Integer totalSeats;
        private List<String> amenities;
        private List<String> vehicleImages;
    }
    
    @Data
    public static class RouteDetails {
        private List<Stop> stops;
    }
    
    @Data
    public static class Stop {
        private String name;
        private String arrival;
        private String departure;
    }
    
    @Data
    public static class MenuCategory {
        private String category;
        private List<MenuItem> items;
    }
    
    @Data
    public static class MenuItem {
        private String name;
        private Double price;
        private String description;
    }
    
    @Data
    public static class ChefInfo {
        private String name;
        private String bio;
    }
    
    @Data
    public static class RoomDetails {
        private List<String> amenities;
        private String bedType;
        private String roomSize;
    }
    
    @Data
    public static class Policies {
        private String checkIn;
        private String checkOut;
        private String cancellation;
        private Boolean pets;
        private Boolean smoking;
    }
    
    @Data
    public static class SeoInfo {
        private String metaTitle;
        private String metaDescription;
        private List<String> keywords;
    }
}
