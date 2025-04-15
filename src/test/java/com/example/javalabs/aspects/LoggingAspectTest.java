package com.example.javalabs.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.*;

class LoggingAspectTest {

    private LoggingAspect loggingAspect;
    private JoinPoint joinPoint;
    private Signature signature;
    private Logger logger;

    @BeforeEach
    void setUp() {
        loggingAspect = new LoggingAspect();
        joinPoint = mock(JoinPoint.class);
        signature = mock(Signature.class);
        logger = LoggerFactory.getLogger(LoggingAspect.class);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("testMethod");
    }

    @Test
    void logSuccessfulExecution_logsMethodCall() {
        Object[] args = new Object[]{"arg1", 123};
        Object result = "success";
        when(joinPoint.getArgs()).thenReturn(args);

        loggingAspect.logSuccessfulExecution(joinPoint, result);

        verify(joinPoint).getSignature();
        verify(joinPoint).getArgs();
    }

    @Test
    void logException_logsMethodFailure() {
        Object[] args = new Object[]{"arg1", 123};
        Throwable ex = new RuntimeException("Test exception");
        when(joinPoint.getArgs()).thenReturn(args);

        loggingAspect.logException(joinPoint, ex);

        verify(joinPoint).getSignature();
        verify(joinPoint).getArgs();
    }
}