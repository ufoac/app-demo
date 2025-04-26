package com.demo.app.infrastructure.repository.test;

import com.demo.app.domain.repository.ICommonRepo;
import com.demo.app.infrastructure.repository.test.mapper.TestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


/**
 * The type Test repo.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class TestRepo implements ICommonRepo {
    private final TestMapper testMapper;

    @Override
    public LocalDateTime now() {
        return testMapper.now();
    }


    @Override
    public String timezone() {
        return testMapper.timezone();
    }
}
