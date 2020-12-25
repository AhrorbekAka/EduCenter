package ecma.demo.educenter.entity.audit;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@MappedSuperclass
public abstract class DateAudit implements Serializable {
    @CreationTimestamp
    @Column(updatable = false)
//    @OrderBy(value = "createdAt")
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

}
