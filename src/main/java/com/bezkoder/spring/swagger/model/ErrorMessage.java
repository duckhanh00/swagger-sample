package com.bezkoder.spring.swagger.model;

import java.time.LocalDateTime;

public record ErrorMessage(LocalDateTime timestamp, int status, String error, String path) {
}
