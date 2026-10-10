package com.natureevent.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// =========================================================
	// VALIDATION ERROR - 400
	// =========================================================

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException exception) {

		Map<String, String> errors = new HashMap<>();

		exception.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

		Map<String, Object> response = new HashMap<>();

		response.put("status", HttpStatus.BAD_REQUEST.value());
		response.put("message", "Validation failed");
		response.put("errors", errors);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	// =========================================================
	// RESOURCE NOT FOUND - 404
	// =========================================================

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException exception) {

		return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage());
	}

	// =========================================================
	// UNAUTHORIZED - 401
	// =========================================================

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<Map<String, Object>> handleUnauthorized(UnauthorizedException exception) {

		return buildResponse(HttpStatus.UNAUTHORIZED, exception.getMessage());
	}

	// =========================================================
	// FORBIDDEN - 403
	// =========================================================

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<Map<String, Object>> handleForbidden(ForbiddenException exception) {

		return buildResponse(HttpStatus.FORBIDDEN, exception.getMessage());
	}

	// =========================================================
	// RUNTIME EXCEPTION - 400
	// =========================================================

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException exception) {

		String message = exception.getMessage();

		if (message == null || message.isBlank()) {
			message = "Invalid request";
		}

		return buildResponse(HttpStatus.BAD_REQUEST, message);
	}

	// =========================================================
	// GENERAL EXCEPTION - 500
	// =========================================================

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGeneralException(Exception exception) {

		return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
	}

	// =========================================================
	// COMMON RESPONSE BUILDER
	// =========================================================

	private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {

		Map<String, Object> response = new HashMap<>();

		response.put("status", status.value());
		response.put("message", message);

		return ResponseEntity.status(status).body(response);
	}
}