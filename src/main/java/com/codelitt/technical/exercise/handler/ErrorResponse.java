package com.codelitt.technical.exercise.handler;

public record ErrorResponse(String status, String message, Integer code) {
}