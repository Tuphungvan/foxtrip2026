package com.haui.foxtrip.util;

import lombok.*;

import java.text.Normalizer;

@Data
public class SearchUtil {
    /**
     * Chuyển đổi chuỗi có dấu thành chuỗi không dấu
     * Ví dụ: "Đà Nẵng" -> "Da Nang"
     */
    public static String removeAccents(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String nfd = Normalizer.normalize(input, Normalizer.Form.NFD);
        return nfd.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    }

    /**
     * Tính độ tương tự Levenshtein giữa hai chuỗi (0-1)
     * Sử dụng cho fuzzy matching
     * 1.0 = giống hệt, 0.0 = hoàn toàn khác
     */
    public static double levenshteinSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return 0.0;
        }

        int maxLength = Math.max(s1.length(), s2.length());
        if (maxLength == 0) {
            return 1.0; // Cả hai đều rỗng
        }

        int distance = levenshteinDistance(s1, s2);
        return 1.0 - ((double) distance / maxLength);
    }

    /**
     * Tính khoảng cách Levenshtein (số lượng thay đổi tối thiểu)
     */
    private static int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(
                                dp[i - 1][j] + 1,      // deletion
                                dp[i][j - 1] + 1       // insertion
                        ),
                        dp[i - 1][j - 1] + cost   // substitution
                );
            }
        }

        return dp[s1.length()][s2.length()];
    }

    /**
     * Kiểm tra xem chuỗi con có khớp với chuỗi chính (không dấu, case-insensitive)
     * Sử dụng cho exact substring matching
     */
    public static boolean matchesWithoutAccents(String text, String pattern) {
        if (text == null || pattern == null) {
            return false;
        }

        String normalizedText = removeAccents(text).toLowerCase();
        String normalizedPattern = removeAccents(pattern).toLowerCase();

        return normalizedText.contains(normalizedPattern);
    }

    /**
     * Xếp hạng kết quả tìm kiếm dựa trên mức độ phù hợp
     * Ưu tiên: exact match > prefix match > fuzzy match
     */
    public static int scoreMatch(String text, String keyword) {
        if (text == null || keyword == null) {
            return 0;
        }

        String normalizedText = removeAccents(text).toLowerCase();
        String normalizedKeyword = removeAccents(keyword).toLowerCase();

        // Exact match: 100 điểm
        if (normalizedText.equals(normalizedKeyword)) {
            return 100;
        }

        // Exact substring match: 80 điểm
        if (normalizedText.contains(normalizedKeyword)) {
            // Ưu tiên match ở đầu
            if (normalizedText.startsWith(normalizedKeyword)) {
                return 85;
            }
            return 80;
        }

        // Fuzzy match: 50-79 điểm (dựa trên độ tương tự)
        double similarity = levenshteinSimilarity(normalizedText, normalizedKeyword);
        if (similarity >= 0.6) { // Chỉ chấp nhận nếu có ít nhất 60% giống
            return 50 + (int) (similarity * 29);
        }

        return 0; // Không khớp
    }

    /**
     * Normalize keyword cho tìm kiếm (loại bỏ dấu, khoảng trắng thừa, lowercase)
     */
    public static String normalizeKeyword(String keyword) {
        if (keyword == null) {
            return "";
        }

        return removeAccents(keyword)
                .toLowerCase()
                .trim()
                .replaceAll("\\s+", " ");
    }

}
