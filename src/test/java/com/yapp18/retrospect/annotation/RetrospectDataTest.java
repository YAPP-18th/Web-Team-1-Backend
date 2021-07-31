package com.yapp18.retrospect.annotation;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.yapp18.retrospect.config.DBUnitConfig;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) //  어노테이션을 작성할 곳, @Target(ElementType.FIELD)로 지정해주면, 필드에만 어노테이션을 달 수 있습니다.
@Retention(RetentionPolicy.RUNTIME) // 어노테이션의 지속 시간을 정합니다. 이 어노테이션은 런타임시에도 .class 파일에 존재 합니다. 커스텀 어노테이션을 만들 때 주로 사용합니다. Reflection 사용 가능이 가능합니다.
@Import(DBUnitConfig.class) // https://younicode.tistory.com/entry/Spring-boot%EC%97%90%EC%84%9C-DBUnit-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0
@DataJpaTest // https://webcoding-start.tistory.com/20, https://howtodoinjava.com/spring-boot2/testing/datajpatest-annotation/
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Replace.NONE로 설정하면 @ActiveProfiles에 설정한 프로파일 환경값에 따라 데이터 소스가 적용됩니다.
// TestContextManager에 어떤 TestExecutionListener들이 등록되어야 하는지 설정할 수 있게 지원합니다.
@ContextConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, // 스프링 테스트 환경에서 일반적으로 사용되는 의존성 주입 (DI) 리스너입니다. 테스트 인스턴스에 대한 의존성 주입 (DI) 을 제공합니다.
        TransactionDbUnitTestExecutionListener.class })
@DbUnitConfiguration(databaseConnection = "dbUnitDatabaseConnection")
public @interface RetrospectDataTest {
}
