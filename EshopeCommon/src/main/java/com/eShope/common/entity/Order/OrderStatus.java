package com.eShope.common.entity.Order;

public enum OrderStatus {

    NEW{
        @Override
        public String defaultDescription() {
            return "Order was placed by the customer.";
        }
    },
    CANCELLED{
        @Override
        public String defaultDescription() {
            return "Order was cancelled.";
        }
    },
    PROCESSING {
        @Override
        public String defaultDescription() {
            return "Order is being processed.";
        }
    },
    PACKAGED {
        @Override
        public String defaultDescription() {
            return "products were packaged";
        }
    },
    PICKED {
        @Override
        public String defaultDescription() {
            return "Shipper picked the package.";
        }
    },
    SHIPPING {
        @Override
        public String defaultDescription() {
            return "Shipper is on the way to deliver your package.";
        }
    },
    DELIVERED {
        @Override
        public String defaultDescription() {
            return "Your Order has been delivered.";
        }
    },
    RETURNED {
        @Override
        public String defaultDescription() {
            return "Products were returned successfully";
        }
    },
    RETURN_REQUESTED{
        @Override
        public String defaultDescription() {
            return "Customer has requested to return the order.";
        }
    },
    PAID {
        @Override
        public String defaultDescription() {
            return "Customer has paid this order";
        }
    },
    REFUNDED {
        @Override
        public String defaultDescription() {
            return "Refund has been initiated";
        }
    };

    public abstract String defaultDescription();
}
