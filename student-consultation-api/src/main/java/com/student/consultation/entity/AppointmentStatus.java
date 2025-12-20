package com.student.consultation.entity;



public enum AppointmentStatus {
    PENDING,     // chờ duyệt
    CONFIRMED,   // đã xác nhận
    REJECTED,    // bị từ chối
    COMPLETED,   // đã hoàn thành
    CANCELLED_BY_STUDENT,
    CANCELLED_BY_LECTURER,
}
