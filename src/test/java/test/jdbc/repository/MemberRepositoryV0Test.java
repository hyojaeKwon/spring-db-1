package test.jdbc.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.jdbc.domain.Member;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 memberRepositoryV0 = new MemberRepositoryV0();

    @BeforeEach
    void delete() {
        memberRepositoryV0.deleteAll();
    }

    @Test
    void crud() throws SQLException {
        Member member = new Member("member1", 1000);
        memberRepositoryV0.save(member);

        Member findMember = memberRepositoryV0.findById(member.getMemberId());
        log.info("findMember = {} ", findMember);

        assertThat(member.getMemberId()).isEqualTo(findMember.getMemberId());
        memberRepositoryV0.deleteById(member.getMemberId());
        Assertions.assertThatThrownBy(() -> memberRepositoryV0.findById(member.getMemberId())).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void update() throws SQLException{
        Member member = new Member("member", 1000);
        memberRepositoryV0.save(member);

        memberRepositoryV0.update(member.getMemberId(), 3000);
        Member byId = memberRepositoryV0.findById(member.getMemberId());
        assertThat(byId.getMoney()).isEqualTo(3000);
    }
}