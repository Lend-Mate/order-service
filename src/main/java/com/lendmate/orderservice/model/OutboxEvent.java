package com.lendmate.orderservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "outbox")
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "eventid")
    private UUID eventId;

    @Column(name = "aggregatetype")
    private String aggregateType;   // "Order"

    @Column(name = "aggregateid")
    private String aggregateId;     // order.id.toString()

    @Column(name = "type")
    private String type;            // "OrderCreated"

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String payload;

    @Column(name = "timestamp")
    private Instant timestamp = Instant.now();

    @Column(name = "trace_parent")
    private String trace_parent;
}
