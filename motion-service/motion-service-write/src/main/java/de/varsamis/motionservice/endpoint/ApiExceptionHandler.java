package de.varsamis.motionservice.endpoint;

import de.varsamis.motion.model.Error;
import de.varsamis.motion.model.ErrorType;
import de.varsamis.motion.model.Layer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.util.UUID;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    private final String ERROR_CODE_PLACEHOLDER = "OSCP0005-%s";
    private final String TRACE_ID_HEADER_NAME = "requestId";

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Error> handleAll(Exception pException, WebRequest pWebRequest) {

        return createErrorResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.TECHNICAL_FAULT,
                "0001", "Unexpected Error!", pException, pWebRequest);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<Error> handleMissingRequestParameterException(
            MissingServletRequestParameterException pException, WebRequest pWebRequest) {

        return createErrorResponseEntity(HttpStatus.BAD_REQUEST, ErrorType.TECHNICAL_FAULT,
                "0004", "Missing Request Parameter!", pException, pWebRequest);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class,
            MethodArgumentTypeMismatchException.class, IllegalArgumentException.class})
    public ResponseEntity<Error> handleValidationException(Exception pException,
                                                           WebRequest pWebRequest) {

        return createErrorResponseEntity(HttpStatus.BAD_REQUEST, ErrorType.BUSINESS_FAULT,
                "0005", "Invalid Request Parameter!", pException, pWebRequest);
    }

    @ExceptionHandler({HttpClientErrorException.NotFound.class})
    public ResponseEntity<Error> handleResourceNotFoundException(
            HttpClientErrorException.NotFound pException, WebRequest pWebRequest) {

        return createErrorResponseEntity(HttpStatus.NOT_FOUND, ErrorType.BUSINESS_FAULT,
                "0006", "Resource not found!", pException, pWebRequest);
    }

    private ResponseEntity<Error> createErrorResponseEntity(HttpStatus pHttpStatus,
                                                            ErrorType pErrorType, String pErrorCode, String pMessagePart, Exception pException,
                                                            WebRequest pWebRequest) {

        UUID lTraceId = extractTraceIdFromRequest(pWebRequest);

        log.error(String.format("trace-%s: %s", lTraceId, pMessagePart), pException);

        return new ResponseEntity<>(
                createErrorFromException(pErrorCode, pErrorType, pMessagePart, pException, lTraceId),
                new HttpHeaders(), pHttpStatus);
    }

    private Error createErrorFromException(String pErrorCodeSuffix, ErrorType pErrorType,
                                           String pMessagePart, Exception pException, UUID pTraceId) {
        return new Error()
                .code(String.format(ERROR_CODE_PLACEHOLDER, pErrorCodeSuffix))
                .layer(Layer.APP)
                .type(pErrorType)
                .occurrence(OffsetDateTime.now())
                .message(String.format("%s -> %s", pMessagePart, pException.getMessage()))
                .detailMessage(ExceptionUtils.getStackTrace(pException))
                .traceId(pTraceId);
    }

    private UUID extractTraceIdFromRequest(WebRequest pWebRequest) {
        String lTraceId = pWebRequest.getHeader(TRACE_ID_HEADER_NAME);

        if (StringUtils.isBlank(lTraceId)) {
            return null;
        }

        try {
            return UUID.fromString(lTraceId);
        } catch (Exception lE) {
            log.warn(String.format(
                    "RequestId from Request with Description [%s] has wrong format. RequestId was: %s",
                    pWebRequest.getDescription(true), lTraceId));
            return null;
        }
    }
}
