package test.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.jdbc.connection.ConnectionConst;
import test.jdbc.domain.Member;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class MemberRepositoryV1Test {

    MemberRepositoryV1 memberRepositoryV1;

    @BeforeEach
    void beforeEach() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(ConnectionConst.URL);
        dataSource.setUsername(ConnectionConst.USERNAME);
        dataSource.setPassword(ConnectionConst.PASSWORD);
        dataSource.setMaximumPoolSize(8);
        dataSource.setPoolName("hikari");


        memberRepositoryV1 = new MemberRepositoryV1(dataSource);
        memberRepositoryV1.deleteAll();
    }

    @Test
    void crud() throws SQLException {
        Member member = new Member("member1", 1000);
        memberRepositoryV1.save(member);

        Member findMember = memberRepositoryV1.findById(member.getMemberId());
         log.info("findMember = {} ", findMember);

        assertThat(member.getMemberId()).isEqualTo(findMember.getMemberId());
        memberRepositoryV1.deleteById(member.getMemberId());
        Assertions.assertThatThrownBy(() -> memberRepositoryV1.findById(member.getMemberId())).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void update() throws SQLException{
        Member member = new Member("member", 1000);
        memberRepositoryV1.save(member);

        memberRepositoryV1.update(member.getMemberId(), 3000);
        Member byId = memberRepositoryV1.findById(member.getMemberId());
        assertThat(byId.getMoney()).isEqualTo(3000);
    }
}