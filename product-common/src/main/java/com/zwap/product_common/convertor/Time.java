package com.zwap.product_common.convertor;

import com.google.protobuf.Timestamp;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Time {
    public static LocalDateTime fromTs(Timestamp ts) {
        return ts == null ? null :
                LocalDateTime.ofInstant(
                        java.time.Instant.ofEpochSecond(ts.getSeconds(), ts.getNanos()),
                        ZoneId.systemDefault()
                );
    }

    public static Timestamp toTs(LocalDateTime ldt) {
        if (ldt == null) return null;
        java.time.Instant inst = ldt.atZone(ZoneId.systemDefault()).toInstant();
        return Timestamp.newBuilder()
                .setSeconds(inst.getEpochSecond())
                .setNanos(inst.getNano())
                .build();
    }
}
