package com.fit.ecommercialmarketplacebe.repository;

import com.fit.ecommercialmarketplacebe.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Integer> { // ID là Integer (vì kế thừa từ User)

    // Dùng để tìm thực thể Buyer bằng userId (là ID kế thừa từ User)
    Optional<Buyer> findByUserId(Integer userId);
}