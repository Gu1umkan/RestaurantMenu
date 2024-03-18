package peaksoft.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peaksoft.dto.response.AllUsersResponse;
import peaksoft.dto.response.PaginationUser;
import peaksoft.entity.Claim;
import peaksoft.entity.User;
import peaksoft.exceptions.NotFoundException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface ClaimRepository extends JpaRepository<Claim, Long> {

    @Query("select c from Claim c where c.id =:claimId")
    Optional<Claim> findClaimById(Long claimId);
    default Claim getClaimById(Long claimId){
        return findClaimById(claimId).orElseThrow(() ->
                new NotFoundException("Claim with id: "+claimId+" not found"));
    }

    @Query("""
           select new peaksoft.dto.response.AllUsersResponse(c.id,c.lastName,c.firstName,
           c.email,c.role) from Claim c where c.restaurant.id = :restId""")
    List<AllUsersResponse> findAllClaimsByRestaurantId(Long restId);

    default Page<AllUsersResponse> findAllClaimsByRestaurantId(Long restId, Pageable pageable) {
        List<AllUsersResponse> claims = findAllClaimsByRestaurantId(restId);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), claims.size());
        return new PageImpl<>(claims.subList(start, end), pageable, claims.size());
    }


}