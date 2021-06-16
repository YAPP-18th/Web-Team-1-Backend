package com.yapp18.retrospect.domain.recent;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface RecentRepository extends CrudRepository<RecentLog, Long> {
}
