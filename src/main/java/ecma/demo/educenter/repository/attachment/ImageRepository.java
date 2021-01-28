package ecma.demo.educenter.repository.attachment;

import ecma.demo.educenter.entity.attachment.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<ImageModel, UUID> {
}
