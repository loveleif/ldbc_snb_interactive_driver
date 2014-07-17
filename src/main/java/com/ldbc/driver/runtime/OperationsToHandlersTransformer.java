package com.ldbc.driver.runtime;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.ldbc.driver.*;
import com.ldbc.driver.runtime.coordination.ConcurrentCompletionTimeService;
import com.ldbc.driver.runtime.coordination.ReadOnlyConcurrentCompletionTimeService;
import com.ldbc.driver.runtime.metrics.ConcurrentMetricsService;
import com.ldbc.driver.runtime.scheduling.GctDependencyCheck;
import com.ldbc.driver.runtime.scheduling.Spinner;
import com.ldbc.driver.temporal.TimeSource;

import java.util.Iterator;
import java.util.Map;

// TODO test
class OperationsToHandlersTransformer {
    private final TimeSource TIME_SOURCE;
    private final Db db;
    private final Spinner spinner;
    private final ConcurrentCompletionTimeService completionTimeService;
    private final ConcurrentErrorReporter errorReporter;
    private final ConcurrentMetricsService metricsService;
    private final Map<Class<? extends Operation>, OperationClassification> operationClassifications;

    OperationsToHandlersTransformer(TimeSource timeSource,
                                    Db db,
                                    Spinner spinner,
                                    ConcurrentCompletionTimeService completionTimeService,
                                    ConcurrentErrorReporter errorReporter,
                                    ConcurrentMetricsService metricsService,
                                    Map<Class<? extends Operation>, OperationClassification> operationClassifications) {
        this.TIME_SOURCE = timeSource;
        this.db = db;
        this.spinner = spinner;
        this.completionTimeService = completionTimeService;
        this.errorReporter = errorReporter;
        this.metricsService = metricsService;
        this.operationClassifications = operationClassifications;
    }

    Iterator<OperationHandler<?>> transform(Iterator<Operation<?>> operations) throws WorkloadException {
        try {
            final ReadOnlyConcurrentCompletionTimeService readOnlyConcurrentCompletionTimeService = new ReadOnlyConcurrentCompletionTimeService(completionTimeService);
            return Iterators.transform(operations, new Function<Operation<?>, OperationHandler<?>>() {
                @Override
                public OperationHandler<?> apply(Operation<?> operation) {
                    try {
                        OperationHandler<?> operationHandler = db.getOperationHandler(operation);
                        switch (operationClassifications.get(operation.getClass()).gctMode()) {
                            case READ_WRITE:
                                operationHandler.init(TIME_SOURCE, spinner, operation, completionTimeService, errorReporter, metricsService);
                                operationHandler.addCheck(new GctDependencyCheck(completionTimeService, operation, errorReporter));
                                break;
                            case READ:
                                operationHandler.init(TIME_SOURCE, spinner, operation, readOnlyConcurrentCompletionTimeService, errorReporter, metricsService);
                                operationHandler.addCheck(new GctDependencyCheck(completionTimeService, operation, errorReporter));
                                break;
                            case NONE:
                                operationHandler.init(TIME_SOURCE, spinner, operation, readOnlyConcurrentCompletionTimeService, errorReporter, metricsService);
                                break;
                            default:
                                throw new WorkloadException(String.format("Unrecognized GctMode: %s", operationClassifications.get(operation.getClass()).gctMode()));
                        }
                        return operationHandler;
                    } catch (Exception e) {
                        errorReporter.reportError(this, String.format("Unexpected error in operationsToHandlers()\n%s", ConcurrentErrorReporter.stackTraceToString(e)));
                        throw new RuntimeException("Unexpected error in operationsToHandlers()", e);
                    }
                }
            });
        } catch (Throwable e) {
            throw new WorkloadException("Error encountered while transforming Operation stream to OperationHandler stream", e);
        }
    }
}