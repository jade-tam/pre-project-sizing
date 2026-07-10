package vn.gtel.pm2.sizing.constant;

public final class RabbitMQConstants {

    private RabbitMQConstants() {
    }

    public static final class User {

        private User() {
        }

        public static final String EXCHANGE = "user.exchange";

        public static final class Route {

            private Route() {
            }

            public static final String REGISTERED = "user.registered";
        }
    }

    public static final class Queue {

        private Queue() {
        }

        public static final String EMAIL_NOTIFICATION =
                "email.notification.queue";

        public static final String PUSH_NOTIFICATION =
                "push.notification.queue";
    }
}