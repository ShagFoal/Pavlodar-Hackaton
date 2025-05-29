package blog.auth.auth.dto;

import java.util.List;

public record Trend(
        String unit,
        List<TrendPoint> data
) {}