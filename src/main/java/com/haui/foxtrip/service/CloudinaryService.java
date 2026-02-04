package com.haui.foxtrip.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.haui.foxtrip.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {
    
    private final Cloudinary cloudinary;
    
    private static final long MAX_FILE_SIZE = 25 * 1024 * 1024; // 25MB
    private static final List<String> ALLOWED_FORMATS = List.of("jpg", "jpeg", "png", "webp");
    
    /**
     * Upload một ảnh lên Cloudinary
     */
    public String uploadImage(MultipartFile file, String tourName) {
        validateFile(file);
        
        try {
            String folder = "tours/" + tourName.replaceAll("\\s+", "_");
            String publicId = tourName.replaceAll("\\s+", "_") + "_" + 
                            System.currentTimeMillis() + "_" + 
                            generateRandomString();
            
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                "folder", folder,
                "public_id", publicId,
                "resource_type", "auto"
            );
            
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            String imageUrl = (String) uploadResult.get("secure_url");
            
            log.info("✅ Uploaded image: {}", imageUrl);
            return imageUrl;
            
        } catch (IOException e) {
            log.error("❌ Error uploading image to Cloudinary", e);
            throw new BadRequestException("Không thể upload ảnh: " + e.getMessage());
        }
    }
    
    /**
     * Upload nhiều ảnh
     */
    public List<String> uploadImages(List<MultipartFile> files, String tourName) {
        List<String> imageUrls = new ArrayList<>();
        
        for (MultipartFile file : files) {
            String url = uploadImage(file, tourName);
            imageUrls.add(url);
        }
        
        return imageUrls;
    }
    
    /**
     * Xóa một ảnh từ Cloudinary
     */
    public void deleteImage(String imageUrl) {
        try {
            String publicId = extractPublicId(imageUrl);
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("🗑️ Deleted image: {} - Result: {}", publicId, result.get("result"));
            
        } catch (Exception e) {
            log.error("❌ Error deleting image from Cloudinary: {}", imageUrl, e);
            throw new BadRequestException("Không thể xóa ảnh: " + e.getMessage());
        }
    }
    
    /**
     * Xóa tất cả ảnh của một tour
     */
    public void deleteAllTourImages(String tourName) {
        try {
            String prefix = "tours/" + tourName.replaceAll("\\s+", "_");
            Map result = cloudinary.api().deleteResourcesByPrefix(prefix, ObjectUtils.emptyMap());
            log.info("🗑️ Deleted all images for tour '{}': {}", tourName, result.get("deleted"));
            
            // Xóa folder
            cloudinary.api().deleteFolder(prefix, ObjectUtils.emptyMap());
            log.info("🗑️ Deleted folder: {}", prefix);
            
        } catch (Exception e) {
            log.error("❌ Error deleting tour images: {}", tourName, e);
        }
    }
    
    /**
     * Xóa nhiều ảnh
     */
    public void deleteImages(List<String> imageUrls) {
        for (String url : imageUrls) {
            try {
                deleteImage(url);
            } catch (Exception e) {
                log.warn("Failed to delete image: {}", url);
            }
        }
    }
    
    // Helper methods
    
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File không được để trống");
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File không được vượt quá 25MB");
        }
        
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new BadRequestException("Tên file không hợp lệ");
        }
        
        String extension = getFileExtension(originalFilename).toLowerCase();
        if (!ALLOWED_FORMATS.contains(extension)) {
            throw new BadRequestException("Chỉ chấp nhận file: " + String.join(", ", ALLOWED_FORMATS));
        }
    }
    
    private String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return lastDot > 0 ? filename.substring(lastDot + 1) : "";
    }
    
    private String extractPublicId(String imageUrl) {
        // URL format: https://res.cloudinary.com/{cloud_name}/image/upload/v{version}/{folder}/{public_id}.{format}
        String[] parts = imageUrl.split("/");
        int uploadIndex = -1;
        
        for (int i = 0; i < parts.length; i++) {
            if ("upload".equals(parts[i])) {
                uploadIndex = i;
                break;
            }
        }
        
        if (uploadIndex == -1 || uploadIndex + 2 >= parts.length) {
            throw new BadRequestException("URL ảnh không hợp lệ");
        }
        
        // Lấy phần sau "upload/v{version}/"
        StringBuilder publicId = new StringBuilder();
        for (int i = uploadIndex + 2; i < parts.length; i++) {
            if (i > uploadIndex + 2) {
                publicId.append("/");
            }
            publicId.append(parts[i]);
        }
        
        // Loại bỏ extension
        String result = publicId.toString();
        int lastDot = result.lastIndexOf('.');
        return lastDot > 0 ? result.substring(0, lastDot) : result;
    }
    
    private String generateRandomString() {
        return Long.toHexString(Double.doubleToLongBits(Math.random())).substring(0, 9);
    }
}
