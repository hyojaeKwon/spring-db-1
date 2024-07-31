package test.jdbc.exception.translator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.transaction.annotation.Transactional;
import test.jdbc.connection.ConnectionConst;
import test.jdbc.domain.Member;
import test.jdbc.repository.ex.MyDbException;
import test.jdbc.repository.ex.MyDuplicateKeyException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

@Slf4j
public class ExTranslatorV1Test {

    Repository repository;
    Service service;

    @BeforeEach
    void init() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(ConnectionConst.URL,ConnectionConst.USERNAME,ConnectionConst.PASSWORD);
        repository = new Repository(dataSource);
        service = new Service(repository);
    }

    @Test
    @DisplayName("duplicate key 저장 시 새로운 key를 만들어서 저장")
    void duplicateKey() {
        String memberId = "id h  o";
        service.create(memberId);
        service.create(memberId);
    }


    @Slf4j
    @RequiredArgsConstructor
    static class Service {

        private final Repository repository;

        @Transactional
        public void create(String memberId) {
            try {
                repository.save(new Member(memberId, 0));
                log.info("saveId = {}", memberId);
            } catch (MyDuplicateKeyException e) {
                log.info("키 중복");
                String retryId = generateNewId(memberId);
                repository.save(new Member(retryId, 0));
                log.info("retryId = {}", retryId);
            } catch (MyDbException myDbException) {
                log.info("데이터 접근 계층 예외", myDbException);
            }

        }

        private String generateNewId(String id) {
            return id + new Random().nextInt(100);
        }
    }

    @RequiredArgsConstructor
    static class Repository {
        private final DataSource dataSource;

        public Member save(Member member) {
            String sql = "insert into member(member_id, money) values(?,?)";
            Connection con = null;
            PreparedStatement pstmt = null;

            try {
                con = dataSource.getConnection();
                pstmt = con.prepareStatement(sql);
                pstmt.setString(1, member.getMemberId());
                pstmt.setInt(2, member.getMoney());
                pstmt.executeUpdate();
                return member;
            } catch (SQLException e) {
                log.info("error", e);
                if (e.getErrorCode() == 23505) {
                    throw new MyDuplicateKeyException(e);
                }
                throw new MyDbException(e);
            } finally {
                JdbcUtils.closeStatement(pstmt);
                JdbcUtils.closeConnection(con);
            }
        }
    }
}
