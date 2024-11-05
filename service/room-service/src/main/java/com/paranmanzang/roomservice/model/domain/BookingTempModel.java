package com.paranmanzang.roomservice.model.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "예약 정보")
public class BookingTempModel {
    // 기존 필드들 유지

            private Long id;
            private boolean enabled;
            private LocalDate date;
            private Long roomId;
            private Long groupId;
            private String roomName;
            private String address;

}