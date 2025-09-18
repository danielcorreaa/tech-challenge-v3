package com.techchallenge.infrastructure.external.dtos;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentResponseML(
        long id,

        String status,

        @JsonProperty("external_reference")
        String externalReference,

        @JsonProperty("preference_id")
        String preferenceId,

        List<Object> payments,
        List<Object> shipments,
        List<Object> payouts,

        Collector collector,

        String marketplace,

        @JsonProperty("notification_url")
        String notificationUrl,

        @JsonProperty("date_created")
        String dateCreated,

        @JsonProperty("last_updated")
        String lastUpdated,

        @JsonProperty("sponsor_id")
        Long sponsorId,

        @JsonProperty("shipping_cost")
        BigDecimal shippingCost,

        @JsonProperty("total_amount")
        BigDecimal totalAmount,

        @JsonProperty("site_id")
        String siteId,

        @JsonProperty("paid_amount")
        BigDecimal paidAmount,

        @JsonProperty("refunded_amount")
        BigDecimal refundedAmount,

        Payer payer,

        List<Item> items,

        boolean cancelled,

        @JsonProperty("additional_info")
        String additionalInfo,

        @JsonProperty("application_id")
        String applicationId,

        @JsonProperty("is_test")
        boolean isTest,

        @JsonProperty("order_status")
        String orderStatus,

        @JsonProperty("client_id")
        String clientId
) {
    public record Collector(
            long id,
            String email,
            String nickname
    ) {}

    public record Item(
            String id,

            @JsonProperty("category_id")
            String categoryId,

            @JsonProperty("currency_id")
            String currencyId,

            String description,

            @JsonProperty("picture_url")
            String pictureUrl,

            String title,
            int quantity,

            @JsonProperty("unit_price")
            BigDecimal unitPrice
    ) {}

    public record Payer(
            String id,
            String email,
            String nickname
    ) {}

}
