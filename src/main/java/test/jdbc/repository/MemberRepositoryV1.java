package test.jdbc.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;
import test.jdbc.connection.DBConnectionUtil;
import test.jdbc.domain.Member;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC - DataSource 사용, Jdbc utils 사용
 */
@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryV1 {

    private final DataSource dataSource;

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values(?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("error", e);
            throw e;
        } finally {
            close(conn, pstmt, null);
        }
    }

    public void deleteAll() {
        String sql = "delete from member";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.execute();
        } catch (SQLException e) {
            log.error("error", e);
        }
        finally{
            close(conn, pstmt, null);
        }
    }

    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;


        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberId);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member id not found" + memberId);
            }
        } catch (SQLException e) {
            log.error(String.valueOf(e));
            throw e;
        } finally {
            close(conn, pstmt, rs);
        }

    }

    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money = ? where member_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error(String.valueOf(e));
            throw e;
        } finally {
            close(conn, pstmt, null);
        }
    }


    public void deleteById(String memberId) throws SQLException {
        String sql = "delete from member where member_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error(String.valueOf(e));
            throw e;
        } finally {
            close(conn, pstmt, null);
        }
    }


    private Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }

    private void close(Connection conn, Statement stmt, ResultSet rs) {
        JdbcUtils.closeConnection(conn);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeResultSet(rs);
    }
}

