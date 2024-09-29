package com.example.SpringSecurity.repository;

import com.example.SpringSecurity.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    // CURDATE() is a function that returns the current date. In some databases, this function is also used as CURRENT_DATE().

    @Query(value = "from Notice n where CURDATE() BETWEEN n.noticBegDt AND n.noticEndDt")
    List<Notice> findAllActiveNotices();

}
