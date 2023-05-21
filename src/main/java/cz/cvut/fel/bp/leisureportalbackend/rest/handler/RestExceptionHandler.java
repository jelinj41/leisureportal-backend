package cz.cvut.fel.bp.leisureportalbackend.rest.handler;

import cz.cvut.fel.bp.leisureportalbackend.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RestExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * Logs the caught exception.
     *
     * @param ex The caught exception.
     */
    private static void logException(RuntimeException ex) {
        LOG.error("Exception caught:", ex);
    }

    /**
     * Creates an ErrorInfo object with the error message and request URI.
     *
     * @param request The HttpServletRequest object.
     * @param e       The caught exception.
     * @return The ErrorInfo object.
     */
    private static ErrorInfo errorInfo(HttpServletRequest request, Throwable e) {
        return new ErrorInfo(e.getMessage(), request.getRequestURI());
    }

    /**
     * Exception handler for NotFoundException.
     *
     * @param request The HttpServletRequest object.
     * @param e       The NotFoundException object.
     * @return ResponseEntity containing the ErrorInfo object and HttpStatus.NOT_FOUND.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorInfo> resourceNotFound(HttpServletRequest request, NotFoundException e) {
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.NOT_FOUND);
    }

    /**
     * Exception handler for AlreadyExistsException.
     *
     * @param request The HttpServletRequest object.
     * @param e       The AlreadyExistsException object.
     * @return ResponseEntity containing the ErrorInfo object and HttpStatus.CONFLICT.
     */
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorInfo> resourceAlreadyExistsFound(HttpServletRequest request, AlreadyExistsException e) {
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.CONFLICT);
    }

    /**
     * Exception handler for NotAllowedException.
     *
     * @param request The HttpServletRequest object.
     * @param e       The NotAllowedException object.
     * @return ResponseEntity containing the ErrorInfo object and HttpStatus.FORBIDDEN.
     */

    @ExceptionHandler(NotAllowedException.class)
    public ResponseEntity<ErrorInfo> notAllowed(HttpServletRequest request, NotAllowedException e) {
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.FORBIDDEN);
    }

    /**
     * Exception handler for AlreadyLoginException.
     *
     * @param request The HttpServletRequest object.
     * @param e       The AlreadyLoginException object.
     * @return ResponseEntity containing the ErrorInfo object and HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(AlreadyLoginException.class)
    public ResponseEntity<ErrorInfo> alreadyLogin(HttpServletRequest request, AlreadyLoginException e) {
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception handler for BadPassword.
     *
     * @param request The HttpServletRequest object.
     * @param e       The BadPassword object.
     * @return ResponseEntity containing the ErrorInfo object and HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(BadPassword.class)
    public ResponseEntity<ErrorInfo> badPassword(HttpServletRequest request, BadPassword e) {
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception handler for UnauthorizedException.
     *
     * @param request The HttpServletRequest object.
     * @param e       The UnauthorizedException object.
     * @return ResponseEntity containing the ErrorInfo object and HttpStatus.UNAUTHORIZED.
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorInfo> unauthorized(HttpServletRequest request, UnauthorizedException e) {
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.UNAUTHORIZED);
    }

}
