package com.ldbc.driver.coordination;

import com.ldbc.driver.temporal.Time;

abstract class CompletionTimeEvent {

    public enum CoordinationEventType {
        // Operation started executing
        INITIATED,
        // Operation completed scheduling
        COMPLETED,
        // GCT came in from other process
        EXTERNAL,
        // Instruction to terminate when all results have arrived
        TERMINATE,
        // Request for future to GCT value (value will only be available once event is processed)
        FUTURE
    }

    public static InitiatedEvent initiated(Time time) {
        return new InitiatedEvent(time);
    }

    public static CompletedEvent completed(Time time) {
        return new CompletedEvent(time);
    }

    public static ExternalEvent external(String peerId, Time time) {
        return new ExternalEvent(peerId, time);
    }

    public static TerminationEvent terminate(long expectedEventCount) {
        return new TerminationEvent(expectedEventCount);
    }

    public static FutureEvent future(ThreadedQueuedCompletionTimeService.GlobalCompletionTimeFuture future) {
        return new FutureEvent(future);
    }

    abstract CoordinationEventType type();

    static class InitiatedEvent extends CompletionTimeEvent {
        private final Time time;

        private InitiatedEvent(Time time) {
            this.time = time;
        }

        @Override
        CoordinationEventType type() {
            return CoordinationEventType.INITIATED;
        }

        Time time() {
            return time;
        }
    }

    static class CompletedEvent extends CompletionTimeEvent {
        private final Time time;

        private CompletedEvent(Time time) {
            this.time = time;
        }

        @Override
        CoordinationEventType type() {
            return CoordinationEventType.COMPLETED;
        }

        Time time() {
            return time;
        }
    }

    static class ExternalEvent extends CompletionTimeEvent {
        private final Time time;
        private final String peerId;

        private ExternalEvent(String peerId, Time time) {
            this.time = time;
            this.peerId = peerId;
        }

        @Override
        CoordinationEventType type() {
            return CoordinationEventType.EXTERNAL;
        }

        Time time() {
            return time;
        }

        String peerId() {
            return peerId;
        }
    }

    static class TerminationEvent extends CompletionTimeEvent {
        private final long expectedEventCount;

        private TerminationEvent(long expectedEventCount) {
            this.expectedEventCount = expectedEventCount;
        }

        @Override
        CoordinationEventType type() {
            return CoordinationEventType.TERMINATE;
        }

        long expectedEventCount() {
            return expectedEventCount;
        }
    }

    static class FutureEvent extends CompletionTimeEvent {
        private final ThreadedQueuedCompletionTimeService.GlobalCompletionTimeFuture future;

        private FutureEvent(ThreadedQueuedCompletionTimeService.GlobalCompletionTimeFuture future) {
            this.future = future;
        }

        @Override
        CoordinationEventType type() {
            return CoordinationEventType.FUTURE;
        }

        ThreadedQueuedCompletionTimeService.GlobalCompletionTimeFuture future() {
            return future;
        }
    }
}