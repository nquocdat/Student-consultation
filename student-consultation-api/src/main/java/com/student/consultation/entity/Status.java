package com.student.consultation.entity;

public enum Status {
    PENDING,            // Chờ giảng viên duyệt
    APPROVED,           // Giảng viên đã duyệt
    REJECTED,           // Giảng viên từ chối ngay từ đầu
    CANCELLED,          // Đã bị hủy (bởi GV hoặc SV)
    CANCEL_REQUEST,     // SV yêu cầu hủy sau khi lịch đã duyệt
    COMPLETED           // Buổi tư vấn đã diễn ra

}
