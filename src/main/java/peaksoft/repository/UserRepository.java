package peaksoft.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peaksoft.dto.response.AllUsersResponse;
import peaksoft.entity.User;
import peaksoft.exceptions.NotFoundException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    @Query("select u from User u where  u.email = :email")
    Optional<User> findByEmail(String email);


    default User getByEmail(String email) {
        return findByEmail(email).orElseThrow(() ->
                new NotFoundException("User with email: " + email + " not found!"));
    }

    @Query("""
            select new peaksoft.dto.response.AllUsersResponse(c.id,c.lastName,c.firstName,
            c.email,c.role) from User c where c.restaurant.id = :restId
            """)
    List<AllUsersResponse> findAllByRestaurantId(Long restId);

    default Page<AllUsersResponse> findAllByRestaurantId(Long restId, Pageable pageable) {
        List<AllUsersResponse> users = findAllByRestaurantId(restId);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), users.size());
        return new PageImpl<>(users.subList(start, end), pageable, users.size());
    }
}

