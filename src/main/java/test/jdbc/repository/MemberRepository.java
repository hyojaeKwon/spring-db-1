package test.jdbc.repository;

import test.jdbc.domain.Member;

public interface MemberRepository {
    Member save(Member member);
    Member findById(String id);

    void update(String id, int money);
    void deleteById(String id);

    void deleteAll();
}
