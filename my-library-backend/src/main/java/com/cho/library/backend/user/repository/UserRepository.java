package com.cho.library.backend.user.repository;

import com.cho.library.backend.auth.model.Provider;
import com.cho.library.backend.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmailAndProvider(String email, Provider provider);
    Optional<UserEntity> findByProviderAndProviderId(Provider provider, String providerId);
    Optional<UserEntity> findByEmail(String email);
}
