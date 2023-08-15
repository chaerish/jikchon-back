package smu.likelion.jikchon.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smu.likelion.jikchon.domain.Image;
import smu.likelion.jikchon.domain.enumurate.Target;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
