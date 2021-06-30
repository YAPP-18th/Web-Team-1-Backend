//package com.yapp18.retrospect.config;
//
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//
//public final class PageResuest {
//
//    private int page;
//    private int pageSize;
//    private Sort.Direction direction;
//
//    public void setPage(int page) {
//        // 0 이하 값이 들어오면 1로
//        this.page = page <= 0 ? 1 : page;
//    }
//
//    public void setPageSize(int pageSize){
//        int DEFAULT = 20;
//        this.pageSize = pageSize <= 0 ? DEFAULT : pageSize;
//    }
//
//    public void setDirection(Sort.Direction direction){
//        this.direction = direction;
//    }
//
//    // getter
//    public org.springframework.data.domain.PageRequest of() {
//        return org.springframework.data.domain.PageRequest.of(page -1, pageSize, direction, "created_at");
//    }
//}
