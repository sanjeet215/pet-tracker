package org.asiczen.pettracker.exception;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", new Date());
		body.put("status", status.value());

		// Get all errors
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
				.collect(Collectors.toList());
		body.put("message", errors);

		return new ResponseEntity<>(body, headers, status);

	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<CustomErrorResponse> resourceNotFoundException(Exception ex, WebRequest request) {

		CustomErrorResponse errors = new CustomErrorResponse();
		errors.setTimestamp(LocalDateTime.now());
		errors.setStatus(HttpStatus.NOT_FOUND.value());
		errors.setMessage(ex.getLocalizedMessage());

		return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(ResourceAlreadyExistException.class)
	public ResponseEntity<CustomErrorResponse> resourceAlreadyExistException(Exception ex, WebRequest request) {

		CustomErrorResponse errors = new CustomErrorResponse();
		errors.setTimestamp(LocalDateTime.now());
		errors.setStatus(HttpStatus.CONFLICT.value());
		errors.setMessage(ex.getLocalizedMessage());

		return new ResponseEntity<>(errors, HttpStatus.CONFLICT);

	}

//	@ExceptionHandler({ AccessisDeniedException.class, AccessDeniedException.class })
//	public ResponseEntity<CustomErrorResponse> AccessisDeniedException(Exception ex, WebRequest request) {
//
//		CustomErrorResponse errors = new CustomErrorResponse();
//		errors.setTimestamp(LocalDateTime.now());
//		errors.setStatus(HttpStatus.UNAUTHORIZED.value());
//		errors.setMessage(ex.getLocalizedMessage());
//
//		return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
//
//	}

	@ExceptionHandler({ InternalServerError.class })
	public ResponseEntity<CustomErrorResponse> internalServerExceptionException(Exception ex, WebRequest request) {

		CustomErrorResponse errors = new CustomErrorResponse();
		errors.setTimestamp(LocalDateTime.now());
		errors.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errors.setMessage(ex.getLocalizedMessage());

		return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
