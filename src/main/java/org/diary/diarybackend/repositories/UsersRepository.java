package org.diary.diarybackend.repositories;

import org.diary.diarybackend.entities.USERS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 사용자 데이터에 대한 데이터베이스 작업을 추상화하는 JPA 리포지토리 인터페이스.
 * 이 인터페이스는 USERS 엔티티의 지속성을 관리하며, Spring Data JPA가 제공하는 JpaRepository를 확장하여 사용합니다.
 * JpaRepository에는 엔티티의 생성, 조회, 업데이트, 삭제(CRUD) 등의 기본적인 데이터 접근 메서드가 정의되어 있습니다.
 * <p>
 * 주요 기능:
 * - findById(String id): 사용자 ID를 기반으로 해당 사용자의 정보를 조회합니다. 조회 결과는 Optional 객체로 반환되어,
 * 사용자가 존재하지 않는 경우 쉽게 처리할 수 있도록 합니다.
 * <p>
 * 특징:
 * - @Repository: 이 인터페이스가 데이터 접근 계층의 컴포넌트임을 나타내며, Spring IoC 컨테이너에 의해 관리됩니다.
 * <p>
 * 사용 예:
 * - UsersRepository 인터페이스를 주입받아 사용하는 서비스 또는 컨트롤러 클래스에서 이 인터페이스의 메서드를 호출하여
 * 데이터베이스에서 사용자 정보를 조회할 수 있습니다. findById 메서드를 사용하여 특정 ID의 사용자 존재 여부를 확인하고,
 * 해당 사용자의 데이터를 가져올 수 있습니다.
 */

@Repository
public interface UsersRepository extends JpaRepository<USERS, String> {
    Optional<USERS> findById(String id);
}
