package vn.gtel.pm2.sizing.constant;

public final class RabbitMQConstants {

    private RabbitMQConstants() {
    }

    public static final class DeadLetter {

        private DeadLetter() {
        }

        public static final String EXCHANGE = "dead-letter.exchange";

        public static final class RoutingKey {

            private RoutingKey() {
            }

            public static final String EMAIL_NOTIFICATION_FAILED = "email.notification.failed";
        }
    }

    public static final class User {

        private User() {
        }

        public static final String EXCHANGE = "user.exchange";

        public static final class RoutingKey {

            private RoutingKey() {
            }

            public static final String REGISTERED = "user.registered";
        }
    }

    public static final class Queue {

        private Queue() {
        }

        // Main queues
        public static final String EMAIL_NOTIFICATION =
                "email.notification.queue";
        public static final String PUSH_NOTIFICATION =
                "push.notification.queue";


        // Dead letter queues
        public static final String EMAIL_NOTIFICATION_DLQ =
                "email.notification.dlq";
        public static final String PUSH_NOTIFICATION_DLQ =
                "push.notification.dlq";
    }
}