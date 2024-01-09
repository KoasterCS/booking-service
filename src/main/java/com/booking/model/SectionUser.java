package com.booking.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionUser {
    private String sectionId;
    private User user;
}
